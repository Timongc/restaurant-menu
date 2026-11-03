package com.example.restaurant.exception;

public class RecipeNotFoundException extends RuntimeException{

    public RecipeNotFoundException(Long id) {
        super("Recipe not found with id: " + id);
    }
}
