plugins {
    java
}

dependencies {
    // Jackson Core: Базовые интерфейсы и потоковое API
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.2")
    // Jackson Annotations: Аннотации для настройки маппинга
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.2")
    // Jackson Databind: Основной модуль для работы с ObjectMapper (зависит от core и annotations)
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    // Jackson JSR310: Поддержка Java 8 Date/Time API (LocalDateTime, ZonedDateTime и т.д.)
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.2")
    // Jackson Jakarta Module: Для работы с аннотациями Jakarta (бывш. JAXB)
    implementation("com.fasterxml.jackson.module:jackson-module-jakarta-xmlbind-annotations:2.17.2")

    // Lombok: Уменьшение шаблонного кода
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    // JUnit 5 for testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
    testImplementation("org.assertj:assertj-core:3.26.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
