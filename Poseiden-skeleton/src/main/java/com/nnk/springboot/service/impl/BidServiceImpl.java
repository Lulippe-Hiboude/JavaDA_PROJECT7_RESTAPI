package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.Bid;
import com.nnk.springboot.dto.trade_bid.bid.BidCreateDto;
import com.nnk.springboot.dto.trade_bid.bid.BidDto;
import com.nnk.springboot.dto.trade_bid.bid.BidUpdateDto;
import com.nnk.springboot.mapper.BidMapper;
import com.nnk.springboot.repositories.BidRepository;
import com.nnk.springboot.service.AbstractEntityService;
import com.nnk.springboot.service.BidService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
public class BidServiceImpl extends AbstractEntityService<Bid, BidCreateDto, BidDto, BidUpdateDto>
        implements BidService {

    public BidServiceImpl(final BidRepository repository, final BidMapper mapper) {
        super(repository, mapper);
    }

    @Override
    protected void checkEntityValidity(final BidUpdateDto bidUpdateDto) {
    }

    @Override
    protected Integer getEntityId(final BidUpdateDto bidUpdateDto) {
        return bidUpdateDto.getId();
    }

    @Override
    protected void handleError(final Integer id) {
        throw new EntityNotFoundException("The bid with id " + id + " was not found");
    }
}
