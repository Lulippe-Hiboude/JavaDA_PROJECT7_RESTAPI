package com.nnk.springboot.dto.trade_bid.bid;

import com.nnk.springboot.dto.trade_bid.TradeBidBaseDto;
import jakarta.validation.constraints.Digits;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class BidBaseDto extends TradeBidBaseDto {
    @Digits(integer = 10, fraction = 2, message = "The bid quantity must have at most 10 digits and 2 decimals ")
    private BigDecimal bidQuantity;
}
