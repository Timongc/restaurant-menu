package com.example.restaurant.controller;

import com.example.restaurant.entity.Recipe;
import com.example.restaurant.service.FavoriteService;
import com.example.restaurant.service.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MenuController {

    private final RecipeService recipeService;
    private final FavoriteService favoriteService;

    public MenuController(RecipeService recipeService, FavoriteService favoriteService) {
        this.recipeService = recipeService;
        this.favoriteService = favoriteService;
    }

    @GetMapping("/")
    public String menu(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "2") int size,
            Model model) {
        page -= 1;

        Page<Recipe> recipePage = recipeService.getRecipesPage(page, size);

        model.addAttribute("recipes", recipePage.getContent());
        model.addAttribute("currentPage", recipePage.getNumber());
        model.addAttribute("totalPages", recipePage.getTotalPages());

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
