package com.nnk.springboot.dto.curve;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CurvePointUpdateDto extends CurvePointBaseDto {
    private Integer id;
}
