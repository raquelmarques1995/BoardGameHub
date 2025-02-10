package com.example.finalprojectandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.finalprojectandroid.ui.matches.Match;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "boardgames.db";
    private static final int DATABASE_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "onCreate called");
        String CREATE_USER_TABLE = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "email TEXT UNIQUE NOT NULL," +
                "name TEXT," +
                "birthdate TEXT," +
                "city TEXT," +
                "country TEXT" +
                ")";


        String CREATE_CREDENTIALS_TABLE = "CREATE TABLE user_credentials (" +
                "id_user INTEGER PRIMARY KEY, " +
                "passwordHash TEXT NOT NULL, " +
                "salt TEXT NOT NULL, " +
                "FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE CASCADE)";



        String CREATE_MATCHES_TABLE = "CREATE TABLE matches (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "game_name TEXT NOT NULL, " +
                "match_date TEXT NOT NULL, " +
                "number_players INTEGER," +
                "duration INTEGER," +
                "score INTEGER, " +
                "notes TEXT, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ")";



        String CREATE_MYGAMES_TABLE = "CREATE TABLE mygames (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "game_id INTEGER NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (game_id) REFERENCES matches(id) ON DELETE CASCADE" +
                ")";


        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CREDENTIALS_TABLE);
        db.execSQL(CREATE_MATCHES_TABLE);
        db.execSQL(CREATE_MYGAMES_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS users");
            db.execSQL("DROP TABLE IF EXISTS user_credentials");
            db.execSQL("DROP TABLE IF EXISTS matches");
            onCreate(db);
        }

        if (oldVersion < 3) { // New upgrade logic for mygames table
            String CREATE_MYGAMES_TABLE = "CREATE TABLE mygames (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, " +
                    "game_id INTEGER NOT NULL, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (game_id) REFERENCES matches(id) ON DELETE CASCADE" +
                    ")";
            db.execSQL(CREATE_MYGAMES_TABLE);
        }
    }

    public Cursor listUsers() {
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
        return null;
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

    public boolean insertMatch(String userId, String gameName, String matchDate, int numberPlayers, int duration, int score, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user_id", userId);
        values.put("game_name", gameName);
        values.put("match_date", matchDate);
        values.put("number_players", numberPlayers);
        values.put("duration",duration);
        values.put("score", score);
        values.put("notes", notes);

        long result = db.insert("matches", null, values);
        db.close();

        return result != -1;
    }


    public ArrayList<Match> getMatchesByUserId(String userId) {
        ArrayList<Match> matchList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM matches WHERE user_id = ?", new String[]{userId});

        if (cursor != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            if (cursor.moveToFirst()) {
                do {
                    Date matchDate = null;
                    String dateStr = cursor.getString(cursor.getColumnIndexOrThrow("match_date"));

                    if (dateStr != null && !dateStr.isEmpty()) {
                        try {
                            matchDate = dateFormat.parse(dateStr);
                            Log.d("Database", "Data convertida: " + matchDate);
                        } catch (ParseException e) {
                            Log.e("Database", "Erro ao converter data: " + dateStr, e);
                        }
                    } else {
                        Log.e("Database", "Data vazia ou null para a partida com ID: " + cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    }

                    Match match = new Match(
                            cursor.getInt(cursor.getColumnIndexOrThrow("id")),               // ID da partida
                            cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),          // ID do usuário
                            cursor.getString(cursor.getColumnIndexOrThrow("game_name")),     // Nome do jogo
                            matchDate,                                                       // Data da partida convertida
                            cursor.getInt(cursor.getColumnIndexOrThrow("number_players")),   // Número de jogadores
                            cursor.getInt(cursor.getColumnIndexOrThrow("duration")),         // Duração da partida
                            cursor.getInt(cursor.getColumnIndexOrThrow("score")),            // Pontuação
                            cursor.getString(cursor.getColumnIndexOrThrow("notes"))          // Notas
                    );

                    matchList.add(match);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        db.close();
        return matchList;
    }


    public Match getMatchById(int matchId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Match match = null;

        Cursor cursor = db.rawQuery("SELECT * FROM matches WHERE id = ?", new String[]{String.valueOf(matchId)});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); // Ajuste para o formato correto
                Date matchDate = null;

                String dateStr = cursor.getString(cursor.getColumnIndexOrThrow("match_date"));
                Log.e("DatabaseHelper", "match_date lido do banco: " + dateStr);

                if (dateStr != null && !dateStr.isEmpty()) {
                    try {
                        matchDate = dateFormat.parse(dateStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.e("DatabaseHelper", "Erro ao converter data: " + dateStr);
                    }
                } else {
                    Log.e("DatabaseHelper", "match_date é NULL no banco para matchId: " + matchId);
                    matchDate = new Date(); // Define a data atual como padrão
                }

                match = new Match(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("game_name")),
                        matchDate,
                        cursor.getInt(cursor.getColumnIndexOrThrow("number_players")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("duration")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("score")),
                        cursor.getString(cursor.getColumnIndexOrThrow("notes"))
                );
            }
            cursor.close();
        }

        db.close();
        return match;
    }

    public boolean updateMatch(int matchId, String gameName, String matchDate, int numberPlayers, int duration, int score, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("game_name", gameName);
        values.put("match_date", matchDate);
        values.put("number_players", numberPlayers != 0 ? numberPlayers : null);
        values.put("duration", duration != 0 ? duration : null);
        values.put("score", score != 0 ? score : null);
        values.put("notes", notes != null ? notes : null);

        int rowsUpdated = db.update("matches", values, "id = ?", new String[]{String.valueOf(matchId)});
        db.close();

        return rowsUpdated > 0;
    }


    public boolean deleteMatch(int matchId) {
        SQLiteDatabase db = null;
        boolean success = false;
        try {
            db = this.getWritableDatabase();
            int rowsDeleted = db.delete("matches", "id = ?", new String[]{String.valueOf(matchId)});
            success = rowsDeleted > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return success;
    }

    public int insertGameToMyGames(int userId, int gameId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Check if the game is already in the user's list
        Cursor cursor = db.rawQuery("SELECT * FROM mygames WHERE user_id = ? AND game_id = ?",
                new String[]{String.valueOf(userId), String.valueOf(gameId)});

        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return -1;  // Game already exists in MyGames
        }

        cursor.close();
        db.close();

        // If game is not in the list, insert it
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("game_id", gameId);

        long result = db.insert("mygames", null, values);
        db.close();

        return result != -1 ? 1 : 0;  // Return 1 if success, 0 if failure
    }


    public List<Integer> getGamesByUserId(int userId) {
        List<Integer> gameIds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT game_id FROM mygames WHERE user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                gameIds.add(cursor.getInt(cursor.getColumnIndexOrThrow("game_id")));
            }
            cursor.close();
        }

        db.close();
        return gameIds;
    }

    public List<Integer> getUsersByGameId(int gameId) {
        List<Integer> userIds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT user_id FROM mygames WHERE game_id = ?", new String[]{String.valueOf(gameId)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                userIds.add(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
            }
            cursor.close();
        }

        db.close();
        return userIds;
    }

    // Method to delete a specific game-user entry (from the 'mygames' table)
    public boolean deleteGameFromMyGames(int userId, int gameId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete("mygames",
                "user_id = ? AND game_id = ?",
                new String[]{String.valueOf(userId), String.valueOf(gameId)});

        db.close();

        return rowsDeleted > 0;
    }
}

