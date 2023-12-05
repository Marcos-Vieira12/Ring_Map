package com.example.ringmap.ui.friends;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsFragment extends Fragment implements FriendAdapter.OnItemClickListener{

    private FragmentFriendsBinding binding;

    private FriendAdapter adapter;

    FloatingActionButton mAddFab;

    // These are taken to make visible and invisible along with FABs
    TextView mTextPlaces;
    EditText textEmail;

    // to check whether sub FAB buttons are visible or not.
    Boolean isAllFabsVisible;

    private RecyclerView recyclerViewAmigos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        InicializaComponents();

        textEmail.setVisibility(View.GONE);
        isAllFabsVisible = false;

        mAddFab.setOnClickListener( view -> {

            if (!isAllFabsVisible) {
                // when isAllFabsVisible becomes true make all
                // the action name texts and FABs VISIBLE
                textEmail.setVisibility(View.VISIBLE);
                // make the boolean variable true as we
                // have set the sub FABs visibility to GONE
                isAllFabsVisible = true;
            } else {
                // when isAllFabsVisible becomes true make
                // all the action name texts and FABs GONE.
                textEmail.setVisibility(View.GONE);

                // make the boolean variable false as we
                // have set the sub FABs visibility to GONE
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String userEmail = textEmail.getText().toString(); // Supondo que text_email seja o TextView que contém o email
                DocumentReference ref = FirebaseFirestore.getInstance().collection("usuarios").document(userEmail);
                ref.get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    // O documento existe
                                    Map<String, Object> Update = new HashMap<>();
                                    Update.put("amigos", FieldValue.arrayUnion(userEmail));
                                    FirebaseFirestore.getInstance().collection("usuarios")
                                            .document(auth.getCurrentUser().getUid())
                                            .update(Update);
                                } else {
                                    // O documento não existe
                                    Log.d("TAG", "Documento não encontrado.");
                                }
                            } else {
                                // Tratar falha na leitura do documento
                                Log.e("TAG", "Erro ao obter documento", task.getException());
                            }
                        });


                isAllFabsVisible = false;
            }
        });
        mTextPlaces.setText("você ainda não tem nenhum amigo :(");

        adapter = new FriendAdapter();
        adapter.setOnItemClickListener(this);

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
        mAddFab = binding.addFriendFab;
        textEmail = binding.editEmail;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(String amigoId) {
        Toast.makeText((AppCompatActivity) requireActivity(), "clicou", Toast.LENGTH_SHORT).show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("amigos", FieldValue.arrayRemove(amigoId));

        db.collection("usuarios")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update(updateData);

        db.collection("usuarios")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Obtém o array de IDs dos amigos do campo "Amigos"
                            List<String> amigosIds = (ArrayList<String>) document.get("amigos");
                            updateUI(amigosIds);
                        }


                    }
                });

    }
}