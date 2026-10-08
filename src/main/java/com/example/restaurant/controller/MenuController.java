package com.example.restaurant.controller;

import com.example.restaurant.service.FavoriteService;
import com.example.restaurant.service.RecipeService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MenuController {

    private final RecipeService recipeService;
    private final FavoriteService favoriteService;

    public MenuController(RecipeService recipeService, FavoriteService favoriteService) {
        this.recipeService = recipeService;
        this.favoriteService = favoriteService;
    }

    @GetMapping("/")
    public String menu(Model model) {
        model.addAttribute("recipes", recipeService.getAllRecipes());

        return "recipes";
    }

    @GetMapping("/recipes/{id}")
    public String recipe(
            @PathVariable Long id,
            Model model,
            Authentication authentication
    ) {
        model.addAttribute("recipe", recipeService.getRecipeById(id));

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("isFavorite", favoriteService.isFavorite(authentication.getName(), id));
        }

        return "recipe";
    }
}
