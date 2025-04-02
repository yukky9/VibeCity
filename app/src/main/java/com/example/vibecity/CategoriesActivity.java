package com.example.vibecity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import java.util.HashSet;
import java.util.Set;

public class CategoriesActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Set<String> selectedCategories = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        selectedCategories = new HashSet<>(sharedPreferences.getStringSet("user_categories", new HashSet<>()));

        // Инициализация элементов
        initCategory(R.id.category_music, "Музыка", R.drawable.ic_music);
        initCategory(R.id.category_sport, "Спорт", R.drawable.ic_sport);
        initCategory(R.id.category_food, "Еда", R.drawable.ic_food);
        initCategory(R.id.category_art, "Искусство", R.drawable.ic_art);
        initCategory(R.id.category_travel, "Путешествия", R.drawable.ic_travel);

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> saveCategories());
    }

    private void initCategory(int layoutId, String categoryName, int iconRes) {
        View categoryLayout = findViewById(layoutId);
        ImageView icon = categoryLayout.findViewById(R.id.category_icon);
        TextView name = categoryLayout.findViewById(R.id.category_name);
        SwitchCompat switchBtn = categoryLayout.findViewById(R.id.category_switch);

        icon.setImageResource(iconRes);
        name.setText(categoryName);
        switchBtn.setChecked(selectedCategories.contains(categoryName));

        switchBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedCategories.add(categoryName);
            } else {
                selectedCategories.remove(categoryName);
            }
        });
    }

    private void saveCategories() {
        if (selectedCategories.isEmpty()) {
            Toast.makeText(this, "Выберите хотя бы одну категорию", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("user_categories", selectedCategories);
        editor.apply();

        Toast.makeText(this, "Категории сохранены", Toast.LENGTH_SHORT).show();

        // Обновляем контент на главном экране
        EventLoader.loadEvents(this, selectedCategories);

        if (getIntent().getBooleanExtra("from_profile", false)) {
            finish();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}