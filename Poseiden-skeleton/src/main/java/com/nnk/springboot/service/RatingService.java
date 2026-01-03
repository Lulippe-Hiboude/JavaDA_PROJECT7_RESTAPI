package com.nnk.springboot.service;

import com.nnk.springboot.dto.rating.RatingCreateDto;
import com.nnk.springboot.dto.rating.RatingDto;
import com.nnk.springboot.dto.rating.RatingUpdateDto;

public interface RatingService extends EntityService<RatingCreateDto, RatingDto, RatingUpdateDto> {
}
