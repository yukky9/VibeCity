package com.example.vibecity;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {
    private static final String PREFS_NAME = "user_session";
    private final SharedPreferences prefs;

    public UserSession(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveUser(String name, String email) {
        prefs.edit()
                .putBoolean("is_logged_in", true)
                .putString("user_name", name)
                .putString("user_email", email)
                .apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean("is_logged_in", false);
    }

    public void logout() {
        prefs.edit().clear().apply();
    }
}