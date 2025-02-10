package com.example.finalprojectandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogueActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BoardGameAdapter boardGameAdapter;
    private EditText searchEditText;
    private Button searchButton;
    private ApiService apiService;  // To make API calls
    private List<BoardGame> boardGameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the ApiService with the correct Retrofit instance for Hot Board Games
        apiService = ApiClientHot.getApiClientForHotApi().create(ApiService.class);

        // Default fetch of hot board games on startup
        try {
            fetchHotBoardGames(); // Fetch hot board games on startup
            Toast.makeText(CatalogueActivity.this, "Lista de jogos populares carregada", Toast.LENGTH_SHORT).show();

        }catch (Exception e) {
            e.printStackTrace(); // Log do erro para facilitar debugging
        }

        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (!query.isEmpty()) {
                fetchBoardGames(query);
            }
        });
    }

    // Fetch hot board games (for example, the popular or trending games)
    private void fetchHotBoardGames() {
        apiService.getHotBoardGames().enqueue(new Callback<HotBoardGameResponse>() {
            @Override
            public void onResponse(Call<HotBoardGameResponse> call, Response<HotBoardGameResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boardGameList = response.body().getHotBoardGames();
                    fetchDetailsForAllGames();  // Fetch details for the hot games
                } else {
                    Toast.makeText(CatalogueActivity.this, "No hot board games found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HotBoardGameResponse> call, Throwable t) {
                Toast.makeText(CatalogueActivity.this, "API error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Fetch board games based on a search query
    private void fetchBoardGames(String query) {
        // Initialize the ApiService for searching board games
        apiService = ApiClient.getApiClientForXmlApi2().create(ApiService.class);

        apiService.searchBoardGames(query).enqueue(new Callback<BoardGameResponse>() {
            @Override
            public void onResponse(Call<BoardGameResponse> call, Response<BoardGameResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boardGameList = response.body().getBoardGames();
                    fetchDetailsForAllGames();  // Fetch details for all found games
                } else {
                    Toast.makeText(CatalogueActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BoardGameResponse> call, Throwable t) {
                Toast.makeText(CatalogueActivity.this, "API error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Fetch details for all the board games retrieved from the search or hot list
    private void fetchDetailsForAllGames() {
        // Switch ApiClient to use the XMLAPI base URL for fetching game details
        ApiService apiServiceForDetails = ApiClientBoardGame.getApiClientForXmlApi().create(ApiService.class);

        for (BoardGame game : boardGameList) {
            apiServiceForDetails.getBoardGameDetails(game.getId()).enqueue(new Callback<BoardGameDetailsResponse>() {
                @Override
                public void onResponse(Call<BoardGameDetailsResponse> call, Response<BoardGameDetailsResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        BoardGameDetails details = response.body().getBoardGames().get(0);
                        game.setDetails(details); // Set the details to the BoardGame object

                        // Update RecyclerView when all details are loaded
                        boardGameAdapter = new BoardGameAdapter(CatalogueActivity.this, boardGameList);
                        recyclerView.setAdapter(boardGameAdapter);
                    }
                }

                @Override
                public void onFailure(Call<BoardGameDetailsResponse> call, Throwable t) {
                    Toast.makeText(CatalogueActivity.this, "Failed to load details", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
