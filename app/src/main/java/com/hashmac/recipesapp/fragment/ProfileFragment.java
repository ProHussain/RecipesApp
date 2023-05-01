package com.hashmac.recipesapp.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hashmac.recipesapp.R;
import com.hashmac.recipesapp.SettingActivity;
import com.hashmac.recipesapp.adapters.RecipeAdapter;
import com.hashmac.recipesapp.databinding.FragmentProfileBinding;
import com.hashmac.recipesapp.models.Recipe;
import com.hashmac.recipesapp.models.User;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Hello Guys, Welcome Back to our Android Development Tutorial Series
 * In this Video we will create a new Screen for Adding New Recipe
 * We will use Firebase Realtime Database to store our Recipes
 * We will use Firebase Storage to store our Recipe Images
 * We will use Firebase Authentication to Authenticate our Users
 * Let's Start Coding
 * For adding new Recipe we will create a new Activity named AddRecipeActivity
 * To navigate to this Activity we will create a button in MainActivity
 */

/**
 * Hello Guys, Welcome Back to our Android Development Tutorial Series
 * Last time i add recipe in firebase and there images in firebase storage
 * Let's retrieve all recipes from firebase and show them in our app
 * As i told you in last video that we will use RecyclerView to show our recipes
 * All recipes publish under my Profile will be shown in ProfileFragment
 * Let's Start Coding
 * Crash, let's fix it
 * Solved and works fine but design is too bad
 * Let's fix it
 * Looks good now but not feasible for Home Screen or Horizontal RecyclerView
 * Let's fix it without affecting ProfileFragment and create a new Adapter for Home Screen
 * A lot of configuration is required to make it work
 * Let's Simple create a new Adapter for Home Screen
 * test now, Some error there, let's fix it
 * By Mistake we replace our database, Lets fix it
 * All Issue are solve, in our next video we will sort populars and favorites recipes on home screen
 * and Display Recipe Details in RecipeDetailsActivity
 * Thanks for Watching
 */

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private User user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Login Required")
                    .setMessage("You need to login to view your profile")
                    .show();
        } else {
            loadProfile();
            init();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserRecipes();
    }

    private void init() {
        binding.imgEditProfile.setOnClickListener(v -> {
            // We will pick image from gallery and upload it to firebase storage
            // I prefer to use a 3rd party library for picking images from gallery
            PickImageDialog.build(new PickSetup()).show(requireActivity()).setOnPickResult(r -> {
                Log.e("ProfileFragment", "onPickResult: " + r.getUri());
                binding.imgProfile.setImageBitmap(r.getBitmap());
                binding.imgCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
                uploadImage(r.getBitmap());
            }).setOnPickCancel(() -> Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show());
        });

        binding.imgEditCover.setOnClickListener(view ->
                PickImageDialog.build(new PickSetup()).show(requireActivity()).setOnPickResult(r -> {
                    Log.e("ProfileFragment", "onPickResult: " + r.getUri());
                    binding.imgCover.setImageBitmap(r.getBitmap());
                    binding.imgCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    uploadCoverImage(r.getBitmap());
                }).setOnPickCancel(() -> Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()));

        binding.btnSetting.setOnClickListener(view1 -> startActivity(new Intent(requireContext(), SettingActivity.class)));
    }

    private void uploadCoverImage(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images/" + FirebaseAuth.getInstance().getUid() + "cover.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }
            return storageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                // We need to save this download url in firebase database
                // So that we can load it in our app
                Toast.makeText(requireContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                user.setCover(Objects.requireNonNull(downloadUri).toString());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(user);

            } else {
                Log.e("ProfileFragment", "onComplete: " + Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    private void uploadImage(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images/" + FirebaseAuth.getInstance().getUid() + "image.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }
            return storageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                // We need to save this download url in firebase database
                // So that we can load it in our app
                Toast.makeText(requireContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                user.setImage(Objects.requireNonNull(downloadUri).toString());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(user);

            } else {
                Log.e("ProfileFragment", "onComplete: " + Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    private void loadUserRecipes() {
        binding.rvProfile.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvProfile.setAdapter(new RecipeAdapter());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Recipes").orderByChild("authorId").equalTo(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Recipe> recipes = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Recipe recipe = dataSnapshot.getValue(Recipe.class);
                    recipes.add(recipe);
                }
                ((RecipeAdapter) Objects.requireNonNull(binding.rvProfile.getAdapter())).setRecipeList(recipes);
                // Let's test it
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProfileFragment", "onCancelled: " + error.getMessage());
            }
        });
    }

    private void loadProfile() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (user != null) {
                    binding.tvUserName.setText(user.getName());
                    binding.tvEmail.setText(user.getEmail());
                    Glide
                            .with(requireContext())
                            .load(user.getImage())
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher)
                            .into(binding.imgProfile);

                    Glide
                            .with(requireContext())
                            .load(user.getCover())
                            .centerCrop()
                            .placeholder(R.drawable.bg_default_recipe)
                            .into(binding.imgCover);
                } else {
                    Log.e("ProfileFragment", "onDataChange: User is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProfileFragment", "onCancelled: " + error.getMessage());
            }
        });
        User user = new User(); // Load From Firebase here, we will learn it in next video
        user.setName("Hashmac");
        user.setEmail("info@hashmac.com");
        binding.tvUserName.setText(user.getName());
        binding.tvEmail.setText(user.getEmail());
        // We will load images later, whenever we add firebase database
        // Let's test our code, Before testing our code, let's add some data in RecyclerView

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}