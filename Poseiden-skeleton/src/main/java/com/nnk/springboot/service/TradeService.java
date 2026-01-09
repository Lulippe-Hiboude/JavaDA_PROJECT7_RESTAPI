package com.nnk.springboot.service;

import com.nnk.springboot.dto.trade_bid.trade.TradeCreateDto;
import com.nnk.springboot.dto.trade_bid.trade.TradeDto;
import com.nnk.springboot.dto.trade_bid.trade.TradeUpdateDto;

public interface TradeService extends EntityService<TradeCreateDto, TradeDto, TradeUpdateDto> {
}
