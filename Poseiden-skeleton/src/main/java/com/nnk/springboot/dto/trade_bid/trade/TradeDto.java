package com.nnk.springboot.dto.trade_bid.trade;

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
public class TradeDto extends TradeBidDto {
    private BigDecimal buyQuantity;
}
