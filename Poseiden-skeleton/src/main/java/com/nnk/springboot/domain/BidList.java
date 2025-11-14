package com.nnk.springboot.domain;

import jakarta.persistence.*;
//TODO deprecated since spring 5
// import org.springframework.beans.factory.annotation.Required;
// remove this import and prefer constructor injection over setter injection

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bidlist")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BidList extends AbstractTradeBid{
    // TODO: Map columns in data table BIDLIST with corresponding java fields

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "BidListId")
    private Integer bidListId;

    @Column(name = "bidQuantity")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal bidQuantity;

    @Column(name = "askQuantity")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal askQuantity;

    @Column(name = "bid")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal bid;

    @Column(name = "ask")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal ask;

    @Column(name = "bidListDate")
    private LocalDateTime bidListDate;

    @Column(name = "commentary")
    private String commentary;

}
