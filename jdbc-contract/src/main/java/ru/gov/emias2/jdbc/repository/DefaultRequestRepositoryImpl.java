package ru.gov.emias2.jdbc.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.gov.emias2.jdbc.*;

import java.util.List;

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
        return jdbcTemplate.query(request.getQuery(), request.getParameters(), request.getMapper());
    }

    @Transactional
    @Override
    public <TResult> TResult getOne(ObjectRequest<TResult> request) {
        return jdbcTemplate.queryForObject(request.getQuery(), request.getParameters(), request.getMapper());
    }

    @Transactional
    @Override
    public <TPrimitiveType> TPrimitiveType getValue(PrimitiveRequest<TPrimitiveType> request) {
        return jdbcTemplate.queryForObject(request.getQuery(), request.getParameters(), request.getResultClass());
    }

    @Transactional
    @Override
    public <TIdentityType> TIdentityType insert(InsertRequest<TIdentityType> request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(request.getQuery(), new MapSqlParameterSource(request.getParameters()), keyHolder);
        return request.getKeyValue(keyHolder.getKey());
    }

    @Transactional
    @Override
    public void exec(VoidRequest request) {
        jdbcTemplate.update(request.getQuery(), request.getParameters());
    }
}
