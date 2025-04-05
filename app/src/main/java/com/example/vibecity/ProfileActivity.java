package com.example.vibecity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.metrics.Event;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.flexbox.FlexboxLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener{

    private static final int PICK_IMAGE_REQUEST = 101;
    private static final int STORAGE_PERMISSION_CODE = 102;

    private RecyclerView rvFavorites;
    private FavoriteEventsAdapter adapter;
    private CircleImageView ivProfile;
    private TextView tvName;
    private Button btnEditProfile, btnLogout;
    private SharedPreferences sharedPreferences;
    private FlexboxLayout flexboxCategories;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_profile;
    }

    @Override
    protected int getSelectedItemId() {
        return R.id.nav_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        // Настройка нижнего меню
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(this);
        bottomNav.setSelectedItemId(R.id.nav_profile);

        initViews();
        loadUserData();
        setupClickListeners();
        setupEditCategoriesButton();
    }

    private void initViews() {
        ivProfile = findViewById(R.id.ivProfile);
        tvName = findViewById(R.id.tvName);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);
        flexboxCategories = findViewById(R.id.flexboxCategories);
        rvFavorites = findViewById(R.id.rvFavorites);
        rvFavorites.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavoriteEventsAdapter(getFavoriteEvents());
        rvFavorites.setAdapter(adapter);
    }

    private List<Event> getFavoriteEvents() {
        // Здесь реализуйте получение избранных мероприятий
        // Например, из БД или SharedPreferences
        return new ArrayList<>(); // верните реальный список
    }

    private void displayUserCategories() {
        flexboxCategories.removeAllViews();
        Set<String> categories = sharedPreferences.getStringSet("user_categories", new HashSet<>());

        if (categories.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText("Интересы не выбраны");
            tv.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
            flexboxCategories.addView(tv);
            return;
        }

        for (String category : categories) {
            @LayoutRes int layoutRes = getCategoryLayoutRes(category);
            if (layoutRes != 0) {
                View chip = LayoutInflater.from(this).inflate(layoutRes, flexboxCategories, false);
                flexboxCategories.addView(chip);
            }
        }
    }

    @LayoutRes
    private int getCategoryLayoutRes(String category) {
        switch (category) {
            case "Музыка": return R.layout.chip_category_music;
            case "Спорт": return R.layout.chip_category_sport;
            case "Еда": return R.layout.chip_category_food;
            case "Искусство": return R.layout.chip_category_art;
            case "Путешествия": return R.layout.chip_category_travel;
            default: return 0;
        }
    }
    private void loadUserData() {
        String userName = sharedPreferences.getString("user_name", "Пользователь");

        tvName.setText(userName);
        loadProfileImage();
    }
    private void setupEditCategoriesButton() {
        Button btnEditCategories = findViewById(R.id.btnEditCategories);
        btnEditCategories.setOnClickListener(v -> {
            Intent intent = new Intent(this, CategoriesActivity.class);
            intent.putExtra("from_profile", true);
            startActivity(intent);
        });
    }
    private void loadProfileImage() {
        String imagePath = sharedPreferences.getString("profile_image_path", null);

        if (imagePath != null) {
            try {
                File imageFile = new File(getFilesDir(), imagePath);
                if (imageFile.exists()) {
                    Glide.with(this)
                            .load(imageFile)
                            .placeholder(R.drawable.ic_profile_placeholder)
                            .error(R.drawable.ic_profile_placeholder)
                            .into(ivProfile);
                }
            } catch (Exception e) {
                Log.e("ProfileActivity", "Error loading profile image", e);
            }
        }
    }

    private void setupClickListeners() {
        ivProfile.setOnClickListener(v -> checkPermissionAndOpenImagePicker());

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> logoutUser());
    }

    private void checkPermissionAndOpenImagePicker() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openImagePicker();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivProfile.setImageBitmap(bitmap);
                saveImageToStorage(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show();
                Log.e("ProfileActivity", "Image loading error", e);
            }
        }
    }

    private void saveImageToStorage(Bitmap bitmap) {
        String fileName = "profile_" + System.currentTimeMillis() + ".jpg";
        try (FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("profile_image_path", fileName);
            editor.apply();

            Toast.makeText(this, "Фото профиля обновлено", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка сохранения фото", Toast.LENGTH_SHORT).show();
            Log.e("ProfileActivity", "Image save error", e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this,
                        "Для изменения фото необходимо разрешение",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void logoutUser() {
        // Очищаем все данные пользователя
        sharedPreferences.edit()
                .clear()
                .apply();

        // Специально сбрасываем флаг авторизации
        getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putBoolean(LoginActivity.KEY_IS_LOGGED_IN, false)
                .apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayUserCategories();
        loadUserData(); // Обновляем данные при возвращении на экран
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            return true;
        } else if (id == R.id.nav_map) {
            startActivity(new Intent(this, MapActivity.class));
            return true;
        } else if (id == R.id.nav_events) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return false;
    }
}