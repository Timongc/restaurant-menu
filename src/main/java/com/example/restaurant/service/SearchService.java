package com.example.restaurant.service;

import com.example.restaurant.entity.Recipe;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

public class SearchService {

    public List<Recipe> filterRecipes(List<Recipe> recipes, BigDecimal minPrice, BigDecimal maxPrice, String keyword, RecipeSort recipeSort) {

        Objects.requireNonNull(recipes, "recipes must not be null");
        Objects.requireNonNull(minPrice, "minPrice must not be null");
        Objects.requireNonNull(maxPrice, "maxPrice must not be null");
        Objects.requireNonNull(keyword, "keyword must not be null");
        Objects.requireNonNull(recipeSort, "recipeSort must not be null");

        if (minPrice.compareTo(BigDecimal.ZERO) < 0 || maxPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Prices must not be negative");
        }
        if (minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("maxPrice must not be less than minPrice");
        }

        String keywordInLowerCase = keyword.toLowerCase(Locale.ROOT);

        Stream<Recipe> resultStream = recipes.stream()
                .filter(recipe -> recipe.getPrice().compareTo(maxPrice) <= 0
                        && recipe.getPrice().compareTo(minPrice) >= 0)
                .filter(recipe -> recipe.getName().toLowerCase(Locale.ROOT).contains(keywordInLowerCase));

        switch (recipeSort) {
            case NONE: break;
            case PRICE_ASC: return resultStream
                    .sorted(Comparator.comparing(Recipe::getPrice))
                    .toList();
            case PRICE_DESC: return resultStream
                    .sorted(Comparator.comparing(Recipe::getPrice).reversed())
                    .toList();
            case NAME_ASC: return resultStream
                    .sorted(Comparator.comparing(Recipe::getName, String.CASE_INSENSITIVE_ORDER))
                    .toList();
        }

        return resultStream.toList();
    }
}
