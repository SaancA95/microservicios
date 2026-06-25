package com.biblioteca.prestamos.service;

import com.biblioteca.prestamos.dto.PrestamoDTO;
import com.biblioteca.prestamos.model.Prestamo;
import com.biblioteca.prestamos.repository.PrestamoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class PrestamosService {

    private static final Logger logger = LoggerFactory.getLogger(PrestamosService.class);
    private static final long TTL_SEGUNDOS = 60L;
    private static final String CACHE_PREFIX = "prestamo:";

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Prestamo crearPrestamo(PrestamoDTO dto) {
        Prestamo prestamo = new Prestamo(dto.getIsbn(), dto.getMiembroId());
        Prestamo guardado = prestamoRepository.save(prestamo);
        logger.info("Préstamo creado: {}", guardado.getId());
        return guardado;
    }

    public Map<String, Object> getPrestamoById(String id) {
        String cacheKey = CACHE_PREFIX + id;

        // 1. Buscar en Redis
        Object cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            logger.info("CACHE HIT para préstamo: {}", id);
            Prestamo prestamo = convertirAPrestamo(cached);
            return buildResponse(prestamo, "CACHE Redis (~2ms)");
        }

        // 2. CACHE MISS → consultar PostgreSQL
        logger.info("CACHE MISS para préstamo: {}. Consultando BASE DE DATOS...", id);
        Optional<Prestamo> optional = prestamoRepository.findById(id);
        if (optional.isEmpty()) {
            return null;
        }

        Prestamo prestamo = optional.get();

        // 3. Guardar en Redis con TTL 60s
        redisTemplate.opsForValue().set(cacheKey, prestamo, TTL_SEGUNDOS, TimeUnit.SECONDS);
        logger.info("Préstamo {} guardado en caché por {} segundos", id, TTL_SEGUNDOS);

        return buildResponse(prestamo, "BASE DE DATOS (~80ms)");
    }

    public Prestamo devolverPrestamo(String id) {
        Optional<Prestamo> optional = prestamoRepository.findById(id);
        if (optional.isEmpty()) return null;

        Prestamo prestamo = optional.get();
        prestamo.setEstado("DEVUELTO");
        prestamo.setFechaDevolucionReal(new Date().toString());
        prestamoRepository.save(prestamo);

        // Invalidar caché
        redisTemplate.delete(CACHE_PREFIX + id);
        logger.info("Préstamo {} marcado como DEVUELTO, caché invalidada", id);

        return prestamo;
    }

    private Map<String, Object> buildResponse(Prestamo prestamo, String fuente) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("fuente", fuente);
        response.put("prestamo", prestamo);
        return response;
    }

    @SuppressWarnings("unchecked")
    private Prestamo convertirAPrestamo(Object obj) {
        if (obj instanceof Prestamo) return (Prestamo) obj;
        if (obj instanceof LinkedHashMap) {
            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) obj;
            Prestamo p = new Prestamo();
            p.setId((String) map.get("id"));
            p.setIsbn((String) map.get("isbn"));
            p.setMiembroId((String) map.get("miembroId"));
            p.setFechaPrestamo((String) map.get("fechaPrestamo"));
            p.setFechaDevolucionEstimada((String) map.get("fechaDevolucionEstimada"));
            p.setFechaDevolucionReal((String) map.get("fechaDevolucionReal"));
            p.setEstado((String) map.get("estado"));
            return p;
        }
        return null;
    }
}
