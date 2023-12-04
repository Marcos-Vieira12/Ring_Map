package com.example.ringmap.ui.places;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ringmap.Mapa;
import com.example.ringmap.R;
import com.example.ringmap.databinding.FragmentPlacesBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PlacesFragment extends Fragment implements FavoriteLocationsAdapter.OnItemClickListener{

    private FragmentPlacesBinding binding;

    private FavoriteLocationsAdapter adapter;

    FloatingActionButton mAddFab, mdeleteFab, mAddPlaceFab;

    // These are taken to make visible and invisible along with FABs
    TextView deleteText, addPlaceText, mTextPlaces;

    // to check whether sub FAB buttons are visible or not.
    Boolean isAllFabsVisible;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlacesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        InicializaComponents();

        mTextPlaces.setText("você ainda não tem nenhum lugar favorito.");

        mdeleteFab.setVisibility(View.GONE);
        mAddPlaceFab.setVisibility(View.GONE);
        deleteText.setVisibility(View.GONE);
        addPlaceText.setVisibility(View.GONE);

        isAllFabsVisible = false;

        mAddFab.setOnClickListener(view -> {

            if (!isAllFabsVisible) {
                // when isAllFabsVisible becomes true make all
                // the action name texts and FABs VISIBLE
                mdeleteFab.show();
                mAddPlaceFab.show();
                deleteText.setVisibility(View.VISIBLE);
                addPlaceText.setVisibility(View.VISIBLE);

                // make the boolean variable true as we
                // have set the sub FABs visibility to GONE
                isAllFabsVisible = true;
            } else {
                // when isAllFabsVisible becomes true make
                // all the action name texts and FABs GONE.
                mdeleteFab.hide();
                mAddPlaceFab.hide();
                deleteText.setVisibility(View.GONE);
                addPlaceText.setVisibility(View.GONE);

                // make the boolean variable false as we
                // have set the sub FABs visibility to GONE
                isAllFabsVisible = false;
            }
        });

        mAddPlaceFab.setOnClickListener(View -> {

            AppCompatActivity activity = (AppCompatActivity) requireActivity();

            Intent intent = new Intent(activity, Mapa.class);
            startActivity(intent);

        });

        // Crie uma instância do seu adaptador
        adapter = new FavoriteLocationsAdapter();
        adapter.setOnItemClickListener(this);


        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewLocations);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .document(userId)
                .collection("FavoriteLocations")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<FavoriteLocation> favoriteLocations = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            FavoriteLocation location = document.toObject(FavoriteLocation.class);
                            location.setId(document.getId());
                            favoriteLocations.add(location);
                        }
                        updateUI(favoriteLocations);
                    } else {
                        // Tratar falha ao obter localizações favoritas do Firebase
                        Log.e("Firestore", "Error getting documents.", task.getException());
                    }
                });
        return root;
    }

    private void updateUI(List<FavoriteLocation> favoriteLocations) {
        if (favoriteLocations.isEmpty()) {
            mTextPlaces.setText("Você ainda não tem nenhum lugar favorito.");
        } else {
            mTextPlaces.setText(""); // Limpa a mensagem anterior

            // Atualiza o RecyclerView com os locais favoritos
            adapter.setFavoriteLocations(favoriteLocations);
        }
    }

    private void InicializaComponents() {
        mTextPlaces = binding.textPlaces;
        mAddFab = binding.addFab;
        mdeleteFab = binding.deleteFab;
        mAddPlaceFab = binding.addPlaceFab;
        deleteText = binding.deleteTextFab;
        addPlaceText = binding.addPlaceTextFab;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(FavoriteLocation favoriteLocation) {
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        Intent intent = new Intent(activity, Mapa.class);
        intent.putExtra("Lat",favoriteLocation.getLocationPoint().getLatitude());
        intent.putExtra("Lng",favoriteLocation.getLocationPoint().getLongitude());
        intent.putExtra("Radius",favoriteLocation.getRadius());
        intent.putExtra("Id",favoriteLocation.getId());

        startActivity(intent);
    }
}
