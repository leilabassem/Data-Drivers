package com.example.Store.controller;



import com.example.Store.model.User;
import com.example.Store.repository.UserRepository;
import com.example.Store.service.EmailService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;


    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute("user", new User());

        return "register";
    }

  
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model model) {

        
    	if (!result.hasFieldErrors("username") &&
                userRepository.existsByUsername(user.getUsername())) {

            result.rejectValue(
                    "username",
                    "error.user",
                    "Username already exists"
            );
        }

        if (!result.hasFieldErrors("email") &&
                userRepository.existsByEmail(user.getEmail())) {

            result.rejectValue(
                    "email",
                    "error.user",
                    "Email already registered"
            );
        }

        if (result.hasErrors()) {
            return "register";
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        user.setRole("USER");

        userRepository.save(user);
        
        emailService.sendEmail(
                user.getEmail(),
                "Welcome to Scentopia",
                "Your account has been created successfully!"
        );

        return "redirect:/login?registered=true";
    }

  
    @GetMapping("/login")
    public String loginPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String registered,
            @RequestParam(required = false) String logout,
            Model model) {

        if (error != null) {

            model.addAttribute( "error","Wrong username or password"
            );
        }

        if (registered != null) {

            model.addAttribute( "success", "Account created successfully"
            );
        }

        if (logout != null) {

            model.addAttribute( "info",  "Logged out successfully"
            );
        }

        return "login";
    }
}