package com.example.ringmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;



public class MainActivity extends AppCompatActivity {
    private TextView text_tela_cadastro;
    private TextView text_esqueceu_senha;
    private EditText edit_email, edit_senha;
    private Button btn_entrar;
    private ProgressBar progressBar;
    String[] mensagens = {"Preecha todos os campos!!!", "Login efetuado com sucesso!!!", "As senhas devem ser iguais!!!"};


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IniciarComponents();

//        ações do botão de cadastrar-se

        text_tela_cadastro.setOnTouchListener((view, motionEvent) -> {
            SublinhaTextView(R.id.text_tela_cadastro);
            return false;
        });
        text_tela_cadastro.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this, Tela_Cadastro.class);
            startActivity(intent);
        });

//        ações do botão de esqueceu a senha

        text_esqueceu_senha.setOnTouchListener((view, motionEvent) -> {
            SublinhaTextView(R.id.text_esqueceu_senha);
            return false;
        });
        text_esqueceu_senha.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this, senha_esquecida.class);
            startActivity(intent);
        });

        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();

                if(email.isEmpty() || senha.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    AutenticarUsuario(v);
                }
            }
        });
    }

    private void AutenticarUsuario(View view) {
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TelaPrincipal();
                        }
                    }, 1000);
                } else {
                    String erro;
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "Email inválido!!!";
                    } catch (Exception e) {
                        erro = "Erro ao logar usuário!!!";
                    }

                    Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioAtual != null) {
            TelaPrincipal();
        }
    }

    private void TelaPrincipal () {
        Intent intent = new Intent(MainActivity.this, tela_inicial.class);
        startActivity(intent);
        finish();
    }

    private void IniciarComponents() {
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
        text_esqueceu_senha = findViewById(R.id.text_esqueceu_senha);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_password);
        btn_entrar = findViewById(R.id.bt_entrar);
        progressBar = findViewById(R.id.PB_entrar);
    }

    private void SublinhaTextView(int ViewID) {
        TextView textview = findViewById(ViewID);
        textview.setPaintFlags(textview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

    }

}