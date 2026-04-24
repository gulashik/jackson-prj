package org.gualsh.demo.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тестирование Jackson в контексте Spring Boot.
 *
 * <p><b>Образовательный момент:</b>
 * Аннотация {@code @JsonTest} позволяет протестировать сериализацию в изолированном контексте,
 * где загружаются только необходимые бины для работы с JSON.
 * {@code JacksonTester} — удобный инструмент Spring для проверки JSON.
 * </p>
 */
@JsonTest
class SpringBootJacksonTest {

    @Autowired
    private JacksonTester<DemoDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Проверка глобальных настроек Spring Boot Jackson")
    void springBootJacksonConfigTest() throws IOException {
        DemoDto dto = new DemoDto("Spring User", LocalDateTime.of(2024, 10, 23, 15, 30));

        // Проверяем, что используется snake_case из application.yml
        assertThat(json.write(dto)).hasJsonPathStringValue("@.user_name", "Spring User");
        
        // Проверяем формат даты из application.yml
        assertThat(json.write(dto)).hasJsonPathStringValue("@.created_at", "2024-10-23 15:30:00");
    }

    @Test
    @DisplayName("Проверка индентации (indent output)")
    void indentTest() throws IOException {
        DemoDto dto = new DemoDto("User", LocalDateTime.now());
        String prettyJson = objectMapper.writeValueAsString(dto);
        
        // В MacOS/Unix используется \n, в Windows \r\n
        assertThat(prettyJson).containsAnyOf("\n", "\r\n");
    }

    static record DemoDto(String userName, LocalDateTime createdAt) {}
}

