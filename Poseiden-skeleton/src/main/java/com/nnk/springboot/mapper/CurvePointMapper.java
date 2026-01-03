package com.nnk.springboot.mapper;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.curve.CurvePointCreateDto;
import com.nnk.springboot.dto.curve.CurvePointDto;
import com.nnk.springboot.dto.curve.CurvePointUpdateDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CurvePointMapper extends BaseMapper<CurvePoint, CurvePointCreateDto, CurvePointDto, CurvePointUpdateDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "curveId", source = "curveId")
    @Mapping(target = "term", source = "term")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "asOfDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
    @BeanMapping(ignoreByDefault = true)
    CurvePoint toEntity(final CurvePointCreateDto curvePointCreateDto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "curveId", source = "curveId")
    @Mapping(target = "term", source = "term")
    @Mapping(target = "value", source = "value")
    @BeanMapping(ignoreByDefault = true)
    CurvePoint toEntity(final CurvePointUpdateDto curvePointUpdateDto, @MappingTarget final CurvePoint curvePoint);
}
