package com.example.vibecity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends BaseActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

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

        // Инициализация SharedPreferences
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Находим элементы интерфейса
        TextView tvName = findViewById(R.id.tvName);
        TextView tvEmail = findViewById(R.id.tvEmail);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Загружаем данные пользователя
        String userName = sharedPreferences.getString("user_name", "Пользователь");
        String userEmail = sharedPreferences.getString("user_email", "email@example.com");

        tvName.setText(userName);
        tvEmail.setText(userEmail);

        // Обработчик кнопки выхода
        btnLogout.setOnClickListener(v -> performLogout());
    }

    private void performLogout() {
        // 1. Очищаем данные сессии
        editor.clear();
        editor.apply();

        // 2. Показываем сообщение
        Toast.makeText(this, "Вы успешно вышли", Toast.LENGTH_SHORT).show();

        // 3. Переходим на экран входа
        Intent intent = new Intent(this, LoginActivity.class);

        // 4. Очищаем историю навигации
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish();
    }
}