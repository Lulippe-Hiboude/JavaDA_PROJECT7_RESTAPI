package com.nnk.springboot.mapper;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.trade_bid.trade.TradeCreateDto;
import com.nnk.springboot.dto.trade_bid.trade.TradeDto;
import com.nnk.springboot.dto.trade_bid.trade.TradeUpdateDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TradeMapper extends BaseMapper<Trade, TradeCreateDto, TradeDto, TradeUpdateDto> {

    @Mapping(target = "id", ignore = true)
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "account", source = "account")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "buyQuantity", source = "buyQuantity")
    @Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
    Trade toEntity(final TradeCreateDto tradeCreateDto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "account", source = "account")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "buyQuantity", source = "buyQuantity")
    @Mapping(target = "revisionDate", expression = "java(java.time.LocalDateTime.now())")
    Trade toEntity(final TradeUpdateDto tradeUpdateDto, @MappingTarget final Trade trade);
}
