package com.hashmac.recipesapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hashmac.recipesapp.R;
import com.hashmac.recipesapp.adapters.RecipeAdapter;
import com.hashmac.recipesapp.databinding.FragmentFavouritesBinding;
import com.hashmac.recipesapp.models.FavouriteRecipe;
import com.hashmac.recipesapp.models.Recipe;
import com.hashmac.recipesapp.room.RecipeRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2023-04-29
 * In this fragment, we will show all favorite recipes.
 * we load all favorite recipes from database and show them in a recyclerview.
 * We will use the same adapter we used in RecipeFragment. and Room and Firebase combination.
 * Let's start
 * Add our Fragment in Navigation Graph.
 * Let's Load Data from Database.
 * Works Prefect.
 * Thanks for watching.
 * Soon I will start a new series on testing. and using 3rd party libraries.
 * Stay tuned.
 * Bye Bye. Happpy Coding.
 */
public class FavouritesFragment extends Fragment {
    FragmentFavouritesBinding binding;
    RecipeRepository recipeRepository;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites();
    }

    private void loadFavorites() {
        recipeRepository = new RecipeRepository(requireActivity().getApplication());
        List<FavouriteRecipe> favouriteRecipes = recipeRepository.getAllFavourites();
        if (favouriteRecipes.isEmpty()) {
            Toast.makeText(requireContext(), "No Favourites", Toast.LENGTH_SHORT).show();
            binding.rvFavourites.setVisibility(View.GONE);
            binding.noFavourites.setVisibility(View.VISIBLE);
        } else {
            binding.rvFavourites.setLayoutManager(new GridLayoutManager(requireContext(), 2));
            binding.rvFavourites.setAdapter(new RecipeAdapter());
            List<Recipe> recipes = new ArrayList<>();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            for (FavouriteRecipe favouriteRecipe : favouriteRecipes) {
                                if (dataSnapshot.getKey().equals(favouriteRecipe.getRecipeId())) {
                                    recipes.add(dataSnapshot.getValue(Recipe.class));
                                }
                            }
                        }
                        binding.rvFavourites.setVisibility(View.VISIBLE);
                        binding.noFavourites.setVisibility(View.GONE);
                        RecipeAdapter adapter = (RecipeAdapter) binding.rvFavourites.getAdapter();
                        if (adapter != null) {
                            adapter.setRecipeList(recipes);
                        }

                    } else {
                        binding.noFavourites.setVisibility(View.VISIBLE);
                        binding.rvFavourites.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FavouritesFragment", "onCancelled: " + error.getMessage());
                }
            });
        }
    }
}