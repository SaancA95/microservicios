package com.biblioteca.miembros.service;

import com.biblioteca.miembros.dto.MiembroDTO;
import com.biblioteca.miembros.model.Miembro;
import com.biblioteca.miembros.repository.MiembroRepository;
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
public class MiembrosService {

    private static final Logger logger = LoggerFactory.getLogger(MiembrosService.class);
    private static final long TTL_SEGUNDOS = 120L; // TTL diferente: 120s para miembros
    private static final String CACHE_PREFIX = "miembro:";

    @Autowired
    private MiembroRepository miembroRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Map<String, Object> getMiembroById(String id) {
        String cacheKey = CACHE_PREFIX + id;

        // 1. Buscar en Redis
        Object cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            logger.info("CACHE HIT para miembro: {}", id);
            Miembro miembro = convertirAMiembro(cached);
            return buildResponse(miembro, "CACHE Redis (~2ms)");
        }

        // 2. CACHE MISS → consultar PostgreSQL
        logger.info("CACHE MISS para miembro: {}. Consultando BASE DE DATOS...", id);
        Optional<Miembro> optional = miembroRepository.findById(id);
        if (optional.isEmpty()) {
            return null;
        }

        Miembro miembro = optional.get();

        // 3. Guardar en Redis con TTL 120s
        redisTemplate.opsForValue().set(cacheKey, miembro, TTL_SEGUNDOS, TimeUnit.SECONDS);
        logger.info("Miembro {} guardado en caché por {} segundos", id, TTL_SEGUNDOS);

        return buildResponse(miembro, "BASE DE DATOS (~80ms)");
    }

    public Miembro crearMiembro(MiembroDTO dto) {
        Miembro miembro = new Miembro();
        miembro.setId(dto.getId() != null && !dto.getId().isEmpty()
            ? dto.getId()
            : "M" + String.format("%03d", miembroRepository.count() + 1));
        miembro.setNombre(dto.getNombre());
        miembro.setEmail(dto.getEmail());
        miembro.setTipoMiembro(dto.getTipoMiembro());
        miembro.setFechaRegistro(new Date().toString());
        miembro.setPrestamosActivos(0);

        Miembro guardado = miembroRepository.save(miembro);
        logger.info("Miembro {} creado en BD", guardado.getId());
        return guardado;
    }

    private Map<String, Object> buildResponse(Miembro miembro, String fuente) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("fuente", fuente);
        response.put("miembro", miembro);
        return response;
    }

    @SuppressWarnings("unchecked")
    private Miembro convertirAMiembro(Object obj) {
        if (obj instanceof Miembro) return (Miembro) obj;
        if (obj instanceof LinkedHashMap) {
            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) obj;
            Miembro m = new Miembro();
            m.setId((String) map.get("id"));
            m.setNombre((String) map.get("nombre"));
            m.setEmail((String) map.get("email"));
            m.setTipoMiembro((String) map.get("tipoMiembro"));
            m.setFechaRegistro((String) map.get("fechaRegistro"));
            m.setPrestamosActivos((Integer) map.get("prestamosActivos"));
            return m;
        }
        return null;
    }
}
