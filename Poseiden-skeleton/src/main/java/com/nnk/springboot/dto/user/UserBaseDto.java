package com.nnk.springboot.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserBaseDto {
    @NotBlank(message = "Username is required")
    @Size(max = 125, message = "Username must be max 125 characters")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ0-9 -]+$", message = "Username has invalid characters")
    private String username;

    @NotBlank(message = "Fullname is required")
    @Size(max = 125, message = "Fullname must be max 125 characters")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ0-9 -]+$", message = "Fullname has invalid characters")
    private String fullname;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(USER|ADMIN)$", message = "Role must be USER or ADMIN")
    private String role;
}
