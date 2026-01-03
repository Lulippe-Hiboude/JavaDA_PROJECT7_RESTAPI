package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bid")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Bid extends AbstractTradeBid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "bid_quantity", nullable = false)
    @Digits(integer = 10, fraction = 2)
    private BigDecimal bidQuantity;

    @Column(name = "ask_quantity")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal askQuantity;

    @Column(name = "bid")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal bid;

    @Column(name = "ask")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal ask;

    @Column(name = "bid_date")
    private LocalDateTime bidDate;

    @Column(name = "commentary")
    private String commentary;

}
