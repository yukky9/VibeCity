package com.example.vibecity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity {

    private static final int PICK_IMAGE_REQUEST = 101;
    private CircleImageView ivProfile;
    private TextView tvName, tvEmail;
    private Button btnEditProfile, btnLogout;
    private SharedPreferences sharedPreferences;

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

        // Инициализация элементов
        ivProfile = findViewById(R.id.ivProfile);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);

        // Загрузка данных пользователя
        loadUserData();
        loadProfileImage();

        // Обработчики кликов
        ivProfile.setOnClickListener(v -> selectImage());
        btnEditProfile.setOnClickListener(v -> openEditProfile());
        btnLogout.setOnClickListener(v -> performLogout());
    }

    private void loadUserData() {
        String userName = sharedPreferences.getString("user_name", "Пользователь");
        String userEmail = sharedPreferences.getString("user_email", "email@example.com");

        tvName.setText(userName);
        tvEmail.setText(userEmail);
    }

    private void loadProfileImage() {
        String imagePath = sharedPreferences.getString("profile_image_path", null);

        if (imagePath != null) {
            try {
                File imageFile = new File(getFilesDir(), imagePath);
                Glide.with(this)
                        .load(imageFile)
                        .placeholder(R.drawable.ic_profile_placeholder)
                        .error(R.drawable.ic_profile_placeholder)
                        .into(ivProfile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openEditProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    private void performLogout() {
        sharedPreferences.edit()
                .clear()
                .apply();

        Toast.makeText(this, "Вы успешно вышли", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                saveImageToStorage(bitmap);
                ivProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImageToStorage(Bitmap bitmap) {
        String fileName = "profile_" + System.currentTimeMillis() + ".jpg";

        try (FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);

            sharedPreferences.edit()
                    .putString("profile_image_path", fileName)
                    .apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновляем данные при возврате на экран
        loadUserData();
    }
}