
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

import com.example.finalprojectandroid.DatabaseHelper;
import com.example.finalprojectandroid.R;
import com.example.finalprojectandroid.Utils;
import com.example.finalprojectandroid.BoardGameDetails;
import com.example.finalprojectandroid.ApiService;
import com.example.finalprojectandroid.ApiClientBoardGame;  // Make sure you import your ApiClient to get an instance of ApiService
import com.example.finalprojectandroid.BoardGameDetailsResponse;
import com.example.finalprojectandroid.BoardGameDetails;
import com.example.finalprojectandroid.BoardGameName;



import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GamesFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private int userId;
    private ApiService apiService;  // Declare the ApiService instance

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        // Get user ID (assuming you're storing the user ID in SharedPreferences or other method)
        Context context = getContext();
        if (context != null) {
            dbHelper = new DatabaseHelper(context);
            userId = Integer.parseInt(Utils.readUserID(context)); // Assuming user ID is stored in SharedPreferences

            // Initialize ApiService with Retrofit instance
            apiService = ApiClientBoardGame.getApiClientForXmlApi().create(ApiService.class); // Update with your ApiClient instance

            // Fetch games for the user and log them
            fetchAndLogUserGames();
        }

        return view;
    }

    // Method to fetch games by user ID and log the result
    private void fetchAndLogUserGames() {
        // Assuming getGamesByUserId returns a list of game IDs (List<Integer>)
        List<Integer> gameIds = dbHelper.getGamesByUserId(userId);

        // Log the result
        if (gameIds != null && !gameIds.isEmpty()) {
            Log.d("GamesFragment", "Games for user " + userId + ": " + gameIds.toString());
            for (Integer gameId : gameIds) {
                // Fetch the BoardGameDetails by gameId here (using Retrofit, for example)
                fetchBoardGameDetails(gameId);
            }
        } else {
            Log.d("GamesFragment", "No games found for user " + userId);
        }
    }

    // Fetch BoardGameDetails by gameId (just an example, assuming you have the Retrofit setup)
    private void fetchBoardGameDetails(int gameId) {
        // Ensure you're using the right ApiService for game details
        ApiService apiServiceForDetails = ApiClientBoardGame.getApiClientForXmlApi().create(ApiService.class);

        apiServiceForDetails.getBoardGameDetails(gameId).enqueue(new Callback<BoardGameDetailsResponse>() {
            @Override
            public void onResponse(Call<BoardGameDetailsResponse> call, Response<BoardGameDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Now you get the correct response type (BoardGameDetailsResponse)
                    BoardGameDetailsResponse gameDetailsResponse = response.body();

                    // Handle the details from the response
                    if (gameDetailsResponse != null) {
                        BoardGameDetails gameDetails = gameDetailsResponse.getBoardGames().get(0); // Adjust according to your structure
                        //String primaryName = gameDetails.getName();  // Use the getName() method from BoardGameDetails
                        String primaryName = gameDetails.getName();
                        Log.d("GamesFragment", "Primary Name for game ID " + gameId + ": " + primaryName);
                    } else {
                        Log.d("GamesFragment", "No details found for game ID " + gameId);
                    }
                }
            }

            @Override
            public void onFailure(Call<BoardGameDetailsResponse> call, Throwable t) {
                Log.d("GamesFragment", "API error while fetching details for game ID " + gameId + ": " + t.getMessage());
            }
        });
    }
}
