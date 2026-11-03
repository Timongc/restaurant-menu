package com.example.restaurant.controller;

import com.example.restaurant.entity.Recipe;
import com.example.restaurant.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
@AutoConfigureMockMvc(addFilters = false)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeService recipeService;

    @Test
    void getRecipeById_returnsRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Pizza");
        recipe.setDescription("Cheese pizza");
        recipe.setPrice(new BigDecimal("12.50"));
        recipe.setIngredients("Cheese, dough");
        recipe.setImageUrl("pizza.jpg");

        when(recipeService.getRecipeById(1L))
                .thenReturn(recipe);

        mockMvc.perform(get("/api/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Pizza"))
                .andExpect(jsonPath("$.description").value("Cheese pizza"))
                .andExpect(jsonPath("$.price").value(12.50))
                .andExpect(jsonPath("$.ingredients").value("Cheese, dough"))
                .andExpect(jsonPath("$.imageUrl").value("pizza.jpg"));

        verify(recipeService).getRecipeById(1L);
    }

    @Test
    void getAllRecipes_returnsRecipes() throws Exception {
        Recipe pizza = new Recipe();
        pizza.setId(1L);
        pizza.setName("Pizza");

        Recipe burger = new Recipe();
        burger.setId(2L);
        burger.setName("Burger");

        when(recipeService.getAllRecipes())
                .thenReturn(List.of(pizza, burger));

        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Pizza"))
                .andExpect(jsonPath("$[1].name").value("Burger"));

        verify(recipeService).getAllRecipes();
    }

    @Test
    void createRecipe_returnsCreatedRecipe() throws Exception {
        Recipe savedRecipe = new Recipe();
        savedRecipe.setId(1L);
        savedRecipe.setName("Pizza");
        savedRecipe.setDescription("Cheese pizza");
        savedRecipe.setPrice(new BigDecimal("12.50"));
        savedRecipe.setIngredients("Cheese, dough");
        savedRecipe.setImageUrl("pizza.jpg");

        when(recipeService.createRecipe(any(Recipe.class)))
                .thenReturn(savedRecipe);

        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "Pizza",
                                "description": "Cheese pizza",
                                "price": 12.50,
                                "ingredients": "Cheese, dough",
                                "imageUrl": "pizza.jpg"
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Pizza"))
                .andExpect(jsonPath("$.price").value(12.50));

        verify(recipeService).createRecipe(any(Recipe.class));
    }

    @Test
    void updateRecipe_returnsUpdatedRecipe() throws Exception {
        Recipe updatedRecipe = new Recipe();
        updatedRecipe.setId(1L);
        updatedRecipe.setName("Updated Pizza");
        updatedRecipe.setPrice(new BigDecimal("15.00"));

        when(recipeService.updateRecipe(eq(1L), any(Recipe.class)))
                .thenReturn(updatedRecipe);

        mockMvc.perform(put("/api/recipes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Updated Pizza",
                            "price": 15.00
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Pizza"))
                .andExpect(jsonPath("$.price").value(15.00));

        verify(recipeService).updateRecipe(eq(1L), any(Recipe.class));
    }

    @Test
    void deleteRecipe_returnsOk() throws Exception {
        doNothing().when(recipeService).deleteRecipe(1L);

        mockMvc.perform(delete("/api/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(recipeService).deleteRecipe(1L);
    }
}
