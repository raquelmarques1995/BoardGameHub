package com.example.finalprojectandroid;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "boardgames", strict = false)
public class BoardGameDetailsResponse {

    @ElementList(entry = "boardgame", inline = true, required = false)
    private List<BoardGameDetails> boardGames;

    public List<BoardGameDetails> getBoardGames() {
        return boardGames;
    }
}
