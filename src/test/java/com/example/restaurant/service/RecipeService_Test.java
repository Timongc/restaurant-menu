package com.example.restaurant.service;

import com.example.restaurant.entity.Recipe;
import com.example.restaurant.exception.RecipeNotFoundException;
import com.example.restaurant.repository.FavoriteRepository;
import com.example.restaurant.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeService_Test {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private RecipeService recipeService;

    private List<Recipe> recipes;

    @BeforeEach
    void setUp() {
        Recipe pizza = new Recipe();
        pizza.setId(1L);
        pizza.setName("pizza");
        pizza.setPrice(BigDecimal.valueOf(260));

        Recipe julius = new Recipe();
        julius.setId(2L);
        julius.setName("Julius");
        julius.setPrice(BigDecimal.valueOf(150));

        recipes = List.of(pizza, julius);
    }

    @Test
    void getAllRecipes_returnsRecipes() {
        when(recipeRepository.findAll()).thenReturn(recipes);

        List<Recipe> result = recipeService.getAllRecipes();

        assertEquals(recipes, result);

        verify(recipeRepository).findAll();
    }

    @Test
    void getRecipeById_returnsRecipe() {
        Long id = 1L;
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipes.get(0)));

        assertSame(recipes.get(0), recipeService.getRecipeById(id));

        verify(recipeRepository).findById(id);
    }

    @Test
    void getRecipeById_throwsException_whenRecipeDoesNotExist() {
        Long id = 999L;
        when(recipeRepository.findById(id)).thenReturn(Optional.empty());

        RecipeNotFoundException exception = assertThrows(RecipeNotFoundException.class,
                () -> recipeService.getRecipeById(id));

        assertEquals("Recipe not found with id: " + id, exception.getMessage());

        verify(recipeRepository).findById(id);
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
    void updateRecipe_updatesFieldsAndSavesRecipe() {
        Long id = 1L;

        Recipe existingRecipe = new Recipe();
        existingRecipe.setId(id);
        existingRecipe.setName("Old Pizza");
        existingRecipe.setPrice(BigDecimal.valueOf(100));

        Recipe updatedRecipe = new Recipe();
        updatedRecipe.setName("New Pizza");
        updatedRecipe.setDescription("Updated description");
        updatedRecipe.setPrice(BigDecimal.valueOf(250));
        updatedRecipe.setIngredients("Cheese, tomato");
        updatedRecipe.setImageUrl("new-image.jpg");

        when(recipeRepository.findById(id)).thenReturn(Optional.of(existingRecipe));

        when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Recipe result = recipeService.updateRecipe(id, updatedRecipe);

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);

        verify(recipeRepository).save(captor.capture());

        Recipe capturedRecipe = captor.getValue();

        assertSame(existingRecipe, capturedRecipe);
        assertEquals("New Pizza", capturedRecipe.getName());
        assertEquals("Updated description", capturedRecipe.getDescription());
        assertEquals(BigDecimal.valueOf(250), capturedRecipe.getPrice());
        assertEquals("Cheese, tomato", capturedRecipe.getIngredients());
        assertEquals("new-image.jpg", capturedRecipe.getImageUrl());

        assertSame(existingRecipe, result);
    }
}
