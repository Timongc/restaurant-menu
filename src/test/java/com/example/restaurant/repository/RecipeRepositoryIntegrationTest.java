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
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
public class RecipeRepositoryIntegrationTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    void saveRecipe_canRetrieveItFromDatabase() {
        Recipe recipe = new Recipe();
        recipe.setName("Pizza");
        recipe.setDescription("Cheese pizza");
        recipe.setPrice(new BigDecimal("12.50"));

        Recipe savedRecipe = recipeRepository.save(recipe);
        Optional<Recipe> result = recipeRepository.findById(savedRecipe.getId());

        assertTrue(result.isPresent());
        assertEquals("Pizza", result.get().getName());
        assertEquals(
                new BigDecimal("12.50"),
                result.get().getPrice()
        );
    }

    @Test
    void findById_returnsEmptyWhenRecipeDoesNotExist() {
        Optional<Recipe> result = recipeRepository.findById(-1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void saveRecipe_generatesId() {
        Recipe recipe = new Recipe();
        recipe.setName("Test Burger");
        recipe.setDescription("A test burger");
        recipe.setPrice(new BigDecimal("8.50"));

        Recipe savedRecipe = recipeRepository.save(recipe);

        assertNotNull(savedRecipe.getId());
    }
}
