package com.example.Store.controller;

import com.example.Store.model.Perfume;
import com.example.Store.repository.PerfumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private PerfumeRepository perfumeRepository;

    @GetMapping({"/", "/home"})
    public String home(Model model,
                       @RequestParam(required = false) String search) {

        List<Perfume> perfumes;

        if (search != null && !search.equals("")) {

            perfumes =
                    perfumeRepository.findByNameContainingIgnoreCase(search);

            model.addAttribute("search", search);

        } else {

            perfumes =
                    perfumeRepository.findByQuantityGreaterThan(0);
        }

        model.addAttribute("perfumes", perfumes);

        return "home";
    }

    @GetMapping("/perfume/{id}")
    public String perfumeDetail(@PathVariable Long id,
                                Model model) {

        Perfume perfume =
                perfumeRepository.findById(id).orElse(null);

        if (perfume == null) {

            return "redirect:/home";
        }

        model.addAttribute("perfume", perfume);

        return "perfume-detail";
    }
}
