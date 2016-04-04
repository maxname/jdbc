package ru.gov.emias2.jdbc;

/**
 * Created by Zinoviev Oleg on 04.04.2016.
 */
public interface TypeConverter<TPrimitiveType> {
    TPrimitiveType convert(Object value);
}
