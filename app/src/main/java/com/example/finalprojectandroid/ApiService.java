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

    // 1️⃣ Search for board games (existing method, keep it as is)
    @GET("search")
    Call<BoardGameResponse> searchBoardGames(@Query("query") String query);

    // 2️⃣ Fetch detailed info for a specific board game by ID (NEW METHOD)
    @GET("boardgame/{id}")  // API URL: https://boardgamegeek.com/xmlapi/boardgame/{id}
    Call<BoardGameDetailsResponse> getBoardGameDetails(@Path("id") int id);

    // New API for "Hot" board games
    @GET("hot?boardgame")  // Example: https://example.com/api/hot
    Call<HotBoardGameResponse> getHotBoardGames();
}

