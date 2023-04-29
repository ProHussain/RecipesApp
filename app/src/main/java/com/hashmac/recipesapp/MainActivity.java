package com.hashmac.recipesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.hashmac.recipesapp.databinding.ActivityMainBinding;

/**
 * Date: 2023-04-29
 * Hello Guy, Welcome to my Recipes App by Hashmac
 * We need to add one more feature to our app, which is the ability to login as Guest
 * Guest will be able to view All recipes, but not add or edit recipes
 * We will use Firebase Authentication to implement this feature
 * Let's start
 * Works Pretty Fine
 * Remember: Sometime you are tired and find it difficult to code, but don't give up
 * Just take a break and start again
 * Thanks for watching
 * Please Subscribe to my channel, like and share this video
 * See you in the next video
 * Bye Bye, Happy Coding :)
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityMainBinding binding;
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
        binding.floatingActionButton.setOnClickListener(view -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null)
                Toast.makeText(this, "Please login to add recipe", Toast.LENGTH_SHORT).show();
            else
                startActivity(new Intent(MainActivity.this, AddRecipeActivity.class));
        });
    }
}