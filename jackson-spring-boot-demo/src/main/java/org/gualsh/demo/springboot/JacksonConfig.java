package org.gualsh.demo.springboot;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Конфигурация Jackson через Java Config.
 *
 * <p><b>Образовательный момент:</b>
 * В Spring Boot можно настраивать Jackson тремя способами:
 * 1. Через application.yml (самый простой).
 * 2. Через Jackson2ObjectMapperBuilder или Jackson2ObjectMapperBuilderCustomizer (программный).
 * 3. Созданием собственного бина ObjectMapper (полный контроль, но отключает автоконфигурацию Spring).
 * </p>
 */
@Configuration
public class JacksonConfig {

    /**
     * Способ А: Jackson2ObjectMapperBuilder.
     * Позволяет создать преднастроенный билдер. 
     * Spring Boot использует его для создания итогового ObjectMapper.
     * <b>Важно:</b> Этот бин ДОПОЛНЯЕТ настройки из application.yml.
     */
    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
                // Например, включаем индентацию программно
                .indentOutput(true);
    }

    /**
     * Способ Б: Jackson2ObjectMapperBuilderCustomizer (Рекомендуемый для дополнения).
     * Позволяет "подкрутить" стандартный билдер, который уже настроен Spring Boot-ом.
     * Это самый безопасный способ ДОПОЛНИТЬ конфигурацию, не переопределяя её.
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder
                // Добавляем специфичные настройки, не ломая то, что пришло из YAML
                .applicationContext(null) // пример вызова метода
                .featuresToDisable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
