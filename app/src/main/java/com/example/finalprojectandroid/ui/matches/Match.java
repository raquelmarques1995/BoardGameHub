package com.example.finalprojectandroid.ui.matches;

public class Match {
    private int id, userId, score;
    private String gameName, matchDate, notes;

    public Match(int id, int userId, String gameName, String matchDate, int score, String notes) {
        this.id = id;
        this.userId = userId;
        this.gameName = gameName;
        this.matchDate = matchDate;
        this.score = score;
        this.notes = notes;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getGameName() { return gameName; }
    public String getMatchDate() { return matchDate; }
    public int getScore() { return score; }
    public String getNotes() { return notes; }
}