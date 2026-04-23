package org.gualsh.demo.core;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Демонстрация полиморфизма в Jackson.
 *
 * Для корректной десериализации иерархии классов Jackson должен знать, какой конкретно класс создавать.
 * Для этого используется "дискриминатор" — специальное поле в JSON.
 * </p>
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Animal.Dog.class, name = "dog"),
    @JsonSubTypes.Type(value = Animal.Cat.class, name = "cat")
})
@Data
@NoArgsConstructor
public abstract class Animal {
    private String name;

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class Dog extends Animal {
        private boolean barks;
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class Cat extends Animal {
        private int lives;
    }
}
