package com.example.finalprojectandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.security.SecureRandom;
import java.util.Base64;

public class userDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public userDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Register User
    public boolean registerUser(String username, String email, String password) {
        db = dbHelper.getWritableDatabase();

        // Create a random salt
        String salt = generateSalt();

        // Generate the password hash with the salt
        String hashedPassword = PasswordUtils.hashPassword(password, salt);

        // Insert into the 'users' table and get the user ID
        ContentValues userValues = new ContentValues();
        userValues.put("username", username);
        userValues.put("email", email);

        long userId = db.insert("users", null, userValues);

        if (userId == -1) {
            db.close();
            return false;
        }

        // Insert into the 'user_credentials' table
        ContentValues credentialsValues = new ContentValues();
        credentialsValues.put("id_user", userId);
        credentialsValues.put("passwordHash", hashedPassword);
        credentialsValues.put("salt", salt);

        long credentialsResult = db.insert("user_credentials", null, credentialsValues);
        db.close();

        return credentialsResult != -1;
    }

    // Method to verify the login
    public boolean loginUser(String email, String password) {
        db = dbHelper.getReadableDatabase();

        // UserID and credencials
        String query = "SELECT uc.passwordHash, uc.salt FROM user_credentials uc " +
                "INNER JOIN users u ON uc.id_user = u.id WHERE u.email = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            String storedHash = cursor.getString(0);
            String salt = cursor.getString(1);
            cursor.close();
            db.close();
            return PasswordUtils.verifyPassword(password, storedHash, salt);
        }

        cursor.close();
        db.close();
        return false;
    }

    private String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // Method to get the user ID through the email
    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE email = ?", new String[]{email});
        int userId = -1; // Default to -1 if not found
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }
        cursor.close();
        return userId;
    }
}

