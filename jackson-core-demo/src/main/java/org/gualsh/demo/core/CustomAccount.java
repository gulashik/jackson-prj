package org.gualsh.demo.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * Пример кастомной сериализации.
 *
 * Когда стандартного маппинга недостаточно (например, нужно объединить поля или изменить структуру),
 * используются кастомные сериализаторы и десериализаторы.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomAccount {

    private String accountNumber;

    @JsonSerialize(using = CurrencySerializer.class)
    @JsonDeserialize(using = CurrencyDeserializer.class)
    private Double balance;

    public static class CurrencySerializer extends JsonSerializer<Double> {
        @Override
        public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(String.format("%.2f USD", value));
        }
    }

    public static class CurrencyDeserializer extends JsonDeserializer<Double> {
        @Override
        public Double deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String text = p.getText().replace(" USD", "").replace(",", ".");
            return Double.valueOf(text);
        }
    }
}
