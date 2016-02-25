package ru.gov.emias2.jdbc;

import java.util.Map;

/**
 * @author maxname
 */
public interface Request {
    String getQuery();
    Map<String, Object> getParameters();
}
