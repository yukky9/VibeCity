package com.example.vibecity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private final List<EventItem> events;
    private final OnEventClickListener listener;

    public interface OnEventClickListener {
        void onEventClick(EventItem event);
    }

    public EventsAdapter(List<EventItem> events, OnEventClickListener listener) {
        this.events = events;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.bind(events.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView dateView;
        private final TextView locationView;
        private final Button actionButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.eventTitle);
            dateView = itemView.findViewById(R.id.eventDate);
            locationView = itemView.findViewById(R.id.eventLocation);
            actionButton = itemView.findViewById(R.id.eventActionButton);
        }

        public void bind(EventItem event, OnEventClickListener listener) {
            titleView.setText(event.getTitle());
            dateView.setText(event.getFormattedDate());
            locationView.setText(event.getLocation());

            actionButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEventClick(event);
                }
            });
        }
    }
}