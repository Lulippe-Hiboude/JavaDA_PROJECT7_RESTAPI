package com.nnk.springboot.controllers;

import com.nnk.springboot.service.AbstractEntityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEntityController<E, C, R, U> {
    private final String entityName;
    private final AbstractEntityService<E, C, R, U> service;

    @GetMapping("/list")
    public String getList(final Model model) {
        log.info("get all {}s.", getEntityName());
        model.addAttribute(getEntityName() + "s", service.findAllEntity());
        return getView("/list");
    }

    @GetMapping("/add")
    public String displayAddEntityForm(final Model model) {
        final C createEntityDto = createEntityDto();
        model.addAttribute(getEntityName(), createEntityDto);
        return getView("/add");
    }

    @PostMapping("/validate")
    public abstract String submitCreateForm(@Valid @ModelAttribute final C entityCreateDto, final BindingResult result);

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") final Integer id, final Model model) {
        model.addAttribute(getEntityName(), service.getEntityUpdateDto(id));
        return getView("/update");
    }

    @PostMapping("/update/{id}")
    public abstract String updateEntity(@PathVariable("id") final Integer id,
                                        @Valid @ModelAttribute final U entityUpdateDto,
                                        final BindingResult result);

    @GetMapping("/delete/{id}")
    public String deleteEntity(@PathVariable("id") final Integer id, final RedirectAttributes redirectAttributes) {
        try {
            service.handleEntityDeletion(id);
            redirectAttributes.addFlashAttribute("successMessage", getEntityName() + " deleted successfully");
        } catch (EntityNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return getRedirectedUrl("/list");
    }

    protected final String getRedirectedUrl(final String viewName) {
        final String suffix = viewName.startsWith("/") ? viewName.substring(1) : viewName;
        return "redirect:/" + getEntityName() + "/" + suffix;
    }

    protected final String getView(final String viewName) {
        return getEntityName() + viewName;
    }

    protected abstract C createEntityDto();

    private String getEntityName() {
        return entityName;
    }
}
