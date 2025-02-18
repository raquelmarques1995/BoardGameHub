package com.example.finalprojectandroid.ui.games;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectandroid.BoardGameDetails;
import com.example.finalprojectandroid.DatabaseHelper;
import com.example.finalprojectandroid.R;
import com.example.finalprojectandroid.Utils;
import com.example.finalprojectandroid.BoardGame;
import com.example.finalprojectandroid.ApiService;
import com.example.finalprojectandroid.ApiClientBoardGame;
import com.example.finalprojectandroid.BoardGameDetailsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GamesFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private int userId;
    private ApiService apiService;
    private RecyclerView recyclerView;
    private BoardGameAdapterGames boardGameAdapterGames;
    private List<BoardGame> boardGameList;
    private List<Integer> gameIds;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerViewMyGames);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize database helper and ApiService
        Context context = getContext();
        if (context != null) {
            dbHelper = new DatabaseHelper(context);
            userId = Integer.parseInt(Utils.readUserID(context));

            apiService = ApiClientBoardGame.getApiClientForXmlApi().create(ApiService.class);

            // Fetch game details and populate the RecyclerView
            fetchAndLogUserGames();
        }

        return view;
    }

    private void fetchAndLogUserGames() {
        // Fetch the list of game IDs from the database
        gameIds = dbHelper.getGamesByUserId(userId);

        if (gameIds != null && !gameIds.isEmpty()) {
            boardGameList = new ArrayList<>();

            // Loop through each gameId and fetch its details
            for (Integer gameId : gameIds) {
                fetchBoardGameDetails(gameId);
            }
        } else {
            Log.e("GamesFragment", "No games found for user " + userId);
        }
    }

    private void fetchBoardGameDetails(int gameId) {
        // Fetch details for each game using its ID
        apiService.getBoardGameDetails(gameId).enqueue(new Callback<BoardGameDetailsResponse>() {
            @Override
            public void onResponse(Call<BoardGameDetailsResponse> call, Response<BoardGameDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getBoardGames() != null) {
                    // Get the details of the game from the API response
                    BoardGameDetailsResponse gameDetailsResponse = response.body();
                    if (gameDetailsResponse != null) {
                        BoardGameDetails gameDetails = gameDetailsResponse.getBoardGames().get(0);
                        BoardGame game = new BoardGame();
                        game.setId(gameId);
                        game.setDetails(gameDetails);
                        String primaryName = gameDetails.getName();
                        //Log.d("GamesFragment", "Primary Name for game ID " + gameId + ": " + primaryName);
                        boardGameList.add(game);

                        // Once all games are fetched, update the adapter and RecyclerView
                        if (boardGameList.size() == gameIds.size()) {
                            boardGameAdapterGames = new BoardGameAdapterGames(getContext(), boardGameList);
                            recyclerView.setAdapter(boardGameAdapterGames);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BoardGameDetailsResponse> call, Throwable t) {
                Log.d("GamesFragment", "Erro de API ao ir buscar detalhes para jogo com ID " + gameId + ": " + t.getMessage());
            }
        });
    }
}

