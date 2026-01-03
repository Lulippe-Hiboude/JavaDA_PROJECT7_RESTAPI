package com.nnk.springboot.service.impl;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.curve.CurvePointCreateDto;
import com.nnk.springboot.dto.curve.CurvePointDto;
import com.nnk.springboot.dto.curve.CurvePointUpdateDto;
import com.nnk.springboot.mapper.CurvePointMapper;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.service.AbstractEntityService;
import com.nnk.springboot.service.CurvePointService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
public class CurvePointServiceImpl extends AbstractEntityService<CurvePoint, CurvePointCreateDto, CurvePointDto, CurvePointUpdateDto>
        implements CurvePointService {

    public CurvePointServiceImpl(final CurvePointRepository entityRepository, final CurvePointMapper mapper) {
        super(entityRepository, mapper);
    }

    protected void checkEntityValidity(final CurvePointUpdateDto entityUpdateDto) {
    }

    protected Integer getEntityId(final CurvePointUpdateDto curvePointUpdateDto) {
        return curvePointUpdateDto.getId();
    }

    protected void handleError(final Integer id) {
        throw new EntityNotFoundException("the CurvePoint with id " + id + " was not found");
    }
}
