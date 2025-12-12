package com.nnk.springboot.dto.curve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurvePointDto {
    private Integer id;
    private Integer curveId;
    private BigDecimal term;
    private BigDecimal value;
}
