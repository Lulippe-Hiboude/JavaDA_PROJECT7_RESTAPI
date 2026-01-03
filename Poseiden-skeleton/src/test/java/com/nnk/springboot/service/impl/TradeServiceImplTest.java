package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.trade_bid.trade.TradeCreateDto;
import com.nnk.springboot.dto.trade_bid.trade.TradeDto;
import com.nnk.springboot.dto.trade_bid.trade.TradeUpdateDto;
import com.nnk.springboot.mapper.TradeMapper;
import com.nnk.springboot.repositories.TradeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TradeServiceImplTest {

    private TradeServiceImpl tradeServiceImpl;

    @Mock
    private TradeRepository tradeRepository;

    @Captor
    private ArgumentCaptor<Trade> tradeArgumentCaptor;

    @BeforeEach
    void setUp() {
        final TradeMapper tradeMapper = Mappers.getMapper(TradeMapper.class);
        tradeServiceImpl = new TradeServiceImpl(tradeRepository, tradeMapper);
    }

    @Test
    @DisplayName("Should create a new Trade")
    void shouldCreateNewTrade() {
        //given
        final BigDecimal buyQuantity = BigDecimal.valueOf(10.05);
        final String account = "account";
        final String type = "type";
        final TradeCreateDto tradeCreateDto = TradeCreateDto.builder()
                .buyQuantity(buyQuantity)
                .account(account)
                .type(type)
                .build();

        //when
        tradeServiceImpl.handleEntityCreation(tradeCreateDto);

        //then
        verify(tradeRepository, times(1)).save(tradeArgumentCaptor.capture());
        final Trade trade = tradeArgumentCaptor.getValue();
        assertEquals(account, trade.getAccount());
        assertEquals(type, trade.getType());
        assertEquals(buyQuantity, trade.getBuyQuantity());
        assertNotNull(trade.getCreationDate());
        assertNull(trade.getRevisionDate());
    }

    @Test
    @DisplayName("should update existing Trade")
    void shouldUpdateExistingTrade() {
        //given
        final BigDecimal updatedBuyQuantity = BigDecimal.valueOf(10.37);
        final String updatedAccount = "updated account";
        final String updatedType = "updated type";
        final LocalDateTime creationDate = LocalDateTime.of(2025, 10, 5, 12, 52, 30);
        final int id = 5;

        final Trade existingTrade = getTrade(id);

        final TradeUpdateDto updateDto = new TradeUpdateDto();
        updateDto.setBuyQuantity(updatedBuyQuantity);
        updateDto.setAccount(updatedAccount);
        updateDto.setType(updatedType);
        updateDto.setId(id);

        given(tradeRepository.findById(id)).willReturn(Optional.of(existingTrade));

        //when
        tradeServiceImpl.handleEntityUpdate(updateDto);

        //then
        verify(tradeRepository, times(1)).save(tradeArgumentCaptor.capture());
        final Trade updated = tradeArgumentCaptor.getValue();
        assertEquals(updatedBuyQuantity, updated.getBuyQuantity());
        assertEquals(updatedAccount, updated.getAccount());
        assertEquals(updatedType, updated.getType());
        assertEquals(id, updated.getId());
        assertEquals(creationDate, updated.getCreationDate());
        assertNotNull(updated.getRevisionDate());
    }

    @Test
    @DisplayName("should throw EntityNotFoundException")
    void shouldThrowEntityNotFoundException() {
        //given
        final int id = 1;
        given(tradeRepository.findById(id)).willReturn(Optional.empty());

        //when & then
        assertThrows(EntityNotFoundException.class, () -> tradeServiceImpl.handleEntityDeletion(id));
        verify(tradeRepository, times(0)).deleteById(id);
    }

    @Test
    @DisplayName("should delete existing Trade by id")
    void shouldDeleteExistingTradeById() {
        //given
        final int id = 5;
        final Trade trade = getTrade(id);
        given(tradeRepository.findById(id)).willReturn(Optional.of(trade));

        //when
        tradeServiceImpl.handleEntityDeletion(id);

        //then
        verify(tradeRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("should return a list of TradeDto")
    void shouldReturnAListOfTradeDto() {
        //given
        final int id = 5;
        final Trade trade = getTrade(id);
        final List<Trade> list = List.of(trade);
        given(tradeRepository.findAll()).willReturn(list);

        //when
        final List<TradeDto> tradeDtos = tradeServiceImpl.findAllEntity();

        //then
        assertNotNull(tradeDtos);

        assertThat(tradeDtos)
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .comparingOnlyFields("buyQuantity", "account", "type", "id")
                .isEqualTo(list.get(0));
    }

    @Test
    @DisplayName("should return an empty list")
    void shouldReturnAnEmptyList() {
        //given
        given(tradeRepository.findAll()).willReturn(Collections.emptyList());

        //when
        final List<TradeDto> tradeDtos = tradeServiceImpl.findAllEntity();

        //then
        assertNotNull(tradeDtos);
        assertThat(tradeDtos).isEmpty();
    }

    @Test
    @DisplayName("should return TradeUpdateDto")
    void shouldReturnTradeUpdateDto() {
        //given
        final int id = 5;
        final Trade trade = getTrade(id);
        given(tradeRepository.findById(id)).willReturn(Optional.of(trade));

        //when
        final TradeUpdateDto tradeUpdateDto = tradeServiceImpl.getEntityUpdateDto(id);

        //then
        assertNotNull(tradeUpdateDto);
        assertEquals(id, tradeUpdateDto.getId());
        assertEquals(trade.getBuyQuantity(), tradeUpdateDto.getBuyQuantity());
        assertEquals(trade.getAccount(), tradeUpdateDto.getAccount());
        assertEquals(trade.getType(), tradeUpdateDto.getType());
    }

    private static Trade getTrade(final int id) {
        final BigDecimal buyQuantity = BigDecimal.valueOf(10.05);
        final String account = "account";
        final String type = "type";
        final LocalDateTime creationDate = LocalDateTime.of(2025, 10, 5, 12, 52, 30);
        return Trade.builder()
                .id(id)
                .buyQuantity(buyQuantity)
                .account(account)
                .type(type)
                .creationDate(creationDate)
                .build();
    }
}