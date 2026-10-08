package com.example.restaurant.controller;

import com.example.restaurant.entity.Recipe;
import com.example.restaurant.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminRecipeController {

    private final RecipeService recipeService;

    public AdminRecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("recipes", recipeService.getAllRecipes());
        return "admin";
    }

    @GetMapping("/recipes/new")
    public String newRecipePage(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "admin-recipe-form";
    }

    @PostMapping("/recipes")
    public String createRecipe(@ModelAttribute Recipe recipe) {
        recipeService.createRecipe(recipe);
        return "redirect:/admin";
    }

    @GetMapping("/recipes/edit/{id}")
    public String editRecipePage(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("recipe", recipeService.getRecipeById(id));
        return "admin-recipe-form";
    }

    @PostMapping("/recipes/update/{id}")
    public String updateRecipe(@PathVariable Long id, @ModelAttribute Recipe recipe) {
        recipeService.updateRecipe(id, recipe);
        return "redirect:/admin";
    }

    @PostMapping("/recipes/delete/{id}")
    public String deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return "redirect:/admin";
    }
}
