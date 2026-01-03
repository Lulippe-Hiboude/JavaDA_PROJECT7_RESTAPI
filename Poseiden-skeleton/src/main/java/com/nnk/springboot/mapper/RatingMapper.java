package com.nnk.springboot.mapper;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.rating.RatingCreateDto;
import com.nnk.springboot.dto.rating.RatingDto;
import com.nnk.springboot.dto.rating.RatingUpdateDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RatingMapper extends BaseMapper<Rating, RatingCreateDto, RatingDto, RatingUpdateDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    Rating toEntity(final RatingCreateDto ratingCreateDto);

    @Override
    @Mapping(target = "id", ignore = true)
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "moodysRating", source = "moodysRating")
    @Mapping(target = "sandPRating", source = "sandPRating")
    @Mapping(target = "fitchRating", source = "fitchRating")
    @Mapping(target = "orderNumber", source = "orderNumber")
    Rating toEntity(final RatingUpdateDto ratingUpdateDto, @MappingTarget final Rating rating);
}
