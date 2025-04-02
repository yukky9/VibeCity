package com.example.vibecity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

public class MapActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    private MapView mapView;
    private static final String YANDEX_MAPS_API_KEY = "a44af384-c275-4d80-8b42-909f5d87077b";
    private static final Point DEFAULT_LOCATION = new Point(55.751574, 37.573856);

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_map;
    }

    @Override
    protected int getSelectedItemId() {
        return R.id.nav_map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Инициализация ДО setContentView!
        MapKitFactory.setApiKey(YANDEX_MAPS_API_KEY);
        MapKitFactory.initialize(this);

        super.onCreate(savedInstanceState);

        // Настройка карты
        mapView = findViewById(R.id.mapView);
        mapView.getMap().move(
                new CameraPosition(DEFAULT_LOCATION, 12.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1f),
                null
        );

        // Настройка нижнего меню
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(this);
        bottomNav.setSelectedItemId(R.id.nav_map);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            // Уже на карте
            return true;
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        } else if (id == R.id.nav_events) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
}