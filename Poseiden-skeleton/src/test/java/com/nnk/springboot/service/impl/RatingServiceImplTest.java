package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.rating.RatingCreateDto;
import com.nnk.springboot.dto.rating.RatingDto;
import com.nnk.springboot.dto.rating.RatingUpdateDto;
import com.nnk.springboot.mapper.RatingMapper;
import com.nnk.springboot.repositories.RatingRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    private RatingServiceImpl ratingServiceImpl;

    @Mock
    private RatingRepository ratingRepository;

    @Captor
    private ArgumentCaptor<Rating> ratingCaptor;

    @BeforeEach
    void setUp() {
        final RatingMapper ratingMapper = Mappers.getMapper(RatingMapper.class);
        ratingServiceImpl = new RatingServiceImpl(ratingRepository, ratingMapper);
    }

    @Test
    @DisplayName("Should create a new rating")
    void shouldCreateNewRating() {
        //given
        final RatingCreateDto ratingCreateDto = new RatingCreateDto();
        ratingCreateDto.setFitchRating("fitchRating");
        ratingCreateDto.setMoodysRating("moodysRating");
        ratingCreateDto.setSandPRating("sandPRRating");
        ratingCreateDto.setOrderNumber(1);
        //when
        ratingServiceImpl.handleEntityCreation(ratingCreateDto);
        //then
        verify(ratingRepository, times(1)).save(ratingCaptor.capture());
        final Rating rating = ratingCaptor.getValue();
        assertEquals(ratingCreateDto.getFitchRating(), rating.getFitchRating());
        assertEquals(ratingCreateDto.getMoodysRating(), rating.getMoodysRating());
        assertEquals(ratingCreateDto.getSandPRating(), rating.getSandPRating());
        assertEquals(ratingCreateDto.getOrderNumber(), rating.getOrderNumber());
    }

    @Test
    @DisplayName("should update existing rating")
    void shouldUpdateExistingRating() {
        //given
        final String fitchRating = "fitchRating";
        final String moodysRating = "moodysRating";
        final String sandPRRating = "sandPRRating";
        final String sandPRRatingUpdated = "sandPRRatingUpdated";
        final String moodysRatingUpdated = "moodysRatingUpdated";
        final String fitchRatingUpdated = "fitchRatingUpdated";
        final int id = 1;
        final int orderNumber = 1;
        final int orderNumberUpdated = 3;

        final Rating existingRating = Rating.builder()
                .fitchRating(fitchRating)
                .moodysRating(moodysRating)
                .sandPRating(sandPRRating)
                .orderNumber(orderNumber)
                .id(id)
                .build();

        final RatingUpdateDto ratingUpdateDto = new RatingUpdateDto();
        ratingUpdateDto.setFitchRating(fitchRatingUpdated);
        ratingUpdateDto.setMoodysRating(moodysRatingUpdated);
        ratingUpdateDto.setSandPRating(sandPRRatingUpdated);
        ratingUpdateDto.setOrderNumber(orderNumberUpdated);
        ratingUpdateDto.setId(id);

        given(ratingRepository.findById(id)).willReturn(Optional.of(existingRating));

        //when
        ratingServiceImpl.handleEntityUpdate(ratingUpdateDto);

        //then
        verify(ratingRepository, times(1)).save(ratingCaptor.capture());
        final Rating updatedRating = ratingCaptor.getValue();
        assertEquals(ratingUpdateDto.getFitchRating(), updatedRating.getFitchRating());
        assertEquals(ratingUpdateDto.getMoodysRating(), updatedRating.getMoodysRating());
        assertEquals(ratingUpdateDto.getSandPRating(), updatedRating.getSandPRating());
        assertEquals(ratingUpdateDto.getOrderNumber(), updatedRating.getOrderNumber());
        assertEquals(id, updatedRating.getId());
    }

    @Test
    @DisplayName("should throw EntityNotFoundException")
    void shouldThrowEntityNotFoundException() {
        //given
        final int id = 1;
        given(ratingRepository.findById(id)).willReturn(Optional.empty());

        //when & then
        assertThrows(EntityNotFoundException.class, () -> ratingServiceImpl.handleEntityDeletion(id));
        verify(ratingRepository, times(0)).deleteById(id);
    }

    @Test
    @DisplayName("should delete existing rating by id")
    void shouldDeleteExistingRatingById() {
        //given
        final int id = 1;
        final String fitchRating = "fitchRating";
        final String moodysRating = "moodysRating";
        final String sandPRRating = "sandPRRating";
        final int orderNumber = 1;

        final Rating existingRating = Rating.builder()
                .fitchRating(fitchRating)
                .moodysRating(moodysRating)
                .sandPRating(sandPRRating)
                .orderNumber(orderNumber)
                .id(id)
                .build();
        given(ratingRepository.findById(id)).willReturn(Optional.of(existingRating));

        //when
        ratingServiceImpl.handleEntityDeletion(id);

        //then
        verify(ratingRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("should return a list of RatingDto")
    void shouldReturnAListOfRatingDto() {
        //given
        final int id = 1;
        final String fitchRating = "fitchRating";
        final String moodysRating = "moodysRating";
        final String sandPRRating = "sandPRRating";
        final int orderNumber = 1;

        final Rating rating = Rating.builder()
                .fitchRating(fitchRating)
                .moodysRating(moodysRating)
                .sandPRating(sandPRRating)
                .orderNumber(orderNumber)
                .id(id)
                .build();
        final List<Rating> ratingList = List.of(rating);
        given(ratingRepository.findAll()).willReturn(ratingList);

        //when
        List<RatingDto> ratingDtoList = ratingServiceImpl.findAllEntity();

        //then
        assertEquals(1, ratingDtoList.size());
    }

    @Test
    @DisplayName("should return an empty list")
    void shouldReturnAnEmptyList() {
        //given
        final List<Rating> ratingList = List.of();
        given(ratingRepository.findAll()).willReturn(ratingList);

        //when
        List<RatingDto> ratingDtoList = ratingServiceImpl.findAllEntity();

        //then
        assertEquals(0, ratingDtoList.size());
    }

    @Test
    @DisplayName("should return RatingUpdateDto")
    void shouldReturnRatingUpdateDto() {
        //given
        final int id = 1;
        final String fitchRating = "fitchRating";
        final String moodysRating = "moodysRating";
        final String sandPRRating = "sandPRRating";
        final int orderNumber = 1;

        final Rating rating = Rating.builder()
                .fitchRating(fitchRating)
                .moodysRating(moodysRating)
                .sandPRating(sandPRRating)
                .orderNumber(orderNumber)
                .id(id)
                .build();
        given(ratingRepository.findById(id)).willReturn(Optional.of(rating));

        //when
        final RatingUpdateDto ratingUpdateDto = ratingServiceImpl.getEntityUpdateDto(id);

        //then
        assertEquals(ratingUpdateDto.getFitchRating(), rating.getFitchRating());
        assertEquals(ratingUpdateDto.getMoodysRating(), rating.getMoodysRating());
        assertEquals(ratingUpdateDto.getSandPRating(), rating.getSandPRating());
        assertEquals(ratingUpdateDto.getOrderNumber(), rating.getOrderNumber());
        assertEquals(ratingUpdateDto.getId(), rating.getId());
    }
}