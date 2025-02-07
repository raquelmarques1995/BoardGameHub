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

    private DatabaseHelper db;
    private EditText editTextNome, editTextEmail;
    private ListView listViewUtilizadores;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> utilizadores;
    private int selectedId;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            //Verificar se o userId já está registado e se sim abre automaticamente a aplicação
            // Check if a user ID exists in the internal storage
//        String userId = Utils.readUserID(this);
//
//        // If a valid user ID is found (not -1), redirect to MainFrameActivity
//        if (userId != "-1") {
//            // Redirect to MainFrameActivity
//            Intent intent = new Intent(MainActivity.this, MainFrame.class);
//            intent.putExtra("user_id", userId);  // Pass the user ID to MainFrameActivity
//            startActivity(intent);
//            finish(); // Close MainActivity to prevent going back to it
//        } else {
//            // No user ID found, let the user log in (normal behavior)
//            Toast.makeText(this, "No user ID found. Please log in.", Toast.LENGTH_SHORT).show();
//        }
            //abertura automática tendo em conta user até aqui

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

//        // Botão para abrir Catálogo
//        Button linearButton = findViewById(R.id.btnCatalogo);
//        linearButton.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, LinearLayoutActivity.class);
//            startActivity(intent);
//        });
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


