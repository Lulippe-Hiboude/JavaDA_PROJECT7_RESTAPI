package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.rating.RatingCreateDto;
import com.nnk.springboot.dto.rating.RatingUpdateDto;
import com.nnk.springboot.service.impl.RatingService;
import com.nnk.springboot.utils.AuthenticationUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/rating")
public class RatingController {
    private final RatingService ratingService;

    @RequestMapping("/list")
    public String getList(final Model model) {
        log.info("get all ratings");
        model.addAttribute("ratings", ratingService.findAllEntity());
        model.addAttribute("username", AuthenticationUtil.getAuthenticatedUsername());
        return "rating/list";
    }

    @GetMapping("/add")
    public String displayAddRatingForm(final Model model) {
        model.addAttribute("rating", new RatingCreateDto());
        return "rating/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("rating") final RatingCreateDto ratingCreateDto, final BindingResult result) {
        log.info("submit rating creation form");
        if (result.hasErrors()) {
            return "rating/add";
        }
        ratingService.handleEntityCreation(ratingCreateDto);
        return "redirect:/rating/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") final Integer id, final Model model) {
        model.addAttribute("rating", ratingService.getEntityUpdateDto(id));
        return "rating/update";
    }

    @PostMapping("/update/{id}")
    public String updateRating(@PathVariable("id") final Integer id,
                               @Valid @ModelAttribute("rating") final RatingUpdateDto ratingUpdateDto,
                               final BindingResult result) {
        if (result.hasErrors()) {
            return "rating/update";
        }
        ratingService.handleEntityUpdate(ratingUpdateDto);
        return "redirect:/rating/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteRating(@PathVariable("id") final Integer id, final RedirectAttributes redirectAttributes) {
        try {
            ratingService.handleEntityDeletion(id);
            redirectAttributes.addFlashAttribute("successMessage", "rating deleted successfully");
        } catch (EntityNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/rating/list";
    }
}
