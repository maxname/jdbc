package ru.gov.emias2.jdbc;

/**
 * @author maxname
 */
public interface PrimitiveRequest<TPrimitiveType> extends Request {
    Class<TPrimitiveType> getResultClass();
}
