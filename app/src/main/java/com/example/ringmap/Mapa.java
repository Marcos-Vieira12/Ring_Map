package com.example.ringmap;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

public class Mapa extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;

    private Marker marker;

    private static final String TAG = "info";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(),"AIzaSyC2gLStife8TZazXCPKyEs_v3MejlmdaCQ");
        }
        PlacesClient PlacesClient = Places.createClient(this);



        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(-4.970833, -39.015),
                new LatLng(-4.970833, -39.015)
        ));

        autocompleteFragment.setCountries("BR");

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                jump(place.getLatLng());
            }


            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Toast.makeText(Mapa.this, "Erro: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // Obtenha o SupportMapFragment e notifique quando o mapa estiver pronto para ser usado

        // Configura o AutocompleteSupportFragment

        }

    public void jump(LatLng latLng) {
        if (marker != null)
            marker.remove();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        marker = mMap.addMarker(new MarkerOptions().position(latLng));
    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng quixada = new LatLng(-4.970833, -39.015);
        marker = mMap.addMarker(new MarkerOptions().position(quixada).title("Quixadá"));

        mMap.setOnMapClickListener(this::onMapClick);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(quixada, 14));

    }

    @Override
    public void onMapClick(LatLng latLng) {
        mudar_marcador(latLng);

    }

    public void mudar_marcador(LatLng latLng) {
        // Adiciona um marcador no local do clique
        if (marker == null)
            marker = mMap.addMarker(new MarkerOptions().position(latLng));
        else {
            marker.remove();
            marker = null;
        }

    }

}


