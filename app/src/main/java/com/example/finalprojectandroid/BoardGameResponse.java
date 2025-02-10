package com.example.finalprojectandroid;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

// Wrapper class for board game search results (KEEP THIS AS IS)
@Root(name = "items", strict = false)
public class BoardGameResponse {
    @ElementList(inline = true, required = false)
    private List<BoardGame> boardGames;

    public List<BoardGame> getBoardGames() { return boardGames; }
}
