package com.example.finalprojectandroid;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

@Root(name = "items", strict = false)
public interface ApiService {

    // Search for board games by name
    @GET("search")
    Call<BoardGameResponse> searchBoardGames(@Query("query") String query);

    // Fetch detailed info for a specific board game by ID
    @GET("boardgame/{id}")  // API URL: https://boardgamegeek.com/xmlapi/boardgame/{id}
    Call<BoardGameDetailsResponse> getBoardGameDetails(@Path("id") int id);

    // API for "Hot"/popular board games
    @GET("hot?boardgame")
    Call<HotBoardGameResponse> getHotBoardGames();
}

