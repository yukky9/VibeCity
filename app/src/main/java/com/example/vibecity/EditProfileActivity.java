package com.example.vibecity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditProfileActivity extends BaseActivity {

    private static final int PICK_IMAGE_REQUEST = 101;
    private ImageView ivProfile;
    private EditText etName, etEmail;
    private Button btnSave;
    private SharedPreferences sharedPreferences;
    private String currentPhotoPath;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected int getSelectedItemId() {
        return R.id.nav_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);

        ivProfile = findViewById(R.id.ivProfile);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        btnSave = findViewById(R.id.btnSave);

        // Загрузка текущих данных
        loadProfileData();

        // Обработчики кликов
        ivProfile.setOnClickListener(v -> openImageChooser());
        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void loadProfileData() {
        etName.setText(sharedPreferences.getString("user_name", ""));
        etEmail.setText(sharedPreferences.getString("user_email", ""));

        // Загрузка фото профиля
        String imagePath = sharedPreferences.getString("profile_image_path", null);
        if (imagePath != null) {
            File imgFile = new File(getFilesDir(), imagePath);
            if (imgFile.exists()) {
                Glide.with(this)
                        .load(imgFile)
                        .placeholder(R.drawable.ic_profile_placeholder)
                        .into(ivProfile);
            }
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivProfile.setImageBitmap(bitmap);
                currentPhotoPath = saveImageToStorage(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private String saveImageToStorage(Bitmap bitmap) {
        String fileName = "profile_" + System.currentTimeMillis() + ".jpg";
        try (FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveProfile() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_name", name);
        editor.putString("user_email", email);

        if (currentPhotoPath != null) {
            editor.putString("profile_image_path", currentPhotoPath);
        }

        editor.apply();
        Toast.makeText(this, "Профиль сохранен", Toast.LENGTH_SHORT).show();
        finish();
    }
}