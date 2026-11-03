package com.example.restaurant.controller;

import com.example.restaurant.entity.Recipe;
import com.example.restaurant.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
public class RecipeController_Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeService recipeService;

    @Test
    void getAllRecipes_returnsRecipes() throws Exception {
        Recipe pizza = new Recipe();
        pizza.setName("pizza");
        Recipe coconut = new Recipe();
        coconut.setName("coconut");

        List<Recipe> recipes = List.of(pizza, coconut);

        when(recipeService.getAllRecipes()).thenReturn(recipes);

        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("pizza")))
                .andExpect(content().string(containsString("coconut")));

    }
}
