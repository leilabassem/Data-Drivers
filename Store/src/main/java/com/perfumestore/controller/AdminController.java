package com.example.Store.controller;


import com.example.Store.model.Order;
import com.example.Store.model.Perfume;
import com.example.Store.repository.OrderRepository;
import com.example.Store.repository.PerfumeRepository;
import com.example.Store.repository.UserRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PerfumeRepository perfumeRepository;
    
    @Autowired
    private OrderRepository orderRepository;
   
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping
    public String dashboard(Model model) {

        long perfumeCount = perfumeRepository.count();
        long orderCount = orderRepository.count();
        long userCount = userRepository.count();

        model.addAttribute("totalPerfumes", perfumeCount);
        model.addAttribute("totalOrders", orderCount);
        model.addAttribute("totalUsers", userCount);

        return "admin/dashboard";
    }

   
    @GetMapping("/perfumes")
    public String perfumesPage(Model model) {

        model.addAttribute("perfumes", perfumeRepository.findAll());

        return "admin/perfumes";
    }

  
    @GetMapping("/perfumes/add")
    public String addPerfumePage(Model model) {

        model.addAttribute("perfume",
                new Perfume());

        return "admin/add-perfume";
    }

   
    @PostMapping("/perfumes/add")
    public String savePerfume(
            @Valid @ModelAttribute("perfume") Perfume perfume,
            BindingResult result) {

        if (result.hasErrors()) {

            return "admin/add-perfume";
        }

        perfumeRepository.save(perfume);

        return "redirect:/admin/perfumes";
    }

   
    @GetMapping("/perfumes/edit/{id}")
    public String editPerfumePage(@PathVariable Long id, Model model) {

        Perfume perfume = perfumeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Perfume not found"));

        model.addAttribute("perfume", perfume);

        return "admin/edit-perfume";
    }

    
    @PostMapping("/perfumes/edit/{id}")
    public String updatePerfume(@PathVariable Long id,
    		@Valid @ModelAttribute("perfume") Perfume perfume,
    		BindingResult result) {

        if (result.hasErrors()) {

            return "admin/edit-perfume";
        }
        

        perfume.setId(id);

        perfumeRepository.save(perfume);

        return "redirect:/admin/perfumes";
    }

   
    @PostMapping("/perfumes/delete/{id}")
    public String deletePerfume(@PathVariable Long id) {

        perfumeRepository.deleteById(id);

        return "redirect:/admin/perfumes";
    }

   
    @GetMapping("/orders")
    public String ordersPage(Model model) {

        model.addAttribute("orders",
                orderRepository.findAllByOrderByOrderDateDesc());

        return "admin/orders";
    }

 
    @GetMapping("/users")
    public String usersPage(Model model) {

        model.addAttribute("users",
                userRepository.findAll());

        return "admin/users";
    }
    
    @PostMapping("/orders/approve/{id}")
    public String approveOrder(@PathVariable Long id){

        Order order =
                orderRepository.findById(id)
                .orElse(null);

        if(order != null){

            order.setStatus("Approved");

            orderRepository.save(order);
        }

        return "redirect:/admin/orders";
    }
}