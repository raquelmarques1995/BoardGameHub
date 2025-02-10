package com.example.finalprojectandroid;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private final String FILE_NAME = "userInSession.txt";


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Check if a login already exists
            if (isUserLoggedIn()) {
                String userId = Utils.readUserID(this);
                if (userId != null) {
                    //If there is an existing login, redirect to the MainFrameActivity
                    Intent intent = new Intent(MainActivity.this, MainFrameActivity.class);
                    intent.putExtra("user_id", userId);
                    startActivity(intent);
                    finish();
                    return;
                }
            }

            // Button Login
            Button linearButton = findViewById(R.id.btnLogin);
            linearButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            });

            // Button Criar Conta
            Button criarConta = findViewById(R.id.btnCriarConta);
            criarConta.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            });

        // Button Catálogo
        Button catalogue = findViewById(R.id.btnCatalogo);
            catalogue.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CatalogueActivity.class);
            startActivity(intent);
        });
        }


    private boolean isUserLoggedIn() {
        File file = new File(getFilesDir(), FILE_NAME);
        return file.exists();
    }

    }


