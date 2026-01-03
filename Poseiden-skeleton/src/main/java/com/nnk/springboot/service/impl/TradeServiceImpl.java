package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.trade_bid.trade.TradeCreateDto;
import com.nnk.springboot.dto.trade_bid.trade.TradeDto;
import com.nnk.springboot.dto.trade_bid.trade.TradeUpdateDto;
import com.nnk.springboot.mapper.TradeMapper;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.service.AbstractEntityService;
import com.nnk.springboot.service.TradeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
public class TradeServiceImpl extends AbstractEntityService<Trade, TradeCreateDto, TradeDto, TradeUpdateDto>
        implements TradeService {

    public TradeServiceImpl(final TradeRepository repository, final TradeMapper mapper) {

        super(repository, mapper);
    }

    @Override
    protected void checkEntityValidity(final TradeUpdateDto entityUpdateDto) {
    }

    @Override
    protected Integer getEntityId(final TradeUpdateDto tradeUpdateDto) {
        return tradeUpdateDto.getId();
    }

    @Override
    protected void handleError(final Integer id) {
        throw new EntityNotFoundException("The trade with id " + id + " was not found");
    }
}
