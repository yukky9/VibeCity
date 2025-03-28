package com.example.vibecity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EventDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        EventItem event = getIntent().getParcelableExtra("event");
        if (event != null) {
            displayEventDetails(event);
        }
    }

    private void displayEventDetails(EventItem event) {
        TextView titleView = findViewById(R.id.detailTitle);
        TextView dateView = findViewById(R.id.detailDate);
        TextView locationView = findViewById(R.id.detailLocation);

        titleView.setText(event.getTitle());
        dateView.setText(event.getFormattedDate());
        locationView.setText(event.getLocation());
    }
}