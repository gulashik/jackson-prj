plugins {
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    java
}

dependencies {
    // Spring Boot Starter Web включает Jackson по умолчанию
    implementation("org.springframework.boot:spring-boot-starter-web")
    // Starter JSON включает jackson-databind, jackson-datatype-jdk8, jackson-datatype-jsr310, jackson-module-parameter-names
    implementation("org.springframework.boot:spring-boot-starter-json")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
