package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.curve.CurvePointCreateDto;
import com.nnk.springboot.dto.curve.CurvePointDto;
import com.nnk.springboot.dto.curve.CurvePointUpdateDto;
import com.nnk.springboot.mapper.CurvePointMapper;
import com.nnk.springboot.repositories.CurvePointRepository;
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
public class CurvePointService extends AbstractEntityService<CurvePoint, CurvePointCreateDto, CurvePointDto, CurvePointUpdateDto>
        implements EntityService<CurvePointCreateDto, CurvePointDto, CurvePointUpdateDto> {

    public CurvePointService(final CurvePointRepository entityRepository) {
        super(entityRepository);
    }

    protected void checkEntityValidity(final CurvePointUpdateDto entityUpdateDto) {
    }

    protected void processEntityCreation(final CurvePointCreateDto curvePointCreateDto) {
        super.createEntity(curvePointCreateDto);
    }

    protected Integer getEntityId(final CurvePointUpdateDto curvePointUpdateDto) {
        return curvePointUpdateDto.getId();
    }

    protected CurvePoint getUpdatedEntity(final CurvePointUpdateDto curvePointUpdateDto, final CurvePoint curvePoint) {
        return CurvePointMapper.INSTANCE.toCurvePoint(curvePointUpdateDto, curvePoint);
    }

    protected CurvePointUpdateDto getEntityUpdateDto(final CurvePoint curvePoint) {
        return CurvePointMapper.INSTANCE.toCurvePointUpdateDto(curvePoint);
    }

    protected CurvePoint getEntity(final CurvePointCreateDto curvePointCreateDto) {
        return CurvePointMapper.INSTANCE.toCurvePoint(curvePointCreateDto);
    }

    protected void handleError(final Integer id) {
        throw new EntityNotFoundException("the CurvePoint with id " + id + " was not found");
    }

    protected List<CurvePointDto> toDtoList(final List<CurvePoint> curvePointList) {
        return CurvePointMapper.INSTANCE.toCurveDtoList(curvePointList);
    }
}
