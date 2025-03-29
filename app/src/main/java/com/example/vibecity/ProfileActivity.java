package com.example.vibecity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity {

    private static final int PICK_IMAGE_REQUEST = 101;
    private static final int STORAGE_PERMISSION_CODE = 102;

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

        initViews();
        loadUserData();
        setupClickListeners();
    }

    private void initViews() {
        ivProfile = findViewById(R.id.ivProfile);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void loadUserData() {
        String userName = sharedPreferences.getString("user_name", "Пользователь");
        String userEmail = sharedPreferences.getString("user_email", "email@example.com");

        tvName.setText(userName);
        tvEmail.setText(userEmail);
        loadProfileImage();
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
        sharedPreferences.edit()
                .clear()
                .apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData(); // Обновляем данные при возвращении на экран
    }
}