package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.curve.CurvePointCreateDto;
import com.nnk.springboot.dto.curve.CurvePointDto;
import com.nnk.springboot.dto.curve.CurvePointUpdateDto;
import com.nnk.springboot.service.CurvePointService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.nnk.springboot.enums.ViewEnum.*;

@Controller
@Slf4j
@RequestMapping("/curvePoint")
public class CurveController extends AbstractEntityController<CurvePointCreateDto, CurvePointDto, CurvePointUpdateDto> {

    private final CurvePointService curvePointService;

    public CurveController(final CurvePointService curvePointService) {
        super("curvePoint", curvePointService);
        this.curvePointService = curvePointService;
    }

    @Override
    public String submitCreateForm(@Valid @ModelAttribute("curvePoint") final CurvePointCreateDto curvePointCreateDto, final BindingResult result) {
        log.info("submit curve point creation form");
        if (result.hasErrors()) {
            return getView(ADD.getValue());
        }
        curvePointService.handleEntityCreation(curvePointCreateDto);
        return getRedirectedUrl(LIST.getValue());
    }

    @Override
    public String updateEntity(@PathVariable("id") Integer id,
                               @Valid @ModelAttribute("curvePoint") final CurvePointUpdateDto curvePointUpdateDto,
                               final BindingResult result) {

        if (result.hasErrors()) {
            return getView(UPDATE.getValue());
        }
        curvePointService.handleEntityUpdate(curvePointUpdateDto);
        return getRedirectedUrl(LIST.getValue());
    }

    @Override
    protected CurvePointCreateDto createEntityDto() {
        return new CurvePointCreateDto();
    }
}
