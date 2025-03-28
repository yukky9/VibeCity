package com.example.vibecity;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventItem implements Parcelable {
    private String id;
    private String title;
    private Date date;
    private String location;

    public EventItem(String id, String title, Date date, String location) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.location = location;
    }

    protected EventItem(Parcel in) {
        id = in.readString();
        title = in.readString();
        date = new Date(in.readLong());
        location = in.readString();
    }

    public static final Creator<EventItem> CREATOR = new Creator<EventItem>() {
        @Override
        public EventItem createFromParcel(Parcel in) {
            return new EventItem(in);
        }

        @Override
        public EventItem[] newArray(int size) {
            return new EventItem[size];
        }
    };

    // Геттеры
    public String getId() { return id; }
    public String getTitle() { return title; }
    public Date getDate() { return date; }
    public String getLocation() { return location; }

    public String getFormattedDate() {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeLong(date.getTime());
        dest.writeString(location);
    }
}