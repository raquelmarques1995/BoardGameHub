package com.example.finalprojectandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.finalprojectandroid.ui.matches.Match;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "boardgames.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabela de utilizadores (dados gerais)
        String CREATE_USER_TABLE = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "email TEXT UNIQUE NOT NULL," +
                "name TEXT," +
                "birthdate DATE," +
                "city TEXT," +
                "country TEXT" +
                ")";

        // Tabela de credenciais (hash da password e salt)
        String CREATE_CREDENTIALS_TABLE = "CREATE TABLE user_credentials (" +
                "id_user INTEGER PRIMARY KEY, " +
                "passwordHash TEXT NOT NULL, " +
                "salt TEXT NOT NULL, " +
                "FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE CASCADE)";


        //Tabela das matches
        String CREATE_MATCHES_TABLE = "CREATE TABLE matches (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "game_name TEXT NOT NULL, " + //este campo deverá passar para uma dropdown list que vem da lista Os meus jogos
                "match_date DATE NOT NULL, " +
                "score INTEGER, " +
                "notes TEXT, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ")";


        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CREDENTIALS_TABLE);
        db.execSQL(CREATE_MATCHES_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE users ADD COLUMN name TEXT");
            db.execSQL("ALTER TABLE users ADD COLUMN birthdate TEXT");
            db.execSQL("ALTER TABLE users ADD COLUMN city TEXT");
            db.execSQL("ALTER TABLE users ADD COLUMN country TEXT");
        }
    }

    public Cursor listarUtilizadores() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users",null);
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"id", "username", "email", "name", "birthdate", "city", "country"},
                "id=?", new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6)
            );
            cursor.close();
            db.close();
            return user;
        }

        db.close();
        return null; // Retorna null se o user não for encontrado
    }

    public void updateUser(int userId, String name, String birthdate, String city, String country) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("birthdate", birthdate);
        values.put("city", city);
        values.put("country", country);

        db.update("users", values, "id = ?", new String[]{String.valueOf(userId)});
        db.close();
    }

    public boolean insertMatch(String userId, String gameName, String matchDate, int score, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user_id", userId);
        values.put("game_name", gameName);
        values.put("match_date", matchDate);
        values.put("score", score);
        values.put("notes", notes);

        long result = db.insert("matches", null, values);
        db.close();

        return result != -1; // Retorna true se a inserção foi bem-sucedida
    }


    public ArrayList<Match> getMatchesByUserId(int userId) {
        ArrayList<Match> matchList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM matches WHERE user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Match match = new Match(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getString(5)
                );
                matchList.add(match);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return matchList;
    }









}

