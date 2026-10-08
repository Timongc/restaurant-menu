package com.example.restaurant.controller;

import com.example.restaurant.service.FavoriteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites/add/{recipeId}")
    public String addFavorite(
            @PathVariable Long recipeId,
            Authentication authentication
    ) {
        favoriteService.addFavorite(
                authentication.getName(),
                recipeId
        );

        return "redirect:/recipes/" + recipeId;
    }

    @PostMapping("/favorites/remove/{recipeId}")
    public String removeFavorite(
            @PathVariable Long recipeId,
            Authentication authentication
    ) {
        favoriteService.removeFavorite(
                authentication.getName(),
                recipeId
        );

        return "redirect:/recipes/" + recipeId;
    }

    @GetMapping("/favorites")
    public String favorites(
            Authentication authentication,
            org.springframework.ui.Model model
    ) {
        model.addAttribute("favorites",
                            favoriteService.getFavorites(authentication.getName())
        );

        return "favorites";
    }
}
