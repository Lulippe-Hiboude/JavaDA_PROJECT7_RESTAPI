package com.nnk.springboot.dto.trade_bid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TradeBidDto {
    private int id;
    private String account;
    private String type;
}
