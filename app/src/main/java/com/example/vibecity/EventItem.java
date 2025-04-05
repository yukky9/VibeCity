package com.example.vibecity;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventItem implements Parcelable {
    private String id;
    private String title;
    private String description;
    private String location;
    private String imageUrl;
    private long dateTime;
    private int price;
    private boolean isFavorite;
    private String paymentUrl;

    // Конструкторы, геттеры, сеттеры
    // Реализация Parcelable

    public boolean isFree() {
        return price == 0;
    }

    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return sdf.format(new Date(dateTime));
    }

    public String getFormattedDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault());
        return sdf.format(new Date(dateTime));
    }
}