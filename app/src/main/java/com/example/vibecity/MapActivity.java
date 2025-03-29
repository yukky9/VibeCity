package com.example.vibecity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

public abstract class MapActivity extends BaseActivity implements OnMapReadyCallback {
    private MapView mapView;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_map;
    }

    @Override
    protected int getSelectedItemId() {
        return R.id.nav_map;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Инициализация Яндекс Карт
        MapKitFactory.setApiKey("ВАШ_API_КЛЮЧ");
        MapKitFactory.initialize(this);

        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.mapView);

        // Установка начальной позиции камеры (Москва)
        Point moscow = new Point(55.751574, 37.573856);
        mapView.getMap().move(
                new CameraPosition(moscow, 12.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1f),
                null
        );

        // Добавление маркера
        mapView.getMap().getMapObjects().addPlacemark(moscow);
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