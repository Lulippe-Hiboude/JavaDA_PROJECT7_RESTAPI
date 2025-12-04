package com.nnk.springboot.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserUpdateDto extends UserBaseDto {
    private Integer id;
    @Pattern(
            regexp = "^$|^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,125}$",
            message = "Password must have at least 1 uppercase, 1 lowercase, 1 number, 1 special character and it must be between 8 and 125 characters long."
    )
    private String password;
}
