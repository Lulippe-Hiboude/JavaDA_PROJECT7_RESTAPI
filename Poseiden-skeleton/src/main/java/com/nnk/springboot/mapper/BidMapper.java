package com.nnk.springboot.mapper;

import com.nnk.springboot.domain.Bid;
import com.nnk.springboot.dto.trade_bid.bid.BidCreateDto;
import com.nnk.springboot.dto.trade_bid.bid.BidDto;
import com.nnk.springboot.dto.trade_bid.bid.BidUpdateDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BidMapper extends BaseMapper<Bid, BidCreateDto, BidDto, BidUpdateDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", source = "account")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "bidQuantity", source = "bidQuantity")
    @Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
    @BeanMapping(ignoreByDefault = true)
    Bid toEntity(final BidCreateDto bidCreateDto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", source = "account")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "bidQuantity", source = "bidQuantity")
    @Mapping(target = "revisionDate", expression = "java(java.time.LocalDateTime.now())")
    @BeanMapping(ignoreByDefault = true)
    Bid toEntity(final BidUpdateDto bidUpdateDto, @MappingTarget final Bid bid);
}
