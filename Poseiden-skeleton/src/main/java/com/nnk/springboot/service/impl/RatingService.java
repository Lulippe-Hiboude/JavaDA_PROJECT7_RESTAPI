package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.rating.RatingCreateDto;
import com.nnk.springboot.dto.rating.RatingDto;
import com.nnk.springboot.dto.rating.RatingUpdateDto;
import com.nnk.springboot.mapper.RatingMapper;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.service.AbstractEntityService;
import com.nnk.springboot.service.EntityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class RatingService extends AbstractEntityService<Rating, RatingCreateDto, RatingDto, RatingUpdateDto>
        implements EntityService<RatingCreateDto, RatingDto, RatingUpdateDto> {
    public RatingService(final RatingRepository ratingRepository) {
        super(ratingRepository);
    }

    @Override
    protected void checkEntityValidity(final RatingUpdateDto ratingUpdateDto) {
    }

    @Override
    protected void processEntityCreation(final RatingCreateDto ratingCreateDto) {
        super.createEntity(ratingCreateDto);
    }

    @Override
    protected Integer getEntityId(final RatingUpdateDto ratingUpdateDto) {
        return ratingUpdateDto.getId();
    }

    @Override
    protected Rating getUpdatedEntity(final RatingUpdateDto ratingUpdateDto, final Rating rating) {
        return RatingMapper.INSTANCE.toRating(ratingUpdateDto, rating);
    }

    @Override
    protected RatingUpdateDto getEntityUpdateDto(final Rating rating) {
        return RatingMapper.INSTANCE.toRatingUpdateDto(rating);
    }

    @Override
    protected Rating getEntity(final RatingCreateDto ratingCreateDto) {
        return RatingMapper.INSTANCE.toRating(ratingCreateDto);
    }

    @Override
    protected void handleError(final Integer id) {
        throw new EntityNotFoundException("the rating with id " + id + " was not found");
    }

    @Override
    protected List<RatingDto> toDtoList(final List<Rating> ratings) {
        return RatingMapper.INSTANCE.toRatingDtoList(ratings);
    }
}
