package com.example.finalprojectandroid;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

// Wrapper class for hot board game results
@Root(name = "items", strict = false)
public class HotBoardGameResponse {
    @ElementList(inline = true, required = false)
    private List<BoardGame> hotBoardGames;

    public List<BoardGame> getHotBoardGames() { return hotBoardGames; }
}
