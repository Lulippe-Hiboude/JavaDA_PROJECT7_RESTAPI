package com.nnk.springboot.domain;

import jakarta.persistence.*;
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
    // TODO: Map columns in data table TRADE with corresponding java fields

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TradeId")
    private Integer tradeId;

    @Column(name = "buyQuantity")
    private BigDecimal buyQuantity;

    @Column(name = "sellQuantity")
    private BigDecimal sellQuantity;

    @Column(name = "buyPrice")
    private BigDecimal buyPrice;

    @Column(name = "sellPrice")
    private BigDecimal sellPrice;

    @Column(name = "tradeDate")
    private LocalDateTime tradeDate;
}
