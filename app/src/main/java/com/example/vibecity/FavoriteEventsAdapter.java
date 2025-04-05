package com.example.vibecity;

import android.media.metrics.Event;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteEventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private List<Event> events;
    private OnEventClickListener listener;

    public interface OnEventClickListener {
        void onEventClick(Event event);
        void onFavoriteClick(Event event, boolean isFavorite);
    }

    public FavoriteEventsAdapter(List<Event> events) {
        this.events = events;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteEventsAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsAdapter.EventViewHolder holder, int position) {
        Event event = events.get(position);

        holder.title.setText(event.getTitle());
        holder.date.setText(event.getFormattedDate());
        holder.location.setText(event.getLocation());

        holder.itemView.setOnClickListener(v -> listener.onEventClick(event));
        holder.actionButton.setOnClickListener(v -> listener.onEventClick(event));

        // Иконка избранного
        int favoriteIcon = event.isFavorite() ?
                R.drawable.ic_favorite : R.drawable.ic_favorite_border;
        holder.btnFavorite.setImageResource(favoriteIcon);

        holder.btnFavorite.setOnClickListener(v -> {
            boolean newFavoriteState = !event.isFavorite();
            event.setFavorite(newFavoriteState);
            notifyItemChanged(position);
            listener.onFavoriteClick(event, newFavoriteState);
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, location;
        Button actionButton;
        ImageButton btnFavorite;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.eventTitle);
            date = itemView.findViewById(R.id.eventDate);
            location = itemView.findViewById(R.id.eventLocation);
            actionButton = itemView.findViewById(R.id.eventActionButton);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }
    }
}