package com.nnk.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "curvepoint")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurvePoint {
    // TODO: Map columns in data table CURVEPOINT with corresponding java fields

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "CurveId")
    @NotNull(message = "CurveId is mandatory")
    private Integer curveId;

    @Column(name = "asOfDate")
    private LocalDateTime asOfDate;

    @Column(name = "term")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal term;

    @Column(name = "value")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal value;
}
