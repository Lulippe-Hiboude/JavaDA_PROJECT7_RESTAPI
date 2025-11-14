package com.nnk.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.sql.Timestamp;

@Entity
@Table(name = "rating")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {
    // TODO: Map columns in data table RATING with corresponding java fields
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "moodysRating", length = 125)
    @NotBlank(message = "Moodys Rating is mandatory")
    @Length(max = 125, message = "Moodys Rating cannot be longer than 125 characters" )
    private String moodysRating;

    @Column(name = "sandPRating", length = 125)
    @NotBlank(message = "S&P Rating is mandatory")
    @Length(max = 125, message = "SP Rating cannot be longer than 125 characters" )
    private String sandPRating;

    @Column(name = "fitchRating", length = 125)
    @NotBlank(message = "Fitch Rating is mandatory")
    @Length(max = 125, message = "Fitch Rating cannot be longer than 125 characters" )
    private String fitchRating;

    @Column(name = "orderNumber")
    @NotNull(message = "Order number is mandatory")
    private Integer orderNumber;
}
