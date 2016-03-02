package ru.gov.emias2.jdbc.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.gov.emias2.jdbc.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mkomlev
 */
@Repository
public class DefaultRequestRepositoryImpl implements RequestRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DefaultRequestRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public <TResult> List<TResult> getList(CollectionRequest<TResult> request) {
        return jdbcTemplate.query(request.getQuery(), processParameters(request.getParameters()), request.getMapper());
    }

    @Transactional
    @Override
    public <TResult> TResult getOne(ObjectRequest<TResult> request) {
        return jdbcTemplate.queryForObject(request.getQuery(), processParameters(request.getParameters()), request.getMapper());
    }

    @Transactional
    @Override
    public <TPrimitiveType> TPrimitiveType getValue(PrimitiveRequest<TPrimitiveType> request) {
        return jdbcTemplate.queryForObject(request.getQuery(), processParameters(request.getParameters()), request.getResultClass());
    }

    @Transactional
    @Override
    public <TIdentityType> TIdentityType insert(InsertRequest<TIdentityType> request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(request.getQuery(), new MapSqlParameterSource(processParameters(request.getParameters())), keyHolder);
        return request.getKeyValue(keyHolder.getKey());
    }

    @Transactional
    @Override
    public void exec(VoidRequest request) {
        jdbcTemplate.update(request.getQuery(), processParameters(request.getParameters()));
    }

    private Map<String, Object> processParameters(Map<String, Object> parameters) {
        Map<String, Object> result = new HashMap<>(parameters.size());
        for(Map.Entry<String, Object> kv : parameters.entrySet()) {
            result.put(kv.getKey(), processValue(kv.getValue()));
        }
        return result;
    }

    private Object processValue(Object src) {
        if (src instanceof LocalDate) {
            return java.sql.Date.valueOf((LocalDate)src);
        }

        if (src instanceof LocalTime) {
            return java.sql.Time.valueOf((LocalTime)src);
        }

        if (src instanceof LocalDateTime) {
            return java.sql.Timestamp.valueOf((LocalDateTime)src);
        }

        return src;
    }
}
