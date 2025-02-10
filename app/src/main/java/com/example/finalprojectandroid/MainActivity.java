package com.example.finalprojectandroid;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private final String FILE_NAME = "userInSession.txt";


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Verificar se já existe um login
            if (isUserLoggedIn()) {
                String userId = Utils.readUserID(this);
                if (userId != null) {
                    // Se houver um login feito, redireciona para a MainFrameActivity
                    Intent intent = new Intent(MainActivity.this, MainFrameActivity.class);
                    intent.putExtra("user_id", userId);  // Passa o userId para a MainFrameActivity
                    startActivity(intent);
                    finish();  // Fecha a MainActivity para não permitir voltar a ela
                    return;  // Sai da execução para evitar a exibição dos botões
                }
            }

            // Botão para abrir Login
            Button linearButton = findViewById(R.id.btnLogin);
            linearButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            });

            // Botão para abrir Criar Conta
            Button criarConta = findViewById(R.id.btnCriarConta);
            criarConta.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            });

        // Botão para abrir Catálogo
        Button catalogue = findViewById(R.id.btnCatalogo);
            catalogue.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CatalogueActivity.class);
            startActivity(intent);
        });
        }

    // Função para verificar se o utilizador já está logado
    private boolean isUserLoggedIn() {
        File file = new File(getFilesDir(), FILE_NAME);
        return file.exists();
    }

    public void dropDatabase(View view) {
        File dbFile = getApplicationContext().getDatabasePath("appDB.db");
        if (dbFile.exists()) {
            boolean deleted = dbFile.delete();
            if (deleted) {
                Toast.makeText(this, "Base de dados apagada!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao apagar a base de dados.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Base de dados não encontrada.", Toast.LENGTH_SHORT).show();
        }
    }
    }


