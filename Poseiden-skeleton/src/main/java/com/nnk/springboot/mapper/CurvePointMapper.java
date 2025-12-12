package com.nnk.springboot.mapper;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.curve.CurvePointCreateDto;
import com.nnk.springboot.dto.curve.CurvePointDto;
import com.nnk.springboot.dto.curve.CurvePointUpdateDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CurvePointMapper {
    CurvePointMapper INSTANCE = Mappers.getMapper(CurvePointMapper.class);

    default List<CurvePointDto> toCurveDtoList(final List<CurvePoint> curvePoints) {
        return curvePoints.stream()
                .map(this::toCurveDto)
                .toList();
    }

    @Mapping(target = "id", source = "id")
    @Mapping(target = "curveId", source = "curveId")
    @Mapping(target = "term", source = "term")
    @Mapping(target = "value", source = "value")
    @BeanMapping(ignoreByDefault = true)
    CurvePointDto toCurveDto(final CurvePoint curvePoint);

    @Mapping(target = "curveId", source = "curveId")
    @Mapping(target = "term", source = "term")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "id", source = "id")
    CurvePointUpdateDto toCurvePointUpdateDto(final CurvePoint curvePoint);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "curveId", source = "curveId")
    @Mapping(target = "term", source = "term")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "asOfDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
    CurvePoint toCurvePoint(final CurvePointCreateDto curvePointCreateDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "curveId", source = "curveId")
    @Mapping(target = "term", source = "term")
    @Mapping(target = "value", source = "value")
    @BeanMapping(ignoreByDefault = true)
    CurvePoint toCurvePoint(final CurvePointUpdateDto curvePointUpdateDto, @MappingTarget final CurvePoint curvePoint);
}
