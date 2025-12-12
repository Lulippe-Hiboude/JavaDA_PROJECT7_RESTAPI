package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.curve.CurvePointCreateDto;
import com.nnk.springboot.dto.curve.CurvePointDto;
import com.nnk.springboot.dto.curve.CurvePointUpdateDto;
import com.nnk.springboot.repositories.CurvePointRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CurvePointServiceTest {
    @InjectMocks
    private CurvePointService curvePointService;

    @Mock
    private CurvePointRepository curvePointRepository;

    @Captor
    private ArgumentCaptor<CurvePoint> curvePointArgumentCaptor;

    @Test
    @DisplayName("Should create a new rating")
    void shouldCreateNewRating() {
        //given
        final double term = 12.5;
        final double value = 20.3;
        final int curveId = 10;
        final CurvePointCreateDto curvePointCreateDto = new CurvePointCreateDto();
        curvePointCreateDto.setTerm(term);
        curvePointCreateDto.setValue(value);
        curvePointCreateDto.setCurveId(curveId);

        //when
        curvePointService.handleEntityCreation(curvePointCreateDto);

        //then
        verify(curvePointRepository, times(1)).save(curvePointArgumentCaptor.capture());
        final CurvePoint curvePoint = curvePointArgumentCaptor.getValue();
        assertEquals(curvePoint.getTerm(), BigDecimal.valueOf(curvePointCreateDto.getTerm()));
        assertEquals(curvePoint.getValue(), BigDecimal.valueOf(curvePointCreateDto.getValue()));
        assertEquals(curvePoint.getCurveId(), curvePointCreateDto.getCurveId());

    }

    @Test
    @DisplayName("should update existing rating except password")
    void shouldUpdateExistingRatingExceptPassword() {
        //given
        final BigDecimal term = BigDecimal.valueOf(12.5);
        final BigDecimal value = BigDecimal.valueOf(20.3);
        final int curveId = 10;
        final double termUpdated = 15.5;
        final double valueUpdated = 25.3;
        final int curveIdUpdated = 15;
        final int id = 1;


        final CurvePoint existingCurvePoint = CurvePoint.builder()
                .id(id)
                .term(term)
                .value(value)
                .curveId(curveId)
                .build();

        final CurvePointUpdateDto curvePointUpdateDto = new CurvePointUpdateDto();
        curvePointUpdateDto.setId(id);
        curvePointUpdateDto.setTerm(termUpdated);
        curvePointUpdateDto.setValue(valueUpdated);
        curvePointUpdateDto.setCurveId(curveIdUpdated);


        given(curvePointRepository.findById(id)).willReturn(Optional.of(existingCurvePoint));

        //when
        curvePointService.handleEntityUpdate(curvePointUpdateDto);

        //then
        verify(curvePointRepository, times(1)).save(curvePointArgumentCaptor.capture());
        final CurvePoint curvePointUpdated = curvePointArgumentCaptor.getValue();

        assertEquals(id, curvePointUpdated.getId());
        assertEquals(BigDecimal.valueOf(termUpdated), curvePointUpdated.getTerm());
        assertEquals(BigDecimal.valueOf(valueUpdated), curvePointUpdated.getValue());
        assertEquals(curveIdUpdated, curvePointUpdated.getCurveId());
    }

    @Test
    @DisplayName("should throw EntityNotFoundException")
    void shouldThrowEntityNotFoundException() {
        //given
        final int id = 1;
        given(curvePointRepository.findById(id)).willReturn(Optional.empty());

        //when & then
        assertThrows(EntityNotFoundException.class, () -> curvePointService.handleEntityDeletion(id));
        verify(curvePointRepository, times(0)).deleteById(id);
    }

    @Test
    @DisplayName("should delete existing rating by id")
    void shouldDeleteExistingRatingById() {
        //given
        final BigDecimal term = BigDecimal.valueOf(12.5);
        final BigDecimal value = BigDecimal.valueOf(20.3);
        final int curveId = 10;
        final int id = 1;


        final CurvePoint existingCurvePoint = CurvePoint.builder()
                .id(id)
                .term(term)
                .value(value)
                .curveId(curveId)
                .build();
        given(curvePointRepository.findById(id)).willReturn(Optional.of(existingCurvePoint));

        //when
        curvePointService.handleEntityDeletion(id);

        //then
        verify(curvePointRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("should return a list of RatingDto")
    void shouldReturnAListOfRatingDto() {
        //given
        final BigDecimal term = BigDecimal.valueOf(12.5);
        final BigDecimal value = BigDecimal.valueOf(20.3);
        final int curveId = 10;
        final int id = 1;


        final CurvePoint curvePoint = CurvePoint.builder()
                .id(id)
                .term(term)
                .value(value)
                .curveId(curveId)
                .build();
        final List<CurvePoint> curvePointList = List.of(curvePoint);
        given(curvePointRepository.findAll()).willReturn(curvePointList);

        //when
        List<CurvePointDto> curvePointDtoList = curvePointService.findAllEntity();

        //then
        assertEquals(1, curvePointDtoList.size());
    }

    @Test
    @DisplayName("should return an empty list")
    void shouldReturnAnEmptyList() {
        //given
        final List<CurvePoint> curvePointList = List.of();
        given(curvePointRepository.findAll()).willReturn(curvePointList);

        //when
        List<CurvePointDto> curvePointDtoList = curvePointService.findAllEntity();

        //then
        assertEquals(0, curvePointDtoList.size());
    }

    @Test
    @DisplayName("should return RatingUpdateDto")
    void shouldReturnRatingUpdateDto() {
        //given
        final BigDecimal term = BigDecimal.valueOf(12.5);
        final BigDecimal value = BigDecimal.valueOf(20.3);
        final int curveId = 10;
        final int id = 1;


        final CurvePoint curvePoint = CurvePoint.builder()
                .id(id)
                .term(term)
                .value(value)
                .curveId(curveId)
                .build();
        given(curvePointRepository.findById(id)).willReturn(Optional.of(curvePoint));

        //when
        final CurvePointUpdateDto curvePointUpdateDto = curvePointService.getEntityUpdateDto(id);

        //then
        assertEquals(id, curvePointUpdateDto.getId());
        assertEquals(term, BigDecimal.valueOf(curvePointUpdateDto.getTerm()));
        assertEquals(value, BigDecimal.valueOf(curvePointUpdateDto.getValue()));
        assertEquals(curveId, curvePointUpdateDto.getCurveId());
    }

}