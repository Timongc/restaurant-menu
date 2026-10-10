package com.example.restaurant.service;

import com.example.restaurant.entity.Recipe;
import com.example.restaurant.repository.FavoriteRepository;
import com.example.restaurant.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final FavoriteRepository favoriteRepository;

    public RecipeService(RecipeRepository recipeRepository, FavoriteRepository favoriteRepository) {
        this.recipeRepository = recipeRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Recipe updateRecipe(Long id, Recipe recipe) {
        Recipe existingRecipe = getRecipeById(id);

        existingRecipe.setName(recipe.getName());
        existingRecipe.setDescription(recipe.getDescription());
        existingRecipe.setPrice(recipe.getPrice());
        existingRecipe.setIngredients(recipe.getIngredients());
        existingRecipe.setImageUrl(recipe.getImageUrl());

        return recipeRepository.save(existingRecipe);
    }

    @Transactional
    public void deleteRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        favoriteRepository.deleteByRecipe(recipe);

        recipeRepository.deleteById(id);
    }
}
