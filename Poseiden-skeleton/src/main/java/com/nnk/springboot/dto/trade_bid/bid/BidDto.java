package com.nnk.springboot.dto.trade_bid.bid;

import com.nnk.springboot.dto.trade_bid.TradeBidDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class BidDto extends TradeBidDto {
    private BigDecimal bidQuantity;
}
