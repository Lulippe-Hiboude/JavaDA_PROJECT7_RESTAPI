package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "curve_point")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurvePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "curve_id")
    @NotNull(message = "CurveId is mandatory")
    private Integer curveId;

    @Column(name = "as_of_date")
    @NotNull(message = "asOfDate is mandatory")
    @PastOrPresent(message = "asOfDate cannot be in the future")
    private LocalDateTime asOfDate;

    @Column(name = "term")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal term;

    @Column(name = "value")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal value;

    @Column(name = "creation_date", updatable = false)
    @NotNull(message = "creation date is mandatory")
    @CreationTimestamp
    private LocalDateTime creationDate;
}
