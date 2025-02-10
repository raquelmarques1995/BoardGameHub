package com.example.finalprojectandroid.ui.games;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalprojectandroid.BoardGame;
import com.example.finalprojectandroid.R;

import java.util.List;

public class BoardGameAdapterGames extends RecyclerView.Adapter<BoardGameAdapterGames.BoardGameViewHolder> {

    private Context context;
    private List<BoardGame> boardGameList;

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

        // Handle the favorite button action
        holder.favoriteButton.setOnClickListener(v -> {
            // Add to favorites logic here
        });

        // Handle the remove button action
        holder.removeButton.setOnClickListener(v -> {
            // Remove from "My Games" logic here
            boardGameList.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return boardGameList.size();
    }

    public static class BoardGameViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, yearTextView;
        ImageView imageView;
        ImageButton favoriteButton, removeButton;

        public BoardGameViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            yearTextView = itemView.findViewById(R.id.year_published);
            imageView = itemView.findViewById(R.id.board_game_image);
            favoriteButton = itemView.findViewById(R.id.favorite_button);
            removeButton = itemView.findViewById(R.id.remove_button);
        }
    }
}
