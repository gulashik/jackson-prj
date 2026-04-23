package org.gualsh.demo.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Базовая модель для демонстрации простых возможностей Jackson.
 *
 * <p><b>Образовательный момент:</b>
 * По умолчанию Jackson использует геттеры/сеттеры для доступа к полям.
 * Аннотации позволяют переопределить имена полей в JSON и форматировать типы данных.
 * </p>
 *
 * <pre>{@code
 * SimpleUser user = new SimpleUser("Ivan", "Ivanov", LocalDateTime.now());
 * String json = objectMapper.writeValueAsString(user);
 * }</pre>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleUser {

    /**
     * Переименование поля в JSON. Полезно для соблюдения snake_case в API.
     */
    @JsonProperty("first_name")
    private String firstName;

    /**
     * Обычное поле, будет сериализовано как "lastName".
     */
    private String lastName;

    /**
     * Настройка формата даты. Без модуля jsr310 и @JsonFormat LocalDateTime может упасть или
     * превратиться в массив чисел [2024, 10, 23, ...].
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
