package kg.mega.lashes.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record ClientCreateDto (
        String name,
        @Pattern(
                regexp = "\\+996\\d{9}",
                message = "Дополнительный номер должен начинаться с +996 и содержать всего 13 символов (например, +996555123456)"
        )
        @Schema(description = "Дополнительный номер телефона (только в формате +996)", example = "+996")
        String phoneNumber,
        String data
){
}
