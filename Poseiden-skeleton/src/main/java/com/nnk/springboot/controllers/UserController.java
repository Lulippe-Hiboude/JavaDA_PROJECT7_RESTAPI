package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.user.UserCreateDto;
import com.nnk.springboot.dto.user.UserDto;
import com.nnk.springboot.dto.user.UserUpdateDto;
import com.nnk.springboot.service.impl.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequestMapping("/user")
public class UserController extends AbstractEntityController<User, UserCreateDto, UserDto, UserUpdateDto> {

    private final UserService userService;

    public UserController(final UserService userService) {
        super("user", userService);
        this.userService = userService;
    }

    @Override
    public String submitCreateForm(@Valid @ModelAttribute("user") final UserCreateDto userCreateDTO, final BindingResult result) {
        log.info("submit user creation form");
        if (result.hasErrors()) {
            return getView("/add");
        }

        try {
            userService.handleEntityCreation(userCreateDTO);
        } catch (EntityExistsException ex) {
            result.rejectValue("username", "error.user", ex.getMessage());
            return getView("/add");
        }

        return getRedirectedUrl("/list");
    }

    @Override
    public String updateEntity(@PathVariable("id") Integer id,
                               @Valid @ModelAttribute("user") final UserUpdateDto userUpdateDto,
                               final BindingResult result) {
        if (result.hasErrors()) {
            return getView("/update");
        }

        try {
            userService.handleEntityUpdate(userUpdateDto);
        } catch (EntityExistsException ex) {
            result.rejectValue("username", "error.user", ex.getMessage());
            return getView("/update");
        }

        return getRedirectedUrl("/list");
    }

    @Override
    protected UserCreateDto createEntityDto() {
        return new UserCreateDto();
    }
}
