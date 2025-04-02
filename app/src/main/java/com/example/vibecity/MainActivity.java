package com.example.vibecity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements EventsAdapter.OnEventClickListener {

    private RecyclerView eventsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        Set<String> categories = prefs.getStringSet("user_categories", null);

        if (categories == null || categories.isEmpty()) {
            // Если категории не выбраны, перенаправляем на выбор
            startActivity(new Intent(this, CategoriesActivity.class));
            finish();
            return;
        }

        // Используем выбранные категории
        for (String category : categories) {
            Log.d("UserCategories", category);
        }

        setupRecyclerView();
        setupBottomNavigation();
    }

    private void setupRecyclerView() {
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<EventItem> events = generateTestEvents();
        EventsAdapter adapter = new EventsAdapter(events, this);
        eventsRecyclerView.setAdapter(adapter);
    }

    private List<EventItem> generateTestEvents() {
        List<EventItem> events = new ArrayList<>();
        events.add(new EventItem("1", "Концерт в парке", new Date(), "Центральный парк"));
        events.add(new EventItem("2", "Выставка искусств", new Date(), "Городской музей"));
        return events;
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(this::handleNavigationItemSelected);
    }

    private boolean handleNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_events) {
            return true;
        } else if (id == R.id.nav_map) {
            startActivity(new Intent(this, MapActivity.class));
            return true;
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        }
        return false;
    }

    @Override
    public void onEventClick(EventItem event) {
        showEventDetails(event);
    }

    private void showEventDetails(EventItem event) {
        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra("event", event);
        startActivity(intent);
    }
}