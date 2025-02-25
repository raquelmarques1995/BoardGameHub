package com.example.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.style.ClickableSpan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class CreateAccountActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etPasswordConfirm;
    private Button btnCreateAccount;
    private userDAO userDAO;
    private TextView LoginText;
    private ClickableSpan clickableSpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createacount);


        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etRepeatPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        userDAO = new userDAO(this);
        LoginText = findViewById(R.id.LoginText);

        btnCreateAccount.setOnClickListener(v -> {
            if (validateFields()) {
                // User creation
                String username = etUsername.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etPasswordConfirm.getText().toString();

                //User creation logic - call registerUser in userDAO
                if (userDAO.registerUser(username, email, password)) {
                    // AlertDialog Creation
                    new AlertDialog.Builder(CreateAccountActivity.this)
                            .setTitle("Account Created")
                            .setMessage("Account created successfully!")
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialogInterface, id) -> {
                                // Redirect
                                Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();  // Close the CreateAccountActivity
                            })
                            .show();
                } else {
                    Toast.makeText(this, "Error creating account!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Return to Login
        String text = "Already have an account? Login";
        SpannableString spannableString = new SpannableString(text);

        // Set the color and action of "Login" TODO
        clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                startActivity(intent);
            }

            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(LoginText.getContext(), R.color.accentColor));
                ds.setUnderlineText(true);
            }
        };

        // Applies the style only to the word "Register"
        spannableString.setSpan(clickableSpan, text.indexOf("Login"), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Sets the text in the TextView
        LoginText.setText(spannableString);
        LoginText.setMovementMethod(LinkMovementMethod.getInstance());
    }
    // Valid fields
    private boolean validateFields() {
        boolean isValid = true;

        // Check if username is empty
        if (etUsername.getText().toString().trim().isEmpty()) {
            etUsername.setError("Username is required");
            isValid = false;
        }

        // Check if email is empty or poorly formatted
        String email = etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            isValid = false;
        }

        // Check if password is empty
        if (etPassword.getText().toString().trim().isEmpty()) {
            etPassword.setError("Password is required");
            isValid = false;
        }

        // Check if confirm password is empty
        if (etPasswordConfirm.getText().toString().trim().isEmpty()) {
            etPasswordConfirm.setError("Confirm password is required");
            isValid = false;
        }

        // Check if password confirmation is equal to password
        if (!etPassword.getText().toString().trim().equals(etPasswordConfirm.getText().toString().trim())) {
            etPasswordConfirm.setError("Passwords do not match");
            isValid = false;
        }

        return isValid; // Returns true if all fields are valid
    }
}


