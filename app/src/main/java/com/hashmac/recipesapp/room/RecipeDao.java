package com.hashmac.recipesapp.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.hashmac.recipesapp.models.FavouriteRecipe;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    long insert(FavouriteRecipe recipe);

    @Query("DELETE FROM favourite_recipes WHERE recipeId = :id")
    void delete(String id);

    @Query("SELECT * FROM favourite_recipes")
    List<FavouriteRecipe> getAll();

    @Query("SELECT * FROM favourite_recipes WHERE recipeId = :favouriteName")
    FavouriteRecipe getFavourite(String favouriteName);

    @Query("SELECT * FROM favourite_recipes")
    List<FavouriteRecipe> getAllFavourites();

}
