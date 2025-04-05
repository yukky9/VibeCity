package com.example.vibecity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "AuthPrefs";
    public static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String TELEGRAM_BOT_URL = "https://t.me/vibecity_expresspe_bot";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkLoginStatus();

        Button btnOpenBot = findViewById(R.id.btnOpenBot);
        btnOpenBot.setOnClickListener(v -> {
            openTelegramBot();
            goToRegisterActivity();
        });
    }

    private void checkLoginStatus() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (prefs.getBoolean(KEY_IS_LOGGED_IN, false)) {
            startMainActivity();
        }
    }

    private void openTelegramBot() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TELEGRAM_BOT_URL));
        startActivity(intent);
    }

    private void goToRegisterActivity() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    // Метод для выхода из профиля
    public static void logout(SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();
    }
}