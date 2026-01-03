package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.rating.RatingCreateDto;
import com.nnk.springboot.dto.rating.RatingDto;
import com.nnk.springboot.dto.rating.RatingUpdateDto;
import com.nnk.springboot.mapper.RatingMapper;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.service.AbstractEntityService;
import com.nnk.springboot.service.RatingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
public class RatingServiceImpl extends AbstractEntityService<Rating, RatingCreateDto, RatingDto, RatingUpdateDto>
        implements RatingService {

    public RatingServiceImpl(final RatingRepository ratingRepository, final RatingMapper ratingMapper) {
        super(ratingRepository, ratingMapper);
    }

    @Override
    protected void checkEntityValidity(final RatingUpdateDto ratingUpdateDto) {
    }

    @Override
    protected Integer getEntityId(final RatingUpdateDto ratingUpdateDto) {
        return ratingUpdateDto.getId();
    }

    @Override
    protected void handleError(final Integer id) {
        throw new EntityNotFoundException("the rating with id " + id + " was not found");
    }
}
