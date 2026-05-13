package com.example.Store.controller;

import com.example.Store.model.CartItem;
import com.example.Store.model.Perfume;
import com.example.Store.repository.PerfumeRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private PerfumeRepository perfumeRepository;

    @SuppressWarnings("unchecked")
    private List<CartItem> getCart(HttpSession session) {

        List<CartItem> cart =
                (List<CartItem>) session.getAttribute("cart");

        if (cart == null) {

            cart = new ArrayList<>();

            session.setAttribute("cart", cart);
        }

        return cart;
    }

    @GetMapping
    public String cartPage(HttpSession session, Model model) {

        List<CartItem> cart = getCart(session);

        double total = 0;

        for (CartItem item : cart) {

            total += item.getSubtotal();
        }

        model.addAttribute("cart", cart);
        model.addAttribute("total", total);

        return "cart";
    }

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id,
                            @RequestParam(defaultValue = "1")
                            Integer quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        Perfume perfume = perfumeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Perfume not found"));

        // CHECK STOCK
        if (perfume.getQuantity() < quantity) {

            redirectAttributes.addFlashAttribute("error","Not enough stock available"  );

            return "redirect:/home";
        }

        List<CartItem> cart = getCart(session);

        boolean exists = false;

        for (CartItem item : cart) {

            if (item.getPerfumeId().equals(id)) {

                int newQuantity =
                        item.getQuantity() + quantity;

                // CHECK TOTAL QUANTITY
                if (newQuantity > perfume.getQuantity()) {

                    redirectAttributes.addFlashAttribute("error", "Cannot add more than available stock" );

                    return "redirect:/cart";
                }

                item.setQuantity(newQuantity);

                exists = true;
                break;
            }
        }

        if (!exists) {

            CartItem newItem = new CartItem(
                    perfume,
                    quantity
            );

            cart.add(newItem);
        }

        session.setAttribute("cart", cart);

        redirectAttributes.addFlashAttribute( "success","Perfume added to cart successfully");

        return "redirect:/cart";
    }

    @PostMapping("/remove/{index}")
    public String removeItem(@PathVariable int index,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        List<CartItem> cart = getCart(session);

        if (index >= 0 && index < cart.size()) {

            cart.remove(index);

            redirectAttributes.addFlashAttribute( "success", "Item removed from cart" );
        }

        session.setAttribute("cart", cart);

        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session,
                            RedirectAttributes redirectAttributes) {

        session.removeAttribute("cart");

        redirectAttributes.addFlashAttribute("success",  "Cart cleared successfully" );

        return "redirect:/cart";
    }
}