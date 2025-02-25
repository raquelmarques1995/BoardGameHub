package com.example.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private final String FILE_NAME ="userInSession.txt";
    private EditText etEmailLogin, etPasswordLogin;
    private Button btnLogin, btnCatalog;
    ;
    private userDAO userDAO;
    private TextView forgotPasswordText, RegisterText;

    private ClickableSpan clickableSpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        userDAO = new userDAO(this);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        btnCatalog = findViewById(R.id.btnCatalog);
        RegisterText = findViewById(R.id.RegisterText);

        btnLogin.setOnClickListener(v -> {
            String email = etEmailLogin.getText().toString();
            String password = etPasswordLogin.getText().toString();

            if (userDAO.loginUser(email, password)) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

                // Retrieve the user ID using the email
                int userId = userDAO.getUserIdByEmail(email);

                if (userId == -1) {
                    Toast.makeText(this, "Error: User not found.", Toast.LENGTH_SHORT).show();
                    return; // Stop execution if user ID is invalid
                }

                // Redirect to MainFrame
                Intent intent = new Intent(MainActivity.this, MainFrameActivity.class);
                saveUserId(Integer.toString(userId));
                startActivity(intent);
                finish(); // Close MainActivity
            } else {
                Toast.makeText(this, "Incorrect email or password!", Toast.LENGTH_SHORT).show();
            }
        });

        // Inflating the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_reset_password, null);

        //Getting the reference to TextInputEditText
        final TextInputEditText emailInput = dialogView.findViewById(R.id.emailInput);

        // Creating the AlertDialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Forgot Password")
                .setMessage("Enter your email to reset the password.")
                .setView(dialogView)  // Adding the custom layout
                .setCancelable(true)
                .setPositiveButton("OK", (dialogInterface, id) -> {
                    String email = emailInput.getText().toString().trim();
                    if (!email.isEmpty()) {
                        // Logic to send the recovery email TODO
                    } else {
                        Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    }
                    dialogInterface.dismiss();
                })
                .setNegativeButton("Cancel", (dialogInterface, id) -> dialogInterface.dismiss());

        forgotPasswordText.setOnClickListener(v -> dialog.show());

        //Register
        String text = "Don’t have an account? Register";
        SpannableString spannableString = new SpannableString(text);

        // Set the color and action of "Register" TODO
        clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }

            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(RegisterText.getContext(), R.color.accentColor));
                ds.setUnderlineText(true);
            }
        };

        // Applies the style only to the word "Register"
        spannableString.setSpan(clickableSpan, text.indexOf("Register"), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Sets the text in the TextView
        RegisterText.setText(spannableString);
        RegisterText.setMovementMethod(LinkMovementMethod.getInstance());

        //Catalog button
        btnCatalog.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CatalogueActivity.class);
            startActivity(intent);
       });

    }
    private void saveUserId(String text) {
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Login","Error saving login details!");
        }
        File directory = getFilesDir();
        Log.d("Internal directory path:",directory.getAbsolutePath());
    }
}