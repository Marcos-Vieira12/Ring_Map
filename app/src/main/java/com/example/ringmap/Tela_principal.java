package com.example.ringmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Tela_principal extends AppCompatActivity {
    private Button btn_text, btn_mapa, btn_inicial;
    private TextView text_email, text_user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        IniciarComponents();
        btn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Tela_principal.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_mapa.setOnClickListener(view -> {
            Intent intent = new Intent(Tela_principal.this, Mapa.class);
            startActivity(intent);
        });

        btn_inicial.setOnClickListener(view -> {
            Intent intent = new Intent(Tela_principal.this, tela_inicial.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("usuarios").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null) {
                    text_user.setText(documentSnapshot.getString("nome"));
                    text_email.setText(email);
                }
            }
        });
    }



    private void IniciarComponents() {
        btn_mapa = findViewById(R.id.bt_mapa);
        btn_text = findViewById(R.id.bt_deslogar);
        btn_inicial = findViewById(R.id.bt_inicial);
        text_email = findViewById(R.id.text_email);
        text_user = findViewById(R.id.text_user);
    }
}
