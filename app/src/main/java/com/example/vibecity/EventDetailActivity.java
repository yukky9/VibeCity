package com.example.vibecity;

import android.content.Intent;
import android.media.metrics.Event;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class EventDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Event event = getIntent().getParcelableExtra("event");

        ImageView eventImage = findViewById(R.id.eventImage);
        TextView title = findViewById(R.id.eventDetailTitle);
        TextView dateTime = findViewById(R.id.eventDetailDateTime);
        TextView location = findViewById(R.id.eventDetailLocation);
        TextView price = findViewById(R.id.eventDetailPrice);
        TextView description = findViewById(R.id.eventDetailDescription);
        Button btnPay = findViewById(R.id.btnPay);
        Button btnTelegram = findViewById(R.id.btnOpenTelegram);

        // Загрузка данных
        Glide.with(this).load(event.getImageUrl()).into(eventImage);
        title.setText(event.getTitle());
        dateTime.setText(event.getFormattedDateTime());
        location.setText(event.getLocation());
        description.setText(event.getDescription());

        if (event.isFree()) {
            price.setText("Бесплатное мероприятие");
            btnPay.setVisibility(View.GONE);
        } else {
            price.setText(String.format("Цена: %d ₽", event.getPrice()));
            btnPay.setVisibility(View.VISIBLE);
        }

        // Обработчики кликов
        btnPay.setOnClickListener(v -> openPaymentLink(event.getPaymentUrl()));
        btnTelegram.setOnClickListener(v -> openTelegramBot());
    }

    private void openPaymentLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void openTelegramBot() {
        String telegramUrl = "https://t.me/your_bot_username";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(telegramUrl));
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Telegram не установлен", Toast.LENGTH_SHORT).show();
        }
    }
}