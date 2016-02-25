package ru.gov.emias2.jdbc;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author maxname
 */
public interface ObjectRequest<TResult> extends Request {
    RowMapper<TResult> getMapper();
    Class<TResult> getResultClass();
}
