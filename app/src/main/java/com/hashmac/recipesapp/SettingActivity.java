package com.hashmac.recipesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.hashmac.recipesapp.databinding.ActivitySettingBinding;

/**
 * SettingActivity class
 * Our Design for the Setting Activity page is in activity_setting.xml
 * It's ready now, let's go to the next step
 * Need to add java code to this class
 * let's test our code
 * Works pretty well
 * In our next Video We will add Favorties Functionality
 * Thank you for watching this video
 * Happy Coding
 */

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.linearLayoutShare.setOnClickListener(view -> shareApp());
        binding.linearLayoutRate.setOnClickListener(view -> rateApp());
        binding.linearLayoutFeedback.setOnClickListener(view -> sendFeedback());
        binding.linearLayoutApps.setOnClickListener(view -> moreApps());
        binding.linearLayoutPrivacy.setOnClickListener(view -> privacyPolicy());
        binding.btnSignout.setOnClickListener(view -> signOut());
    }

    private void signOut() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Sign Out", (dialogInterface, i) -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                    finishAffinity();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }).show();
    }

    private void privacyPolicy() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(android.net.Uri.parse("https://www.google.com"));
        startActivity(intent);
    }

    private void sendFeedback() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.developer_email)});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for " + getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, "Hi " + getString(R.string.developer_name) + ",");
        startActivity(Intent.createChooser(intent, "Send Feedback"));
    }

    private void moreApps() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(android.net.Uri.parse("https://play.google.com/store/apps/developer?id=" + getString(R.string.developer_id)));
        startActivity(intent);
    }

    private void rateApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(android.net.Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
        startActivity(intent);
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Wallcraft");
        intent.putExtra(Intent.EXTRA_TEXT, "Get " + getString(R.string.app_name) + " to get the best wallpapers for your phone: https://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(Intent.createChooser(intent, "Share App"));
    }
}