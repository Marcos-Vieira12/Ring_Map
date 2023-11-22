package com.example.ringmap.ui.places;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ringmap.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FavoriteLocationsAdapter extends RecyclerView.Adapter<FavoriteLocationsAdapter.LocationViewHolder> {

    private List<FavoriteLocation> favoriteLocations;

    public FavoriteLocationsAdapter() {
        this.favoriteLocations = new ArrayList<>();
    }

    public void setFavoriteLocations(List<FavoriteLocation> favoriteLocations) {
        this.favoriteLocations = favoriteLocations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        FavoriteLocation location = favoriteLocations.get(position);
        holder.bind(location);
    }

    @Override
    public int getItemCount() {
        return favoriteLocations.size();
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {

        private TextView locationNameTextView;
        private TextView coordinatesTextView;

        LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationNameTextView = itemView.findViewById(R.id.locationNameTextView);
            coordinatesTextView = itemView.findViewById(R.id.coordinatesTextView);
        }

        void bind(FavoriteLocation location) {
            locationNameTextView.setText(location.getLocationName());
            String coordinatesText = String.format(Locale.getDefault(),
                    "Lat: %f, Lon: %f", location.getLocationPoint().getLatitude(), location.getLocationPoint().getLongitude());
            coordinatesTextView.setText(coordinatesText);
        }
    }
}
