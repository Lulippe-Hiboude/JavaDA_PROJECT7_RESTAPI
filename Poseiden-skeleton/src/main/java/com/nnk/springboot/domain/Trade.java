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
@Table(name = "trade")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Trade extends AbstractTradeBid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "buy_quantity", nullable = false)
    @Digits(integer = 10, fraction = 2)
    private BigDecimal buyQuantity;

    @Column(name = "sell_quantity")
    private BigDecimal sellQuantity;

    @Column(name = "buy_price")
    private BigDecimal buyPrice;

    @Column(name = "sell_price")
    private BigDecimal sellPrice;

    @Column(name = "trade_date")
    private LocalDateTime tradeDate;
}
