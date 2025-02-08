package com.example.finalprojectandroid;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CreateAccountActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etUsername, etEmail, etPassword, etPasswordConfirm; // Novo campo
    private Button btnRegistrar;
    private userDAO userDAO;
    private ListView listViewUtilizadores;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listaUtilizadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createacount);

        dbHelper = new DatabaseHelper(this);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etRepeatPassword); // Inicialize o campo de confirmação de senha
        btnRegistrar = findViewById(R.id.btnRegistrar);
        userDAO = new userDAO(this);
        listViewUtilizadores = findViewById(R.id.listViewUtilizadores);

        listaUtilizadores = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaUtilizadores);
        listViewUtilizadores.setAdapter(adapter);

        listUsers();

        btnRegistrar.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String confirmPassword = etPasswordConfirm.getText().toString(); // Pega o valor de confirmar senha

            // Verifica se a senha e a confirmação coincidem
            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
                return; // Impede o registro se as senhas não coincidirem
            }

            // Criação da conta
            if (userDAO.registerUser(username, email, password)) {
                Toast.makeText(this, "Conta criada!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao criar conta!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listUsers() {
        listaUtilizadores.clear();
        Cursor cursor = dbHelper.listUsers();

        if (cursor.moveToFirst()) {
            do {
                String nome = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

                listaUtilizadores.add(nome + " - " + email);
            } while (cursor.moveToNext());
        }

        cursor.close();
        adapter.notifyDataSetChanged();
    }
}


