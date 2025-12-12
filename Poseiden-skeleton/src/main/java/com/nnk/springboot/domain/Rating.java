package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "rating")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "moodys_rating", length = 125)
    @NotBlank(message = "Moodys Rating is mandatory")
    @Length(max = 125, message = "Moodys Rating cannot be longer than 125 characters")
    private String moodysRating;

    @Column(name = "sand_p_rating", length = 125)
    @NotBlank(message = "S&P Rating is mandatory")
    @Length(max = 125, message = "SP Rating cannot be longer than 125 characters")
    private String sandPRating;

    @Column(name = "fitch_rating", length = 125)
    @NotBlank(message = "Fitch Rating is mandatory")
    @Length(max = 125, message = "Fitch Rating cannot be longer than 125 characters")
    private String fitchRating;

    @Column(name = "order_number")
    @NotNull(message = "Order number is mandatory")
    private Integer orderNumber;
}
