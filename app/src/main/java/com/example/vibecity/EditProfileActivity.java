package com.example.vibecity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditProfileActivity extends BaseActivity {

    private static final int PICK_IMAGE_REQUEST = 101;
    private ImageView ivProfile;
    private Uri imageUri;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ivProfile = findViewById(R.id.ivProfile);
        ivProfile.setOnClickListener(v -> selectImage());

        // Загрузка текущего изображения
        loadProfileImage();
    }
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected int getSelectedItemId() {
        return R.id.nav_profile;
    }


    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivProfile.setImageBitmap(bitmap);
                saveImageToStorage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImageToStorage(Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput("profile_image.jpg", Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);

            // Сохраняем путь к изображению
            SharedPreferences.Editor editor = getSharedPreferences("user_session", MODE_PRIVATE).edit();
            editor.putString("profile_image_path", "profile_image.jpg");
            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadProfileImage() {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String imagePath = prefs.getString("profile_image_path", null);

        if (imagePath != null) {
            try {
                FileInputStream fis = openFileInput(imagePath);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                ivProfile.setImageBitmap(bitmap);
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}