package com.example.ringmap.ui.places;

import com.google.firebase.firestore.GeoPoint;

public class FavoriteLocation {
    private String locationName;
    private GeoPoint locationPoint; // Usando GeoPoint para latitude e longitude

    // Construtores, getters e setters

    public FavoriteLocation() {
        // Construtor padrão necessário para Firebase
    }

    public FavoriteLocation(String locationName, GeoPoint locationPoint) {
        this.locationName = locationName;
        this.locationPoint = locationPoint;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public GeoPoint getLocationPoint() {
        return locationPoint;
    }

    public void setLocationPoint(GeoPoint locationPoint) {
        this.locationPoint = locationPoint;
    }
}
