        package kg.mega.lashes.models.dtos;

        import com.fasterxml.jackson.annotation.JsonProperty;
        import io.swagger.v3.oas.annotations.media.Schema;
        import jakarta.validation.constraints.NotBlank;
        import jakarta.validation.constraints.NotNull;
        import jakarta.validation.constraints.Pattern;

        import java.time.LocalDate;
        import java.time.LocalTime;

        public record ClientUpdateDto(
                String name,
                @Pattern(
                        regexp = "\\+996\\d{9}",
                        message = "Дополнительный номер должен начинаться с +996 и содержать всего 13 символов (например, +996555123456)"
                )
                @Schema(description = "Дополнительный номер телефона (только в формате +996)", example = "+996")
                String phoneNumber,
                LocalDate visitDate,
                LocalTime visitTime,
                @JsonProperty("data")
                 String comment
        ) {
        }
