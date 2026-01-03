package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.rating.RatingCreateDto;
import com.nnk.springboot.dto.rating.RatingDto;
import com.nnk.springboot.dto.rating.RatingUpdateDto;
import com.nnk.springboot.service.impl.RatingServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/rating")
public class RatingController extends AbstractEntityController<RatingCreateDto, RatingDto, RatingUpdateDto> {
    private final RatingServiceImpl ratingServiceImpl;

    public RatingController(RatingServiceImpl ratingServiceImpl) {
        super("rating", ratingServiceImpl);
        this.ratingServiceImpl = ratingServiceImpl;
    }

    @Override
    public String submitCreateForm(@Valid @ModelAttribute("rating") final RatingCreateDto ratingCreateDto, final BindingResult result) {
        log.info("submit rating creation form");
        if (result.hasErrors()) {
            return getView("/add");
        }
        ratingServiceImpl.handleEntityCreation(ratingCreateDto);
        return getRedirectedUrl("/list");
    }

    @Override
    public String updateEntity(@PathVariable("id") final Integer id,
                               @Valid @ModelAttribute("rating") final RatingUpdateDto ratingUpdateDto,
                               final BindingResult result) {
        if (result.hasErrors()) {
            return getView("/update");
        }
        ratingServiceImpl.handleEntityUpdate(ratingUpdateDto);
        return getRedirectedUrl("/list");
    }

    /*@Override
    public String deleteEntity(@PathVariable("id") final Integer id, final RedirectAttributes redirectAttributes) {
        try {
            ratingService.handleEntityDeletion(id);
            redirectAttributes.addFlashAttribute("successMessage", "rating deleted successfully");
        } catch (EntityNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return getRedirectedUrl("/list");
    }*/

    @Override
    protected RatingCreateDto createEntityDto() {
        return new RatingCreateDto();
    }
}
