package com.rafbel94.libridex_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rafbel94.libridex_api.entity.User;
import com.rafbel94.libridex_api.service.UserService;


@Controller
public class AuthController {

    private final String LOGIN_VIEW = "login";
    private final String REGISTER_FORM = "register";

    @Autowired
    @Qualifier("userService")
    UserService userService;

    @GetMapping("/login")
    public String login(Model model, @RequestParam(required = false) String error, @RequestParam(required = false) String logout) {
        model.addAttribute("error", error);
        model.addAttribute("logout", logout);
        model.addAttribute("user", new User());
        return LOGIN_VIEW;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return REGISTER_FORM;
    }
    
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, RedirectAttributes flashAttributes) {
        userService.addUser(user);
        flashAttributes.addFlashAttribute("success", "Registered successfully!");
        return LOGIN_VIEW;
    }
    
}
