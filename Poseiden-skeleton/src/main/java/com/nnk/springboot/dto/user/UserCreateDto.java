package com.nnk.springboot.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserCreateDto extends UserBaseDto {
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 125, message = "Password must be between 8 and 125 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
            message = "Password must have at least 1 uppercase, 1 lowercase, 1 number, 1 special character"
    )
    private String password;
}
