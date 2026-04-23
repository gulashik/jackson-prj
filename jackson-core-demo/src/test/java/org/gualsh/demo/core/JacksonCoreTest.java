package org.gualsh.demo.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тесты для демонстрации работы Jackson Core.
 *
 * <p><b>Образовательный момент:</b>
 * ObjectMapper — это "сердце" Jackson. Его конфигурация определяет поведение всего процесса
 * сериализации и десериализации. Правильная настройка модулей (например, JavaTimeModule)
 * критична для работы с современными типами Java.
 * </p>
 */
class JacksonCoreTest {

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        // Регистрация модуля для поддержки Java 8 Date/Time
        mapper.registerModule(new JavaTimeModule());
        // Отключаем запись дат в виде timestamp (чисел) для наглядности
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Не падать при наличии неизвестных полей в JSON
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    @DisplayName("Базовая сериализация и десериализация")
    void simpleMappingTest() throws JsonProcessingException {
        SimpleUser user = SimpleUser.builder()
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.of(2024, 10, 23, 10, 0))
                .build();

        String json = mapper.writeValueAsString(user);
        
        assertThat(json).contains("\"first_name\":\"John\"");
        assertThat(json).contains("\"createdAt\":\"2024-10-23 10:00:00\"");

        SimpleUser deserialized = mapper.readValue(json, SimpleUser.class);
        assertThat(deserialized).isEqualTo(user);
    }

    @Test
    @DisplayName("Демонстрация полиморфизма (Animal)")
    void polymorphismTest() throws JsonProcessingException {
        Animal.Dog dog = new Animal.Dog();
        dog.setName("Buddy");
        dog.setBarks(true);

        String json = mapper.writeValueAsString(dog);
        assertThat(json).contains("\"type\":\"dog\"");

        Animal deserialized = mapper.readValue(json, Animal.class);
        assertThat(deserialized).isInstanceOf(Animal.Dog.class);
        assertThat(((Animal.Dog) deserialized).isBarks()).isTrue();
    }

    @Test
    @DisplayName("Подводный камень: неизвестные поля")
    void unknownPropertiesTest() throws JsonProcessingException {
        String json = "{\"first_name\":\"Ivan\", \"extra_field\":\"hidden\"}";
        
        // По умолчанию Jackson падает на неизвестных полях, если не настроен
        ObjectMapper strictMapper = new ObjectMapper();
        assertThrows(JsonProcessingException.class, () -> strictMapper.readValue(json, SimpleUser.class));
        
        // Наш сконфигурированный mapper проигнорирует лишнее поле
        SimpleUser user = mapper.readValue(json, SimpleUser.class);
        assertThat(user.getFirstName()).isEqualTo("Ivan");
    }

    @Test
    @DisplayName("Кастомный сериализатор и десериализатор")
    void customSerializationTest() throws JsonProcessingException {
        CustomAccount account = new CustomAccount("ACC123", 1500.50);
        
        String json = mapper.writeValueAsString(account);
        assertThat(json).contains("\"balance\":\"1500.50 USD\"");

        CustomAccount deserialized = mapper.readValue(json, CustomAccount.class);
        assertThat(deserialized.getBalance()).isEqualTo(1500.50);
    }
}
