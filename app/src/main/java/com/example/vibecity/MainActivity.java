package com.example.vibecity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private Button btnLogout;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnLogout = findViewById(R.id.btnLogout);
        prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);

        String name = prefs.getString("name", "Пользователь");
        tvWelcome.setText("Добро пожаловать, " + name + "!");

        btnLogout.setOnClickListener(v -> {
            prefs.edit().putBoolean("isLoggedIn", false).apply();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }
}