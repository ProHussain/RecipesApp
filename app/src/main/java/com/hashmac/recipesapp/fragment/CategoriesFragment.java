package com.hashmac.recipesapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hashmac.recipesapp.adapters.CategoryAdapter;
import com.hashmac.recipesapp.databinding.FragmentCategoryBinding;
import com.hashmac.recipesapp.models.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * We have 2 categories in our Database, Now we will retrieve data from firebase database and show it in our RecyclerView
 * Let's get started and test our app
 * Our app crashes. Let's check the logcat
 * Issue, solved, For design, we need to update our Category Text color and background color
 * It's working fine
 * In Our next video we will add some upload data in firebase database
 * Thanks for watching
 * If you like this video, please like and share this video
 * <p>
 * -----------------------------------------------------------------------
 * <p>
 * Welcome to HashMac
 * Add, Edit, View Feature is complete, Now we will add some more features to our app
 * Like Rate, See All, and Search
 * We will add these features in our next video
 * Thank you for watching this video
 * Happy Coding
 * <p>
 * ----------------------------------------------------------------------
 * <p>
 * Date: 2023-04-28
 * Hello Guys, Welcome to HashMac
 * Today we will add some more features to our app
 * Click on Category, and you will see all recipes of that category in AllRecipesActivity
 * Let's get started
 */

public class CategoriesFragment extends Fragment {

    private FragmentCategoryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadCategories();
    }

    private void loadCategories() {
        binding.rvCategories.setAdapter(new CategoryAdapter());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categories");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Category> categories = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category = dataSnapshot.getValue(Category.class);
                    categories.add(category);
                }
                CategoryAdapter adapter = (CategoryAdapter) binding.rvCategories.getAdapter();
                if (adapter != null) {
                    adapter.setCategoryList(categories);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}