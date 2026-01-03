package com.nnk.springboot.service;

import com.nnk.springboot.dto.trade_bid.bid.BidCreateDto;
import com.nnk.springboot.dto.trade_bid.bid.BidDto;
import com.nnk.springboot.dto.trade_bid.bid.BidUpdateDto;

public interface BidService extends EntityService<BidCreateDto, BidDto, BidUpdateDto> {
}
