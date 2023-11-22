package com.example.ringmap.ui.friends;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ringmap.R;
import com.example.ringmap.databinding.FragmentFriendsBinding;
import com.example.ringmap.ui.places.FavoriteLocation;
import com.example.ringmap.ui.places.FavoriteLocationsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding binding;

    private FriendAdapter adapter;

    FloatingActionButton mAddFab, mdeleteFab, mAddPlaceFab, mSeeRequests;

    // These are taken to make visible and invisible along with FABs
    TextView deleteText, addPlaceText, mTextPlaces, mTextSeeRequests;

    // to check whether sub FAB buttons are visible or not.
    Boolean isAllFabsVisible;

    private RecyclerView recyclerViewAmigos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        InicializaComponents();

        mdeleteFab.setVisibility(View.GONE);
        mAddPlaceFab.setVisibility(View.GONE);
        mSeeRequests.setVisibility(View.GONE);
        deleteText.setVisibility(View.GONE);
        addPlaceText.setVisibility(View.GONE);
        mTextSeeRequests.setVisibility(View.GONE);

        isAllFabsVisible = false;

        mAddFab.setOnClickListener( view -> {

            if (!isAllFabsVisible) {
                // when isAllFabsVisible becomes true make all
                // the action name texts and FABs VISIBLE
                mdeleteFab.show();
                mAddPlaceFab.show();
                mSeeRequests.show();
                deleteText.setVisibility(View.VISIBLE);
                addPlaceText.setVisibility(View.VISIBLE);
                mTextSeeRequests.setVisibility(View.VISIBLE);

                // make the boolean variable true as we
                // have set the sub FABs visibility to GONE
                isAllFabsVisible = true;
            } else {
                // when isAllFabsVisible becomes true make
                // all the action name texts and FABs GONE.
                mdeleteFab.hide();
                mAddPlaceFab.hide();
                mSeeRequests.hide();
                deleteText.setVisibility(View.GONE);
                addPlaceText.setVisibility(View.GONE);
                mTextSeeRequests.setVisibility(View.GONE);

                // make the boolean variable false as we
                // have set the sub FABs visibility to GONE
                isAllFabsVisible = false;
            }
        });
        mTextPlaces.setText("você ainda não tem nenhum amigo :(");

        adapter = new FriendAdapter();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewFriends);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("usuarios")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Obtém o array de IDs dos amigos do campo "Amigos"
                            List<String> amigosIds = (ArrayList<String>) document.get("amigos");

                            if (amigosIds != null && !amigosIds.isEmpty()) {
                                // Aqui, você pode trabalhar diretamente com os IDs dos amigos
                                // Por exemplo, você pode exibi-los em um log
                                updateUI(amigosIds);
                            } else {
                                mTextPlaces.setText("Lista de amigos vazia!!!");
                            }
                        } else {
                            mTextPlaces.setText("Documento não existe ou é nulo!!!");
                        }
                    } else {
                        // Tratar falha ao obter dados do Firestore
                        mTextPlaces.setText("Erro ao conectar com o banco!!!");
                    }
                });

        return root;
    }

    private void updateUI(List<String> friends) {
        if (friends.isEmpty()) {
            mTextPlaces.setText("Você ainda não tem nenhum lugar favorito.");
        } else {
            mTextPlaces.setText(""); // Limpa a mensagem anterior

            // Atualiza o RecyclerView com os locais favoritos
            adapter.setFriendAdapters(friends);
        }
    }

    private void InicializaComponents() {
        mTextPlaces = binding.textFriends;
        mAddFab = binding.addFab;
        mdeleteFab = binding.deleteFab;
        mAddPlaceFab = binding.addPlaceFab;
        deleteText = binding.deleteTextFab;
        addPlaceText = binding.addPlaceTextFab;
        mSeeRequests = binding.seeRequestsFab;
        mTextSeeRequests = binding.seeRequestsTextFab;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}