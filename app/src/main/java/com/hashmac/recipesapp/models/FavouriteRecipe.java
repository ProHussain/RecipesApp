package com.hashmac.recipesapp.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_recipes")
public class FavouriteRecipe {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String recipeId;

    public FavouriteRecipe(String recipeId) {
        this.recipeId = recipeId;
    }

    public long getId() {
        return id;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }
}
