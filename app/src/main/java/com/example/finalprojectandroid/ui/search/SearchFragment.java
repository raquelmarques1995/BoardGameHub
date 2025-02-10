package com.example.finalprojectandroid.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectandroid.ApiClient;
import com.example.finalprojectandroid.ApiClientBoardGame;
import com.example.finalprojectandroid.ApiClientHot;
import com.example.finalprojectandroid.ApiService;
import com.example.finalprojectandroid.BoardGame;
import com.example.finalprojectandroid.BoardGameAdapterLoggedIn;
import com.example.finalprojectandroid.R;
import com.example.finalprojectandroid.HotBoardGameResponse;
import com.example.finalprojectandroid.BoardGameResponse;
import com.example.finalprojectandroid.BoardGameDetailsResponse;
import com.example.finalprojectandroid.BoardGameDetails;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private BoardGameAdapterLoggedIn boardGameAdapterLoggedIn;
    private EditText searchEditText;
    private Button searchButton;
    private ApiService apiService;  // To make API calls
    private List<BoardGame> boardGameList = new ArrayList<>();

    public SearchFragment() {
        // Empty constructor required for fragments
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the ApiService with the correct Retrofit instance for Hot Board Games
        apiService = ApiClientHot.getApiClientForHotApi().create(ApiService.class);

        // Default fetch of hot board games on startup
        fetchHotBoardGames();

        // Button click to search for board games based on user input
        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (!query.isEmpty()) {
                fetchBoardGames(query);
            }
        });

        return view;
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
                    Toast.makeText(getContext(), "No hot board games found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HotBoardGameResponse> call, Throwable t) {
                Toast.makeText(getContext(), "API error: " + t.getMessage(), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getContext(), "No results found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BoardGameResponse> call, Throwable t) {
                Toast.makeText(getContext(), "API error: " + t.getMessage(), Toast.LENGTH_LONG).show();
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
                        boardGameAdapterLoggedIn = new BoardGameAdapterLoggedIn(getContext(), boardGameList);
                        recyclerView.setAdapter(boardGameAdapterLoggedIn);
                    }
                }

                @Override
                public void onFailure(Call<BoardGameDetailsResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Failed to load details", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
