package com.nnk.springboot.dto.curve;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CurvePointBaseDto {
    @NotNull(message = "CurveId is mandatory")
    @Positive(message = "Must be a positive number")
    @Max(value = 255, message = "Must not exceed 255")
    private Integer curveId;

    @NotNull(message = "Term is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message="term must be > 0")
    @DecimalMax(value = "100.0", message = "The term must not exceed 100")
    @Digits(integer = 3, fraction = 2, message = "The term must have at most 3 digits and 2 decimals ")
    private Double term;

    @NotNull(message = "Value is required")
    @DecimalMin(value = "0.01", message = "Must be at least 0.01 %")
    @DecimalMax(value = "100.0", message = "The value must not exceed 100.0 %")
    private Double value;
}
