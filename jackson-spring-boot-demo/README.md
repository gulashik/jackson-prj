# Jackson Spring Boot Demo

Данный подпроект демонстрирует способы интеграции и настройки библиотеки Jackson в приложении на Spring Boot.

## Способы конфигурации

Spring Boot предоставляет три основных уровня настройки Jackson, от простого к сложному:

### 1. Настройка через `application.yml` (рекомендуется)
Самый простой способ изменить глобальное поведение ObjectMapper. Все настройки находятся в секции `spring.jackson`.

Примеры в данном проекте (`src/main/resources/application.yml`):
* `property-naming-strategy: SNAKE_CASE` — превращает `userName` в `user_name`.
* `date-format: yyyy-MM-dd HH:mm:ss` — задает глобальный формат даты.
* `serialization.indent-output: true` — включает "красивую" печать JSON.
* `default-property-inclusion: non_null` — исключает поля с `null` из итогового JSON.

### 2. Использование `Jackson2ObjectMapperBuilderCustomizer` или `Jackson2ObjectMapperBuilder`
Если настроек в YAML недостаточно (например, нужно зарегистрировать специфичный модуль или добавить сложную логику), используйте Java-конфигурацию.

**Особенность:** Эти подходы **ДОПОЛНЯЮТ** стандартную конфигурацию Spring Boot, а не заменяют её полностью. Это значит, что все ваши настройки из `application.yml` продолжат работать.

#### Пример А: `Jackson2ObjectMapperBuilder`
Вы объявляете бин билдера, и Spring Boot использует его как основу для создания `ObjectMapper`.
```java
@Bean
public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
    return new Jackson2ObjectMapperBuilder()
            .indentOutput(true)
            .modulesToInstall(new MyCustomModule());
}
```

#### Пример Б: `Jackson2ObjectMapperBuilderCustomizer` (Рекомендуется)
Позволяет изменить (`customize`) уже существующий билдер, который Spring Boot подготовил на основе `application.yml`.
```java
@Bean
public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return builder -> builder
            .serializationInclusion(JsonInclude.Include.NON_EMPTY)
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
}
```

### 3. Создание собственного бина `ObjectMapper`
**Самый радикальный способ.** Если вы объявите `@Bean public ObjectMapper objectMapper()`, автоконфигурация Spring Boot полностью отключится. Вы получите полный контроль, но потеряете все настройки из `application.yml` и стандартные модули (например, для работы с Java 8 Time), если не настроите их вручную.

---

## Тестирование
Для проверки корректности настроек сериализации в Spring Boot рекомендуется использовать аннотацию `@JsonTest` совместно с `JacksonTester`.

Пример в `SpringBootJacksonTest.java`:
* `@JsonTest` инициализирует только те компоненты Spring, которые нужны для работы с JSON.
* `JacksonTester` предоставляет удобный API для проверки структуры JSON и значений полей (JSONPath).

---

## Best Practices
1. **Предпочитайте YAML настройки.** Они наглядны и их легко менять без пересборки кода.
2. **Используйте `Jackson2ObjectMapperBuilder`.** Это позволяет сохранить "магию" Spring Boot, добавляя свои нюансы.
3. **Не переопределяйте `ObjectMapper` без крайней необходимости.** Это часто приводит к тому, что в одном месте приложения JSON выглядит иначе, чем в другом.
4. **DTO вместо Entity.** Никогда не вешайте аннотации Jackson прямо на сущности базы данных (JPA/Hibernate). Используйте промежуточные DTO.
5. **Модуль Java 8 Time.** Всегда проверяйте наличие `com.fasterxml.jackson.datatype:jackson-datatype-jsr310` в зависимостях (в Spring Boot он идет "из коробки").

---

## Подводные камни
* **Потеря настроек:** Как упоминалось выше, создание своего `@Bean ObjectMapper` отключает `application.yml`.
* **Форматирование дат:** Если вы используете `@JsonFormat` на конкретном поле, оно перекроет глобальные настройки из `application.yml`.
* **Кэширование:** Spring MVC кэширует некоторые настройки конвертеров. При динамической смене бинов настройки могут примениться не сразу (в рантайме).
* **Snake Case и @JsonProperty:** Если вы используете глобальный `SNAKE_CASE`, но вручную указали `@JsonProperty("userName")`, Jackson приоритетно возьмет имя из аннотации.
* **LocalDateTime без модуля:** Без модуля `jackson-datatype-jsr310` попытка сериализовать `LocalDateTime` приведет к ошибке или записи в виде массива чисел.
