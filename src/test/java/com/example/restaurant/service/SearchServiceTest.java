package com.example.restaurant.service;

import com.example.restaurant.entity.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SearchServiceTest {

    private SearchService searchService;
    private List<Recipe> recipes;

    @BeforeEach
    void setUp() {
        searchService = new SearchService();

        Recipe pizza = new Recipe();
        pizza.setId(-1L);
        pizza.setName("pizza");
        pizza.setPrice(BigDecimal.valueOf(260));

        Recipe julius = new Recipe();
        julius.setId(-2L);
        julius.setName("Julius");
        julius.setPrice(BigDecimal.valueOf(150));

        Recipe coconut = new Recipe();
        coconut.setId(-3L);
        coconut.setName("coconut");
        coconut.setPrice(BigDecimal.valueOf(52));

        recipes = List.of(pizza, julius, coconut);
    }

    @Test
    void searchShouldSortByPricesAscending() {

        List<Recipe> result = searchService.filterRecipes(
                recipes,
                BigDecimal.ZERO,
                BigDecimal.valueOf(300),
                "",
                RecipeSort.PRICE_ASC
        );

        List<Recipe> expectedResult = List.of(
                recipes.get(2),
                recipes.get(1),
                recipes.get(0)
        );

        assertEquals(expectedResult, result, "Recipes should be sorted by price ascending");
    }

    @Test
    void searchShouldSortByPricesDescending() {

        List<Recipe> result = searchService.filterRecipes(
                recipes,
                BigDecimal.ZERO,
                BigDecimal.valueOf(300),
                "",
                RecipeSort.PRICE_DESC
        );

        List<Recipe> expectedResult = List.of(
                recipes.get(0),
                recipes.get(1),
                recipes.get(2)
        );

        assertEquals(expectedResult, result, "Recipes should be sorted by price descending");
    }

    @Test
    void searchShouldSortByNameAscending() {

        List<Recipe> result = searchService.filterRecipes(
                recipes,
                BigDecimal.ZERO,
                BigDecimal.valueOf(300),
                "",
                RecipeSort.NAME_ASC
        );

        List<Recipe> expectedResult = List.of(
                recipes.get(2),
                recipes.get(1),
                recipes.get(0)
        );

        assertEquals(expectedResult, result, "Recipes should be sorted by name ascending");
    }

    @Test
    void searchShouldExcludeSorting() {

        List<Recipe> result = searchService.filterRecipes(
                recipes,
                BigDecimal.ZERO,
                BigDecimal.valueOf(300),
                "",
                RecipeSort.NONE
        );

        List<Recipe> expectedResult = List.of(
                recipes.get(0),
                recipes.get(1),
                recipes.get(2)
        );

        assertEquals(expectedResult, result, "Recipes should NOT be sorted");
    }

    @Test
    void searchShouldThrowExceptionWhenMinPriceExceedsMaxPrice() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> searchService.filterRecipes(
                        recipes,
                        BigDecimal.valueOf(600),
                        BigDecimal.valueOf(300),
                        "",
                        RecipeSort.NONE
                )
        );

        assertEquals("maxPrice must not be less than minPrice", exception.getMessage());
        
    }
}
