package com.example.finalprojectandroid.ui.games;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalprojectandroid.BoardGame;
import com.example.finalprojectandroid.DatabaseHelper;
import com.example.finalprojectandroid.R;
import com.example.finalprojectandroid.Utils;

import java.util.List;


public class BoardGameAdapterGames extends RecyclerView.Adapter<BoardGameAdapterGames.BoardGameViewHolder> {

    private Context context;
    private List<BoardGame> boardGameList;
    private DatabaseHelper dbHelper;

    private int userId;



    public BoardGameAdapterGames(Context context, List<BoardGame> boardGameList) {
        this.context = context;
        this.boardGameList = boardGameList;
    }

    @NonNull
    @Override
    public BoardGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_boardgame_games, parent, false);
        return new BoardGameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardGameViewHolder holder, int position) {
        BoardGame game = boardGameList.get(position);

        // Set game name and year
        holder.nameTextView.setText(game.getDetails().getName());
        holder.yearTextView.setText("Year: " + game.getDetails().getYearPublished());

        // Load image with Glide
        if (game.getDetails().getImageUrl() != null) {
            Glide.with(context)
                    .load(game.getDetails().getImageUrl())
                    .placeholder(R.drawable.ic_imageloading)
                    .error(R.drawable.ic_boardgame_background)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_boardgame_background);
        }

        // Handle item click to navigate to BoardGameDetailsFragment
        holder.itemView.setOnClickListener(v -> {
            try {
                Bundle bundle = new Bundle();
                bundle.putSerializable("board_game_details", game.getDetails()); // Put the BoardGameDetails object from game.getDetails()
                bundle.putInt("game_id", game.getId());
                bundle.putString("game_name", game.getName()); // Pass game name
                bundle.putString("game_year", game.getYearPublished()); // Pass game year

                // Navigate to BoardGameDetailsFragment
                NavController navController = Navigation.findNavController((Activity) v.getContext(), R.id.nav_host_fragment);
                navController.navigate(R.id.boardGameDetailsFragment, bundle);

            } catch (Exception e) {
                Log.e("BoardGameAdapter", "Error navigating to BoardGameDetailsFragment: " + e.getMessage());
            }
        });

        // Handle the favorite button action
        holder.favoriteButton.setOnClickListener(v -> {
            // Add to favorites logic here
        });

        // Handle the matches button action
        holder.matchesButton.setOnClickListener(v -> {
            try {
                // Get the game name
                String gameNameMatch = game.getDetails().getName();
                Log.d("GameNameMatch",gameNameMatch);
                // Prepare bundle to pass the game name
                Bundle bundle = new Bundle();
                bundle.putString("gameName", gameNameMatch);

                // Navigate to AddMatchFragment with arguments
                NavController navController = Navigation.findNavController((Activity) v.getContext(), R.id.nav_host_fragment);
                navController.navigate(R.id.addMatchFragment, bundle);
            } catch (Exception e) {
                Log.e("MatchesButtonClick", "Error navigating to AddMatchFragment: " + e.getMessage());
            }
        });


        // Handle the remove button action
        holder.removeButton.setOnClickListener(v -> {
            // Remove from "My Games" logic here
            try {
                // Get user ID (assuming you have access to context)
                Context context = v.getContext();
                dbHelper = new DatabaseHelper(context);
                userId = Integer.parseInt(Utils.readUserID(context));  // Retrieve user ID

                // Get the game ID of the item to remove
                int gameId = game.getId();

                // Attempt to delete the game from "My Games"
                Log.d("LogMyGamesGameRemoval","userid:" + userId + " and gameid: " + gameId);
                boolean isDeleted = dbHelper.deleteGameFromMyGames(userId, gameId);

                if (isDeleted) {
                    Log.d("LogMyGamesGameRemoval", "Successfully removed game ID: " + gameId + " for user ID: " + userId);
                    boardGameList.remove(position);
                    notifyItemRemoved(position);
                } else {
                    Log.d("LogMyGamesGameRemoval", "Failed to remove game ID: " + gameId + " for user ID: " + userId);
                }
            } catch (Exception e) {
                Log.e("LogMyGamesGameRemoval", "Error removing game: " + e.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return boardGameList.size();
    }

    public static class BoardGameViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, yearTextView;
        ImageView imageView;
        ImageButton favoriteButton, removeButton, matchesButton;

        public BoardGameViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            yearTextView = itemView.findViewById(R.id.year_published);
            imageView = itemView.findViewById(R.id.board_game_image);
            favoriteButton = itemView.findViewById(R.id.favorite_button);
            matchesButton = itemView.findViewById(R.id.addmatches_button);
            removeButton = itemView.findViewById(R.id.remove_button);
        }
    }
}
