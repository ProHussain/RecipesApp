package com.hashmac.recipesapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.hashmac.recipesapp.databinding.ActivityLoginBinding;

import java.util.Objects;

/**
 * Our Login Activity design is a simple activity with a single button, two text fields and a text view.
 * The button is used to login the user and the text fields are used to enter the user's email and password.
 * The text view is used to display Login Page title
 * The activity is launched when the user clicks the Login button on the MainActivity.
 * Let's test our design by running the app.
 * it works as expected.
 * Now let's add the functionality to the Login button.
 * We will use the onClick attribute of the button to call the login method.
 * The login method will be defined in the LoginActivity class.
 * We will use the startActivity method to launch the MainActivity.
 * First we will perform some validation on the user's input.
 * So we will check if the email and password fields are empty.
 * If they are empty we will display a toast message to the user.
 * If they are not empty we will launch the MainActivity.
 * Let's start
 * Our code works fine, but we can improve it.
 *
 * Hello, Welcome to Hashmac Tutorials.
 * In this video we will learn how to create a login functionality in android studio with Firebase.
 * So let's get started
 * Login works fine but for testing we need to run our app again and again. So we will check login in Splash Screen
 * If the user is logged in we will launch the MainActivity, otherwise we will launch the LoginActivity.
 */

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnLogin.setOnClickListener(view -> login());
        binding.tvGuest.setOnClickListener(view -> startActivity(new Intent(this, MainActivity.class)));
        binding.tvSignup.setOnClickListener(view -> startActivity(new Intent(this, SingUpActivity.class))); // navigate to signup activity
    }

    private void login() {
        String email = Objects.requireNonNull(binding.etEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();
        // let's check if the email and password fields are empty
        if (email.isEmpty() || password.isEmpty()) {
            // if they are empty we will display a toast message to the user
            Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
        } else {
            // here we perform the login operation
            // if the user is successfully logged in we will launch the MainActivity
            // Let's test our Firebase connection and login functionality
            // Our code works fine, but we can improve it.
            FirebaseApp.initializeApp(this);
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class)); // Navigate to main activity
                    finish();
                } else {
                    Toast.makeText(this, "Login failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}