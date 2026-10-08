package com.example.restaurant.service;

import com.example.restaurant.entity.Favorite;
import com.example.restaurant.entity.Recipe;
import com.example.restaurant.entity.User;
import com.example.restaurant.repository.FavoriteRepository;
import com.example.restaurant.repository.RecipeRepository;
import com.example.restaurant.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository, RecipeRepository recipeRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    public void addFavorite(String username, Long recipeId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        if (favoriteRepository.findByUserAndRecipe(user, recipe).isEmpty()) {

            Favorite favorite = new Favorite();

            favorite.setUser(user);
            favorite.setRecipe(recipe);

            favoriteRepository.save(favorite);
        }
    }

    public boolean isFavorite(String username, Long recipeId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        return favoriteRepository.findByUserAndRecipe(user, recipe).isPresent();
    }

    @Transactional
    public void removeFavorite(String username, Long recipeId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        favoriteRepository.deleteByUserAndRecipe(user, recipe);
    }

    public List<Favorite> getFavorites(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return favoriteRepository.findByUser(user);
    }
}
