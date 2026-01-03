package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/home")
    public String home(Model model) {
        return "redirect:/bid/list";
    }

    @RequestMapping("/admin/home")
    public String adminHome(Model model) {
        return "redirect:/bid/list";
    }
}
