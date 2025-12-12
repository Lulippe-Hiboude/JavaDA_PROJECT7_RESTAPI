package com.nnk.springboot.dto.rating;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RatingBaseDto {
    @NotBlank(message = "Moodys Rating is mandatory")
    @Length(max = 125, message = "Moodys Rating cannot be longer than 125 characters")
    private String moodysRating;

    @NotBlank(message = "S&P Rating is mandatory")
    @Length(max = 125, message = "SP Rating cannot be longer than 125 characters")
    private String sandPRating;

    @NotBlank(message = "Fitch Rating is mandatory")
    @Length(max = 125, message = "Fitch Rating cannot be longer than 125 characters")
    private String fitchRating;

    @NotNull(message = "Order number is mandatory")
    @Positive(message = "should be a positive number")
    private Integer orderNumber;
}
