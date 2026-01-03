package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.Bid;
import com.nnk.springboot.dto.trade_bid.bid.BidCreateDto;
import com.nnk.springboot.dto.trade_bid.bid.BidDto;
import com.nnk.springboot.dto.trade_bid.bid.BidUpdateDto;
import com.nnk.springboot.mapper.BidMapper;
import com.nnk.springboot.repositories.BidRepository;
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
class BidServiceImplTest {

    private BidServiceImpl bidServiceImpl;

    @Mock
    private BidRepository bidRepository;

    @Captor
    private ArgumentCaptor<Bid> bidCaptor;

    @BeforeEach
    void setUp() {
        final BidMapper bidMapper = Mappers.getMapper(BidMapper.class);
        bidServiceImpl = new BidServiceImpl(bidRepository, bidMapper);
    }

    @Test
    @DisplayName("Should create a new bid")
    void shouldCreateNewBid() {
        //given
        final BigDecimal bidQuantity = BigDecimal.valueOf(10.05);
        final String account = "account";
        final String type = "type";
        final BidCreateDto bidCreateDto = new BidCreateDto();
        bidCreateDto.setBidQuantity(bidQuantity);
        bidCreateDto.setAccount(account);
        bidCreateDto.setType(type);

        //when
        bidServiceImpl.handleEntityCreation(bidCreateDto);

        //then
        verify(bidRepository, times(1)).save(bidCaptor.capture());
        final Bid bid = bidCaptor.getValue();
        assertEquals(bidCreateDto.getBidQuantity(), bid.getBidQuantity());
        assertEquals(bidCreateDto.getAccount(), bid.getAccount());
        assertEquals(bidCreateDto.getType(), bid.getType());
        assertNotNull(bid.getCreationDate());
        assertNull(bid.getRevisionDate());
    }

    @Test
    @DisplayName("should update existing bid")
    void shouldUpdateExistingBid() {
        //given
        final BigDecimal updatedBidQuantity = BigDecimal.valueOf(10.37);
        final String updatedAccount = "updated account";
        final String updatedType = "updated type";
        final LocalDateTime creationDate = LocalDateTime.of(2025, 10, 5, 12, 52, 30);
        final int id = 5;

        final Bid existingBid = getBid(id);

        final BidUpdateDto updateDto = new BidUpdateDto();
        updateDto.setBidQuantity(updatedBidQuantity);
        updateDto.setAccount(updatedAccount);
        updateDto.setType(updatedType);
        updateDto.setId(id);

        given(bidRepository.findById(id)).willReturn(Optional.of(existingBid));

        //when
        bidServiceImpl.handleEntityUpdate(updateDto);

        //then
        verify(bidRepository, times(1)).save(bidCaptor.capture());
        final Bid updatedBid = bidCaptor.getValue();
        assertEquals(updatedBidQuantity, updatedBid.getBidQuantity());
        assertEquals(updatedAccount, updatedBid.getAccount());
        assertEquals(updatedType, updatedBid.getType());
        assertEquals(id, updatedBid.getId());
        assertEquals(creationDate, updatedBid.getCreationDate());
        assertNotNull(updatedBid.getRevisionDate());
    }

    @Test
    @DisplayName("should throw EntityNotFoundException")
    void shouldThrowEntityNotFoundException() {
        //given
        final int id = 1;
        given(bidRepository.findById(id)).willReturn(Optional.empty());

        //when & then
        assertThrows(EntityNotFoundException.class, () -> bidServiceImpl.handleEntityDeletion(id));
        verify(bidRepository, times(0)).deleteById(id);
    }

    @Test
    @DisplayName("should delete existing bid by id")
    void shouldDeleteExistingBidById() {
        //given
        final int id = 5;
        final Bid existingBid = getBid(id);
        given(bidRepository.findById(id)).willReturn(Optional.of(existingBid));

        //when
        bidServiceImpl.handleEntityDeletion(id);

        //then
        verify(bidRepository, times(1)).deleteById(id);

    }

    @Test
    @DisplayName("should return a list of BidDto")
    void shouldReturnAListOfBidDto() {
        //given
        final Bid bid = getBid(5);
        final List<Bid> bidList = List.of(bid);
        given(bidRepository.findAll()).willReturn(bidList);

        //when
        final List<BidDto> bidDtoList = bidServiceImpl.findAllEntity();

        //then
        assertNotNull(bidDtoList);

        assertThat(bidDtoList)
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .comparingOnlyFields("bidQuantity", "account", "type", "id")
                .isEqualTo(bidList.get(0));

    }

    @Test
    @DisplayName("should return an empty list")
    void shouldReturnAnEmptyList() {
        //given
        given(bidRepository.findAll()).willReturn(Collections.emptyList());
        //when
        final List<BidDto> bidDtoList = bidServiceImpl.findAllEntity();

        //then
        assertNotNull(bidDtoList);
        assertThat(bidDtoList).isEmpty();
    }

    @Test
    @DisplayName("should return BidUpdateDto")
    void shouldReturnBidUpdateDto() {
        //given
        final int id = 5;
        final Bid existingBid = getBid(id);
        given(bidRepository.findById(id)).willReturn(Optional.of(existingBid));

        //when
        final BidUpdateDto updateDto = bidServiceImpl.getEntityUpdateDto(id);

        //then
        assertNotNull(updateDto);
        assertEquals(id, updateDto.getId());
        assertEquals(existingBid.getBidQuantity(), updateDto.getBidQuantity());
        assertEquals(existingBid.getAccount(), updateDto.getAccount());
        assertEquals(existingBid.getType(), updateDto.getType());
    }

    private static Bid getBid(final int id) {
        final BigDecimal bidQuantity = BigDecimal.valueOf(10.05);
        final String account = "account";
        final String type = "type";
        final LocalDateTime creationDate = LocalDateTime.of(2025, 10, 5, 12, 52, 30);
        return Bid.builder()
                .bidQuantity(bidQuantity)
                .account(account)
                .type(type)
                .creationDate(creationDate)
                .id(id)
                .build();
    }
}