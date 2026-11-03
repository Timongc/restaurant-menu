package com.example.restaurant.repository;

import com.example.restaurant.entity.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RecipeRepository_IntegrationTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    void saveRecipe() {
        Recipe recipe = new Recipe();
        recipe.setName("pizza");
        recipe.setPrice(BigDecimal.valueOf(260));

        Recipe savedRecipe = recipeRepository.save(recipe);

        assertNotNull(savedRecipe.getId());

        Recipe foundRecipe = recipeRepository.findById(savedRecipe.getId())
                .orElseThrow();

        assertEquals(savedRecipe.getId(), foundRecipe.getId());

        assertEquals(recipe.getName(), foundRecipe.getName());
    }

    @Test
    void findById_returnsEmptyOptional_whenRecipeDoesNotExist() {

        Long id = -1L;
        Optional<Recipe> result = recipeRepository.findById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteById() {
        Recipe recipe = new Recipe();
        recipe.setName("pizza");

        Recipe savedRecipe = recipeRepository.save(recipe);

        recipeRepository.deleteById(savedRecipe.getId());

        Optional<Recipe> result = recipeRepository.findById(savedRecipe.getId());

        assertTrue(result.isEmpty());
    }
}
