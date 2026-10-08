package com.example.restaurant.repository;

import com.example.restaurant.entity.Favorite;
import com.example.restaurant.entity.Recipe;
import com.example.restaurant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUser(User user);

    Optional<Favorite> findByUserAndRecipe(User user, Recipe recipe);

    void deleteByUserAndRecipe(User user, Recipe recipe);
}
