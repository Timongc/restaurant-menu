package com.example.restaurant.service;

import com.example.restaurant.entity.Recipe;
import com.example.restaurant.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class RecipeServiceIntegrationTest {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    void createRecipe_savesRecipeToDatabase() {
        Recipe recipe = new Recipe();
        recipe.setName("Integration Test Pizza");
        recipe.setDescription("Pizza for testing");
        recipe.setPrice(new BigDecimal("15.50"));

        Recipe savedRecipe = recipeService.createRecipe(recipe);

        assertNotNull(savedRecipe.getId());

        Recipe foundRecipe = recipeRepository
                .findById(savedRecipe.getId())
                        .orElseThrow();

        assertEquals("Integration Test Pizza", foundRecipe.getName());
        assertEquals(new BigDecimal("15.50"), foundRecipe.getPrice());
    }

    @Test
    void getRecipeById_throwsExceptionWhenRecipeDoesNotExist() {
        assertThrows(
                RuntimeException.class,
                () -> recipeService.getRecipeById(-1L)
        );
    }
}
