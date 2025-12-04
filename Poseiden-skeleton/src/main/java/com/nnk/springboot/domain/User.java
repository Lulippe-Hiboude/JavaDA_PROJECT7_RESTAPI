package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @NotBlank(message = "Username is mandatory")
    @Column(name = "username", nullable = false, unique = true, length = 125)
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Column(name = "password", nullable = false, length = 125)
    private String password;

    @NotBlank(message = "FullName is mandatory")
    @Column(name = "fullname", nullable = false, length = 125)
    private String fullname;

    @NotBlank(message = "Role is mandatory")
    @Column(name = "role", nullable = false, length = 125)
    private String role;

}
