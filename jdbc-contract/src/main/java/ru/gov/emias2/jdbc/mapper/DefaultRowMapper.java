package ru.gov.emias2.jdbc.mapper;


import org.springframework.beans.BeanWrapper;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author mkomlev
 */
public class DefaultRowMapper<T> extends BeanPropertyRowMapper<T> {
    private final static ConversionService CONVERSION_SERVICE = conversionService();

    private DefaultRowMapper(Class<T> mappedClass) {
        super(mappedClass);
    }

    public static <T> DefaultRowMapper<T> newInstance(Class<T> mappedClass) {
        return new DefaultRowMapper<T>(mappedClass);
    }

    @Override
    protected void initBeanWrapper(BeanWrapper bw) {
        bw.setConversionService(CONVERSION_SERVICE);
    }

    private static ConversionService conversionService() {
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new Converter<java.sql.Date, LocalDate>() {
            public LocalDate convert(java.sql.Date date) {
                return toLocalDate(date);
            }
        });
        conversionService.addConverter(new Converter<java.sql.Time, LocalTime>() {
            public LocalTime convert(java.sql.Time time) {
                return toLocalTime(time);
            }
        });
        conversionService.addConverter(new Converter<java.sql.Timestamp, LocalDateTime>() {
            public LocalDateTime convert(java.sql.Timestamp time) {
                return toLocalDateTime(time);
            }
        });
        conversionService.addConverter(new Converter<java.sql.Timestamp, LocalDate>() {
            public LocalDate convert(java.sql.Timestamp time) {
                return toLocalDate(time);
            }
        });
        return conversionService;
    }

    private static LocalDate toLocalDate(java.sql.Date sqlDate) {
        if (sqlDate == null) {
            return null;
        } else {
            return sqlDate.toLocalDate();
        }
    }

    private static LocalDate toLocalDate(java.sql.Timestamp sqlDate) {
        if (sqlDate == null) {
            return null;
        } else {
            return sqlDate.toLocalDateTime().toLocalDate();
        }
    }

    private static LocalTime toLocalTime(java.sql.Time sqlTime) {
        if (sqlTime == null) {
            return null;
        } else {
            return sqlTime.toLocalTime();
        }
    }

    private static LocalDateTime toLocalDateTime(java.sql.Timestamp sqlDateTime) {
        if (sqlDateTime == null) {
            return null;
        } else {
            return sqlDateTime.toLocalDateTime();
        }
    }
}
