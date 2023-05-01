package com.hashmac.recipesapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hashmac.recipesapp.AllRecipesActivity;
import com.hashmac.recipesapp.R;
import com.hashmac.recipesapp.SettingActivity;
import com.hashmac.recipesapp.adapters.HorizontalRecipeAdapter;
import com.hashmac.recipesapp.databinding.FragmentHomeBinding;
import com.hashmac.recipesapp.models.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Welcome to HashMac
 * In this video we will add some icons and images to our project
 * You can download icons from https://www.flaticon.com/ or create svg in Android Studio
 * I prefer Android Studio SVG Editor
 * Let's get started
 * Our basic icons are updated, now we add some dummy images to make our UI more beautiful
 * Look like our Home Page design is almost complete. Let's add some java functionality to our Home Page
 * If we need to update or add some more features to our Home Page, we will do it later
 * First we create a Data Model class for our recipe
 * We will use this class to show our recipe data in RecyclerView
 * Now we create a list and Add some dummy data to our list
 * Now Add some modifications to our Recipe Adapter
 * Our Home Page Almost complete, We will implement the remaining features later
 * In our next video we will create category page
 * Thank you for watching this video
 * <p>
 * Date: 2023-04-28
 * Hey, Welcome to HashMac
 * In this video we will add some recipes to our database. It's very hard to input each recipe manually, So let's do it programmatically with the help of ChatGpt
 * We will create recipe content in ChatGpt and then we will copy the content and paste it in our database with JSON format
 * Let's get started
 * Our Data will generate soo, let's wait for a while and prepare our json data
 * Super easy to do but let's ask GPT and see if they handle it or not
 * Looks good but still a mistake, let's fix it
 * Chat GPT is great but it miss response sometimes, so we need to check our data manually
 * We have Four Recipes, Let's add them to our database and Add some more data with Chat GPT
 * Let's add some other categories too
 * We have 20 recipes in our Database but without images, Let's add some images to our recipes
 * We need to do this manually, but it's not a big deal
 * It's enough for now, we will add more recipes later I will add recipe images background images later
 * See you in the next video
 * Thank you for watching this video

 --------------------------------------------------------------------------------

 * Date: 2023-04-28
 * Hey, Welcome to HashMac
 * In this video we will add some recipes to our database. It's very hard to input each recipe manually, So let's do it programmatically with the help of ChatGpt
 * We will create recipe content in ChatGpt and then we will copy the content and paste it in our database with JSON format
 * Let's get started
 * Our Data will generate soo, let's wait for a while and prepare our json data
 * Super easy to do but let's ask GPT and see if they handle it or not
 * Looks good but still a mistake, let's fix it
 * Chat GPT is great but it miss response sometimes, so we need to check our data manually
 * We have Four Recipes, Let's add them to our database and Add some more data with Chat GPT
 * Let's add some other categories too
 * We have 20 recipes in our Database but without images, Let's add some images to our recipes
 * We need to do this manually, but it's not a big deal
 * It's enough for now, we will add more recipes later I will add recipe images background images later
 * See you in the next video
 * Thank you for watching this video

 ----------------------------------------------------------

 * For HomeFragment we don't have recipes for popular and favourite categories
 * let's we select some recipes from our database and show them in our HomeFragment
 * We do this by random selection
 * Shuffle and select some recipes randomly works fine
 * Now we will create a new Activity for Recipe Details

 ----------------------------------------------------

 * Some error occurred, let's fix it
 * Works fine, that's it for this video
 * In our Next Video we will add a setting page
 * Thank you for watching this video
 * See you in the next video
 * Happy Coding

 ----------------------------------------------------

 * Date: 2023-04-28
 * Hey, Welcome to HashMac
 * In this video we will add settings page to our app
 * Let's get started, I have short time so I will do it fast and copy it from my previous project
 * We will add some more features to our settings page later
 */

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecipes();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.etSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

        binding.tvSeeAllFavourite.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), AllRecipesActivity.class);
            intent.putExtra("type", "favourite");
            startActivity(intent);
        });

        binding.tvSeeAllPopulars.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), AllRecipesActivity.class);
            intent.putExtra("type", "popular");
            startActivity(intent);
        });
    }

    private void performSearch() {
        String query = Objects.requireNonNull(binding.etSearch.getText()).toString().trim();
        Intent intent = new Intent(requireContext(), AllRecipesActivity.class);
        intent.putExtra("type", "search");
        intent.putExtra("query", query);
        startActivity(intent);

    }

    private void loadRecipes() {
        // We will load recipes from our database
        binding.rvPopulars.setAdapter(new HorizontalRecipeAdapter());
        binding.rvFavouriteMeal.setAdapter(new HorizontalRecipeAdapter());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Recipe> recipes = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Recipe recipe = dataSnapshot.getValue(Recipe.class);
                    recipes.add(recipe);
                }
                loadPopularRecipes(recipes);
                loadFavouriteRecipes(recipes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());
            }
        });
    }

    private void loadPopularRecipes(List<Recipe> recipes) {
        List<Recipe> popularRecipes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int random = (int) (Math.random() * recipes.size());
            popularRecipes.add(recipes.get(random));
        }
        HorizontalRecipeAdapter adapter = (HorizontalRecipeAdapter) binding.rvPopulars.getAdapter();
        if (adapter != null) {
            adapter.setRecipeList(popularRecipes);
        }
    }

    private void loadFavouriteRecipes(List<Recipe> recipes) {
        List<Recipe> favouriteRecipes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int random = (int) (Math.random() * recipes.size());
            favouriteRecipes.add(recipes.get(random));
        }
        HorizontalRecipeAdapter adapter = (HorizontalRecipeAdapter) binding.rvFavouriteMeal.getAdapter();
        if (adapter != null) {
            adapter.setRecipeList(favouriteRecipes);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}