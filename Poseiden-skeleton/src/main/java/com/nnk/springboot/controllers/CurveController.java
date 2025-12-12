package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.curve.CurvePointCreateDto;
import com.nnk.springboot.dto.curve.CurvePointUpdateDto;
import com.nnk.springboot.service.impl.CurvePointService;
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
@RequestMapping("/curvePoint")
public class CurveController {

    private final CurvePointService curvePointService;

    @RequestMapping("/list")
    public String getList(final Model model) {
        log.info("get all curve points");
        model.addAttribute("curvePoints", curvePointService.findAllEntity());
        model.addAttribute("username", AuthenticationUtil.getAuthenticatedUsername());
        return "curvePoint/list";
    }

    @GetMapping("/add")
    public String displayAddCurvePointForm(final Model model) {
        model.addAttribute("curvePoint", new CurvePointCreateDto());
        return "curvePoint/add";
    }

    @PostMapping("/validate")
    public String submitCreateForm(@Valid @ModelAttribute("curvePoint") final CurvePointCreateDto curvePointCreateDto, final BindingResult result) {
        log.info("submit curve point creation form");
        if (result.hasErrors()) {
            return "curvePoint/add";
        }
        curvePointService.handleEntityCreation(curvePointCreateDto);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") final Integer id, final Model model) {
        model.addAttribute("curvePoint", curvePointService.getEntityUpdateDto(id));
        return "curvePoint/update";
    }

    @PostMapping("/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id,
                                   @Valid @ModelAttribute("curvePoint") final CurvePointUpdateDto curvePointUpdateDto,
                                   final BindingResult result) {

        if (result.hasErrors()) {
            return "curvePoint/update";
        }
        curvePointService.handleEntityUpdate(curvePointUpdateDto);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") final Integer id, final RedirectAttributes redirectAttributes) {
        try {
            curvePointService.handleEntityDeletion(id);
            redirectAttributes.addFlashAttribute("successMessage", "CurvePoint deleted successfully");
        } catch (EntityNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/curvePoint/list";
    }
}
