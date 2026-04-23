package org.gualsh.demo.springboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Конфигурация Jackson через Java Config.
 *
 * <p><b>Образовательный момент:</b>
 * В Spring Boot можно настраивать Jackson тремя способами:
 * 1. Через application.yml (самый простой).
 * 2. Через Jackson2ObjectMapperBuilder (программный).
 * 3. Созданием собственного бина ObjectMapper (полный контроль, но отключает автоконфигурацию Spring).
 * </p>
 */
@Configuration
public class JacksonConfig {

    /**
     * Кастомизация ObjectMapper без потери стандартных настроек Spring.
     *
     * @return бин настройки Jackson
     */
    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
                // Здесь можно добавить специфичные модули или настройки,
                // которые сложно выразить в YAML.
                .indentOutput(true);
    }
}
