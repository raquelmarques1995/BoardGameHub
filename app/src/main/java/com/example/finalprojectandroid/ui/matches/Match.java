package com.example.finalprojectandroid.ui.matches;

import java.util.Date;

public class Match {
    private int id, userId, numberPlayers, duration, score;
    private String gameName, notes;

    private Date matchDate;

    public Match(int id, int userId, String gameName, Date matchDate, int numberPlayers, int duration, int score, String notes) {
        this.id = id;
        this.userId = userId;
        this.gameName = gameName;
        this.matchDate = matchDate;
        this.numberPlayers = numberPlayers;
        this.duration = duration;
        this.score = score;
        this.notes = notes;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getGameName() { return gameName; }
    public Date getMatchDate() { return matchDate; }
    public int getNumberPlayers() { return numberPlayers; }
    public int getDuration() { return duration; }
    public int getScore() { return score; }
    public String getNotes() { return notes; }
}