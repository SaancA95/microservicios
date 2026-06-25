package com.biblioteca.catalogo.service;

import com.biblioteca.catalogo.dto.LibroDTO;
import com.biblioteca.catalogo.model.Libro;
import com.biblioteca.catalogo.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class CatalogoService {

    private static final Logger logger = LoggerFactory.getLogger(CatalogoService.class);
    private static final long TTL_SEGUNDOS = 60L;
    private static final String CACHE_PREFIX = "libro:";

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Map<String, Object> getLibroPorIsbn(String isbn) {
        String cacheKey = CACHE_PREFIX + isbn;

        // 1. Buscar en Redis
        Object cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            logger.info("CACHE HIT para ISBN: {}", isbn);
            Libro libro = convertirALibro(cached);
            return buildResponse(libro, "CACHE Redis (~2ms)");
        }

        // 2. CACHE MISS → consultar PostgreSQL vía JPA
        logger.info("CACHE MISS para ISBN: {}. Consultando BASE DE DATOS...", isbn);
        Optional<Libro> optional = libroRepository.findById(isbn);
        if (optional.isEmpty()) {
            return null;
        }

        Libro libro = optional.get();

        // 3. Guardar en Redis con TTL 60s
        redisTemplate.opsForValue().set(cacheKey, libro, TTL_SEGUNDOS, TimeUnit.SECONDS);
        logger.info("Libro {} guardado en caché por {} segundos", isbn, TTL_SEGUNDOS);

        return buildResponse(libro, "BASE DE DATOS (~80ms)");
    }

    public Libro guardarLibro(LibroDTO dto) {
        Libro libro = new Libro(
                dto.getIsbn(),
                dto.getTitulo(),
                dto.getAutor(),
                dto.getGenero(),
                dto.getAnioPublicacion(),
                dto.getDisponible(),
                dto.getCopiasTotales(),
                dto.getCopiasDisponibles()
        );
        Libro guardado = libroRepository.save(libro);
        // Invalidar caché si existía
        redisTemplate.delete(CACHE_PREFIX + guardado.getIsbn());
        logger.info("Libro {} guardado en BD y caché invalidada", guardado.getIsbn());
        return guardado;
    }

    private Map<String, Object> buildResponse(Libro libro, String fuente) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("fuente", fuente);
        response.put("libro", libro);
        return response;
    }

    @SuppressWarnings("unchecked")
    private Libro convertirALibro(Object obj) {
        if (obj instanceof Libro) return (Libro) obj;
        if (obj instanceof LinkedHashMap) {
            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) obj;
            Libro l = new Libro();
            l.setIsbn((String) map.get("isbn"));
            l.setTitulo((String) map.get("titulo"));
            l.setAutor((String) map.get("autor"));
            l.setGenero((String) map.get("genero"));
            l.setAnioPublicacion((Integer) map.get("anioPublicacion"));
            l.setDisponible((Boolean) map.get("disponible"));
            l.setCopiasTotales((Integer) map.get("copiasTotales"));
            l.setCopiasDisponibles((Integer) map.get("copiasDisponibles"));
            return l;
        }
        return null;
    }
}
