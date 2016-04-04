package ru.gov.emias2.jdbc;

/**
 * @author maxname
 */
public interface InsertRequest<TPrimitiveType> extends PrimitiveRequest<TPrimitiveType> {
    String getKeyColumn();
    TypeConverter<TPrimitiveType> getKeyConverter();
}
