package com.hashmac.recipesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hashmac.recipesapp.adapters.RecipeAdapter;
import com.hashmac.recipesapp.databinding.ActivityAllRecipesBinding;
import com.hashmac.recipesapp.models.Recipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Date: 2023-04-28
 * Hello Guys, Welcome to HashMac
 * Let's load and filter recipes by category
 * Our filter by Category added, Let's test it
 * Filter by Category is working fine
 * Now we will add search feature
 * But before that, we will fix Home and Edit Recipe Activity
 * Everything is working fine
 * Now we will add search feature
 * Let's get started and search on StackOverflow
 * Let's test our search feature
 * Works but not as expected, Let's fix it
 * Works pretty well, Let's add some more features
 * See All Recipes implement now
 * let's test it
 *
 * Did you notice whenever we open app, Keyboad is open by default
 * We need to fix it
 */

public class AllRecipesActivity extends AppCompatActivity {
    ActivityAllRecipesBinding binding;
    DatabaseReference reference;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllRecipesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        reference = FirebaseDatabase.getInstance().getReference("Recipes");
        binding.rvRecipes.setLayoutManager(new GridLayoutManager(this,2));
        binding.rvRecipes.setAdapter(new RecipeAdapter());
        type = getIntent().getStringExtra("type");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (type.equalsIgnoreCase("category")) {
            filterByCategory();
        } else if (type.equalsIgnoreCase("search")) {
            loadByRecipes();
        } else {
            loadAllRecipes();
        }
    }

    private void loadByRecipes() {
        // Search Recipes by Name
        String query = getIntent().getStringExtra("query");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Recipe> recipes = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Recipe recipe = dataSnapshot.getValue(Recipe.class);
                    if (recipe.getName().toLowerCase().contains(query.toLowerCase()))
                        recipes.add(recipe);
                }
                RecipeAdapter adapter = (RecipeAdapter) binding.rvRecipes.getAdapter();
                if (adapter != null) {
                    adapter.setRecipeList(recipes);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());
            }
        });
    }

    private void loadAllRecipes() {
        // Load All Recipes
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Recipe> recipes = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Recipe recipe = dataSnapshot.getValue(Recipe.class);
                    recipes.add(recipe);
                }
                Collections.shuffle(recipes);
                RecipeAdapter adapter = (RecipeAdapter) binding.rvRecipes.getAdapter();
                if (adapter != null) {
                    adapter.setRecipeList(recipes);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());
            }
        });
    }

    private void filterByCategory() {
        // Filter Recipes by Category
        String category = getIntent().getStringExtra("category");
        reference.orderByChild("category").equalTo(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Recipe> recipes = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Recipe recipe = dataSnapshot.getValue(Recipe.class);
                    recipes.add(recipe);
                }
                RecipeAdapter adapter = (RecipeAdapter) binding.rvRecipes.getAdapter();
                if (adapter != null) {
                    adapter.setRecipeList(recipes);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());
            }
        });

    }
}