package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.user.UserCreateDto;
import com.nnk.springboot.dto.user.UserUpdateDto;
import com.nnk.springboot.service.impl.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/list")
    public String getList(final Model model) {
        log.info("get all users");
        model.addAttribute("users", userService.findAllEntity());
        return "user/list";
    }

    @GetMapping("/add")
    public String displayAddUserForm(final Model model) {
        model.addAttribute("user", new UserCreateDto());
        return "user/add";
    }

    @PostMapping("/validate")
    public String submitCreateForm(@Valid @ModelAttribute("user") final UserCreateDto userCreateDTO, final BindingResult result) {
        log.info("submit user creation form");
        if (result.hasErrors()) {
            return "user/add";
        }

        try {
            userService.handleEntityCreation(userCreateDTO);
        } catch (EntityExistsException ex) {
            result.rejectValue("username", "error.user", ex.getMessage());
            return "user/add";
        }

        return "redirect:/user/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") final Integer id, final Model model) {
        model.addAttribute("user", userService.getEntityUpdateDto(id));
        return "user/update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Integer id,
                             @Valid @ModelAttribute("user") final UserUpdateDto userUpdateDto,
                             final BindingResult result) {
        if (result.hasErrors()) {
            return "user/update";
        }

        try {
            userService.handleEntityUpdate(userUpdateDto);
        } catch (EntityExistsException ex) {
            result.rejectValue("username", "error.user", ex.getMessage());
            return "user/update";
        }

        return "redirect:/user/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") final Integer id, final RedirectAttributes redirectAttributes) {
        try {
            userService.handleEntityDeletion(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully");
        } catch (EntityNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/user/list";
    }
}
