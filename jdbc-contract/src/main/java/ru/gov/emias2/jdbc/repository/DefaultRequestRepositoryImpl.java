package ru.gov.emias2.jdbc.repository;

import org.springframework.dao.EmptyResultDataAccessException;
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
        try {
            return jdbcTemplate.queryForObject(request.getQuery(), processParameters(request.getParameters()), request.getMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional
    @Override
    public <TPrimitiveType> TPrimitiveType getValue(PrimitiveRequest<TPrimitiveType> request) {
        try {
            return jdbcTemplate.queryForObject(request.getQuery(), processParameters(request.getParameters()), request.getResultClass());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional
    @Override
    public <TIdentityType> TIdentityType insert(InsertRequest<TIdentityType> request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                request.getQuery(),
                new MapSqlParameterSource(processParameters(request.getParameters())),
                keyHolder,
                new String[] { request.getKeyColumn() });
        Object keyValue = keyHolder.getKeys().get(request.getKeyColumn());
        if (request.getKeyConverter() != null) {
            return request.getKeyConverter().convert(keyValue);
        }
        return request.getResultClass().cast(processValueBack(keyValue));
    }

    @Transactional
    @Override
    public int exec(VoidRequest request) {
        return jdbcTemplate.update(request.getQuery(), processParameters(request.getParameters()));
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

    private Object processValueBack(Object src) {
        if (src instanceof java.sql.Date) {
            return ((java.sql.Date)src).toLocalDate();
        }

        if (src instanceof java.sql.Time) {
            return ((java.sql.Time)src).toLocalTime();
        }

        if (src instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp)src).toLocalDateTime();
        }

        return src;
    }
}
