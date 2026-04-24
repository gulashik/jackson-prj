# Jackson Core Demo

Данный подпроект демонстрирует базовые возможности библиотеки Jackson для работы с JSON в Java. Здесь собраны примеры конфигурации, аннотаций, полиморфизма и кастомной сериализации.

## Основные настройки и конфигурация

Центральным объектом является `ObjectMapper`. Его правильная настройка критична для корректной работы приложения.

### Регистрация модулей
Для поддержки современных типов данных (например, Java 8 Date/Time API) необходимо регистрировать соответствующие модули:
```java
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new JavaTimeModule());
```

### Популярные настройки (Features)
В Jackson существует три основных типа настроек: `SerializationFeature`, `DeserializationFeature` и `JsonParser.Feature`/`JsonGenerator.Feature`.

Примеры часто используемых настроек:
```java
// Отключаем запись дат в виде чисел (timestamp)
mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

// Не падать, если в JSON есть поля, которых нет в Java-классе
mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

// Включать в JSON только не-пустые (non-null) поля
mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
```

## Best Practices

1.  **Переиспользование ObjectMapper**: Создание экземпляра `ObjectMapper` — ресурсозатратная операция. Рекомендуется использовать один экземпляр на все приложение (в Spring это делается автоматически через Bean). `ObjectMapper` потокобезопасен после настройки.
2.  **Использование snake_case**: Если API использует `snake_case`, вместо ручного переименования каждого поля через `@JsonProperty`, можно настроить стратегию именования глобально:
    ```java
    mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    ```
3.  **Неизменяемые объекты (Immutables)**: Jackson отлично работает с библиотекой Lombok и конструкторами. Рекомендуется делать DTO неизменяемыми (`@Value` в Lombok).

## Подводные камни

1.  **Неизвестные поля**: По умолчанию Jackson выбрасывает исключение `UnrecognizedPropertyException`, если встречает в JSON поле, которого нет в классе. 
    *   *Решение*: Использовать `DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES = false` или аннотацию `@JsonIgnoreProperties(ignoreUnknown = true)` над классом.
2.  **Формат дат**: Стандартный формат даты может не соответствовать вашим требованиям или требованиям фронтенда.
    *   *Решение*: Использовать `@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")` над полями.
3.  **Полиморфизм**: При десериализации интерфейсов или абстрактных классов Jackson не знает, какой тип создать.
    *   *Решение*: Использовать аннотации `@JsonTypeInfo` и `@JsonSubTypes` (см. пример в `Animal.java`).
4.  **Тип данных Double/BigDecimal**: При работе с деньгами `Double` может терять точность. 
    *   *Решение*: Использовать `BigDecimal` и `DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS`.

## Примеры в коде

*   `SimpleUser.java` — базовая разметка аннотациями (`@JsonProperty`, `@JsonFormat`).
*   `Animal.java` — настройка полиморфной десериализации через поле-дискриминатор `type`.
*   `CustomAccount.java` — пример написания и подключения кастомного `JsonSerializer` и `JsonDeserializer`.
*   `JacksonCoreTest.java` — модульные тесты, демонстрирующие все вышеперечисленное в действии.
