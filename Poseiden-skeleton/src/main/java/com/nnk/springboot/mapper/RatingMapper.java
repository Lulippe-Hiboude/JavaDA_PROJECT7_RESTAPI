package com.nnk.springboot.mapper;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.rating.RatingCreateDto;
import com.nnk.springboot.dto.rating.RatingDto;
import com.nnk.springboot.dto.rating.RatingUpdateDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RatingMapper {
    RatingMapper INSTANCE = Mappers.getMapper(RatingMapper.class);

    default List<RatingDto> toRatingDtoList(List<Rating> ratingList) {
        return ratingList.stream()
                .map(this::toRatingDto)
                .toList();
    }

    @Mapping(target = "id", source = "id")
    @Mapping(target = "moodysRating", source = "moodysRating")
    @Mapping(target = "sandPRating", source = "sandPRating")
    @Mapping(target = "fitchRating", source = "fitchRating")
    @Mapping(target = "orderNumber", source = "orderNumber")
    RatingDto toRatingDto(final Rating rating);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "moodysRating", source = "moodysRating")
    @Mapping(target = "sandPRating", source = "sandPRating")
    @Mapping(target = "fitchRating", source = "fitchRating")
    @Mapping(target = "orderNumber", source = "orderNumber")
    Rating toRating(final RatingCreateDto ratingCreateDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "moodysRating", source = "moodysRating")
    @Mapping(target = "sandPRating", source = "sandPRating")
    @Mapping(target = "fitchRating", source = "fitchRating")
    @Mapping(target = "orderNumber", source = "orderNumber")
    RatingUpdateDto toRatingUpdateDto(final Rating rating);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "moodysRating", source = "moodysRating")
    @Mapping(target = "sandPRating", source = "sandPRating")
    @Mapping(target = "fitchRating", source = "fitchRating")
    @Mapping(target = "orderNumber", source = "orderNumber")
    Rating toRating(final RatingUpdateDto ratingUpdateDto, @MappingTarget final Rating rating);
}
