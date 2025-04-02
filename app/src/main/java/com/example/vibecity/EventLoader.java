package com.example.vibecity;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class EventLoader {

    public static void loadEvents(Context context, Set<String> categories) {
        SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        Set<String> eventIds = new HashSet<>();

        // Здесь должна быть ваша логика загрузки событий
        // Например, фильтрация из локальной БД или API
        if (categories.contains("Музыка")) {
            eventIds.add("concert_1");
            eventIds.add("festival_1");
        }
        if (categories.contains("Спорт")) {
            eventIds.add("football_match");
        }

        // Сохраняем ID событий для отображения
        prefs.edit().putStringSet("user_events", eventIds).apply();
    }
}