package com.example.restaurant.service;

import com.example.restaurant.entity.Recipe;
import com.example.restaurant.repository.FavoriteRepository;
import com.example.restaurant.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void updateRecipe_updatesAllFields() {
        Long id = 1L;

        Recipe existingRecipe = new Recipe();
        existingRecipe.setName("Old name");
        existingRecipe.setDescription("Old description");
        existingRecipe.setPrice(new BigDecimal("10.00"));
        existingRecipe.setIngredients("Old ingredients");
        existingRecipe.setImageUrl("old.jpg");

        Recipe updatedRecipe = new Recipe();
        updatedRecipe.setName("New name");
        updatedRecipe.setDescription("New description");
        updatedRecipe.setPrice(new BigDecimal("25.50"));
        updatedRecipe.setIngredients("New ingredients");
        updatedRecipe.setImageUrl("new.jpg");

        when(recipeRepository.findById(id))
                .thenReturn(Optional.of(existingRecipe));

        when(recipeRepository.save(existingRecipe))
                .thenReturn(existingRecipe);

        Recipe result = recipeService.updateRecipe(id, updatedRecipe);

        assertSame(existingRecipe, result);
        assertEquals("New name", result.getName());
        assertEquals("New description", result.getDescription());
        assertEquals(new BigDecimal("25.50"), result.getPrice());
        assertEquals("New ingredients", result.getIngredients());
        assertEquals("new.jpg", result.getImageUrl());

        verify(recipeRepository).findById(id);
        verify(recipeRepository).save(existingRecipe);
    }

    @Test
    void updateRecipe_throwsExceptionWhenRecipeDoesNotExist() {
        Long id = 999L;
        Recipe updateRecipe = new Recipe();

        when(recipeRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> recipeService.updateRecipe(id, updateRecipe)
        );

        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    void getAllRecipes_returnsRecipes() {
        List<Recipe> recipes = List.of(new Recipe(), new Recipe());

        when(recipeRepository.findAll()).thenReturn(recipes);

        List<Recipe> result = recipeService.getAllRecipes();

        assertEquals(recipes, result);
        verify(recipeRepository).findAll();
    }

    @Test
    void getRecipeById_returnsRecipe() {
        Long id = 1L;
        Recipe recipe = new Recipe();

        when(recipeRepository.findById(id))
                .thenReturn(Optional.of(recipe));

        Recipe result = recipeService.getRecipeById(id);

        assertSame(recipe, result);
        verify(recipeRepository).findById(id);
    }

    @Test
    void getRecipeById_throwsExceptionWhenRecipeDoesNotExist() {
        Long id = 999L;

        when(recipeRepository.findById(id))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> recipeService.getRecipeById(id)
        );

        assertEquals("Recipe not found with id: 999", exception.getMessage());
    }

    @Test
    void createRecipe_savesRecipe() {
        Recipe recipe = new Recipe();

        when(recipeRepository.save(recipe)).thenReturn(recipe);

        Recipe result = recipeService.createRecipe(recipe);

        assertSame(recipe, result);
        verify(recipeRepository).save(recipe);
    }

    @Test
    void deleteRecipe_deletesFavoritesAndRecipe() {
        Long id = 1L;
        Recipe recipe = new Recipe();

        when(recipeRepository.findById(id))
                .thenReturn(Optional.of(recipe));

        recipeService.deleteRecipe(id);

        verify(favoriteRepository).deleteByRecipe(recipe);
        verify(recipeRepository).deleteById(id);
    }

    @Test
    void deleteRecipe_throwsExceptionWhenRecipeDoesNotExist() {
        Long id = 999L;

        when(recipeRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> recipeService.deleteRecipe(id)
        );

        verify(favoriteRepository, never()).deleteByRecipe(any());
        verify(recipeRepository, never()).deleteById(id);
    }
}
