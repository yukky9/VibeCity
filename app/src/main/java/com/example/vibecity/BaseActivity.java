package com.example.vibecity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {
    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        // Инициализация навигации только если она есть в макете
        try {
            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            if (bottomNavigationView != null) {
                setupNavigation();
            }
        } catch (Exception e) {
            Log.e("BASE_ACTIVITY", "Navigation setup skipped", e);
        }
    }
    private void setupNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
        bottomNavigationView.setSelectedItemId(getSelectedItemId());
    }

    protected abstract int getLayoutResource();
    protected abstract int getSelectedItemId();

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == getSelectedItemId()) {
            return true;
        }

        Class<?> targetActivity = null;

        if (id == R.id.nav_events) {
            targetActivity = MainActivity.class;
        } else if (id == R.id.nav_map) {
            targetActivity = MapActivity.class;
        } else if (id == R.id.nav_profile) {
            targetActivity = ProfileActivity.class;
        }

        if (targetActivity != null && !this.getClass().equals(targetActivity)) {
            startActivity(new Intent(this, targetActivity));
            overridePendingTransition(0, 0);
            finish();
            return true;
        }

        return false;
    }
    protected boolean isUserLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        return prefs.getBoolean("is_logged_in", false);
    }

    protected void checkLogin() {
        if (!isUserLoggedIn()) {
            redirectToLogin();
        }
    }

    protected void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}