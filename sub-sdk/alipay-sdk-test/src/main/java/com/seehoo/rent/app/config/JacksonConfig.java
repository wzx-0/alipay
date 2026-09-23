package com.seehoo.rent.app.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Jackson序列化配置：LocalDateTime统一yyyy-MM-dd HH:mm:ss，Long转String防止前端精度丢失（雪花ID）
 */
@Configuration
public class JacksonConfig {

    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FMT))
                .serializerByType(Long.class, new ToStringSerializer())
                .serializerByType(Long.TYPE, new ToStringSerializer());
    }
}
