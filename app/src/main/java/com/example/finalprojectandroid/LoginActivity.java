package com.example.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;

public class LoginActivity extends AppCompatActivity {

    // TODO alterar nome de filename "user_id_logado" para "userinsession"
    private final String FILE_NAME ="userInSession.txt";
    private EditText etEmailLogin, etPasswordLogin;
    private Button btnLogin;
    private userDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        userDAO = new userDAO(this);

//        btnLogin.setOnClickListener(v -> {
//            String email = etEmailLogin.getText().toString();
//            String password = etPasswordLogin.getText().toString();
//
//            if (userDAO.loginUser(email, password)) {
//                Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
//                // Redirecionar para a tela principal da app
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                finish();
//            } else {
//                Toast.makeText(this, "Email ou password incorretos!", Toast.LENGTH_SHORT).show();
//            }
//        });

        btnLogin.setOnClickListener(v -> {
            String email = etEmailLogin.getText().toString();
            String password = etPasswordLogin.getText().toString();

            if (userDAO.loginUser(email, password)) {
                Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();

                // Retrieve the user ID using the email
                int userId = userDAO.getUserIdByEmail(email);
                System.out.println("DEBUG: User ID Retrieved = " + userId);

                if (userId == -1) {
                    Toast.makeText(this, "Erro: User ID not found", Toast.LENGTH_SHORT).show();
                    return; // Stop execution if user ID is invalid
                }

                // Redirect to MainFrame
                Intent intent = new Intent(LoginActivity.this, MainFrameActivity.class);
                //LOGIN PERSISTENTE.
                saveUserId(Integer.toString(userId));
                startActivity(intent);
                finish(); // Close LoginActivity
            } else {
                Toast.makeText(this, "Email ou password incorretos!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserId(String texto) {
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(texto.getBytes());
            fos.close();
            Log.e("DEBUG: String com userid",texto);
            Toast.makeText(this, "Dados de login guardados com sucesso!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DEBUG ERRO: String com userid",texto);
            Toast.makeText(this, "Erro ao guardar os dados de login!", Toast.LENGTH_SHORT).show();
        }
        File directory = getFilesDir();
        Log.d("Caminho do diretório interno:",directory.getAbsolutePath());
    }
}