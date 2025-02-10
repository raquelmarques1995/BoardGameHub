package com.example.finalprojectandroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BoardGameAdapter extends RecyclerView.Adapter<BoardGameAdapter.BoardGameViewHolder> {

    private Context context;
    private List<BoardGame> boardGameList;

    public BoardGameAdapter(Context context, List<BoardGame> boardGameList) {
        this.context = context;
        this.boardGameList = boardGameList;
    }

    @NonNull
    @Override
    public BoardGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_boardgame, parent, false);
        return new BoardGameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardGameViewHolder holder, int position) {
        BoardGame game = boardGameList.get(position);
        holder.nameTextView.setText(game.getName());
        holder.yearTextView.setText("Year: " + game.getYearPublished());

        //Load image with glide
        if (game.getDetails() != null && game.getDetails().getImageUrl() != null) {
            Glide.with(context)
                    .load(game.getDetails().getImageUrl())
                    .placeholder(R.drawable.ic_imageloading)  // Show this while loading
                    .error(R.drawable.ic_boardgame_background)        // Show this if image fails to load
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_boardgame_background); // Set backup image
        }

        // Um onclick listener para apenas abrir o site, é a alternativa à parte abaixo
//        holder.itemView.setOnClickListener(v -> {
//            // Construct the URL with the corresponding game ID
//            String gameUrl = "https://boardgamegeek.com/boardgame/" + game.getId();
//
//            // Open the URL in a browser
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gameUrl));
//            context.startActivity(browserIntent);
//        });
        holder.itemView.setOnClickListener(v -> showGameDetailsDialog(context, game));

    }

    private void showGameDetailsDialog(Context context, BoardGame game) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_game_details, null);
        builder.setView(dialogView);

        // Initialize UI elements in the dialog
        TextView gameDetailsTextView = dialogView.findViewById(R.id.gameDetailsTextView);
        Button goToWebsiteButton = dialogView.findViewById(R.id.goToWebsiteButton);
        Button closeButton = dialogView.findViewById(R.id.closeButton);

        // Populate the game details
        // Build category text dynamically
        List<String> categories = game.getDetails().getCategories();
        String categoryText = "\nTop " + (categories != null ? Math.min(categories.size(), 5) : 0) + " categorias: ";

        if (categories != null && !categories.isEmpty()) {
            int maxCategories = Math.min(categories.size(), 5); // Show at most 5 categories
            categoryText += String.join(" , ", categories.subList(0, maxCategories));
        } else {
            categoryText += "N/A";
        }

        // Build the main message
        String message =
                game.getName() +
                "\nAno: " + (!String.valueOf(game.getYearPublished()).isEmpty() ? game.getYearPublished() : "Informação não disponível") +
                "\nMínimo jogadores: " + (!String.valueOf(game.getDetails().getMinPlayers()).isEmpty() ? game.getDetails().getMinPlayers() : "Informação não disponível") +
                "\nMáximo jogadores: " + (!String.valueOf(game.getDetails().getMaxPlayers()).isEmpty() ? game.getDetails().getMaxPlayers() : "Informação não disponível") +
                "\nTempo médio de jogo (mins): " + (!String.valueOf(game.getDetails().getPlayingTime()).isEmpty() ? game.getDetails().getPlayingTime() : "Informação não disponível") +
                "\nIdade mínima: " + (!String.valueOf(game.getDetails().getAge()).isEmpty() ? game.getDetails().getAge() : "Informação não disponível") +

                categoryText; // Append category text

        gameDetailsTextView.setText(message);

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        // "Go to Website" button action
        goToWebsiteButton.setOnClickListener(v -> {
            String gameUrl = "https://boardgamegeek.com/boardgame/" + game.getId();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gameUrl));
            context.startActivity(browserIntent);
        });

        // "Close" button action
        closeButton.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return boardGameList.size();
    }

    public static class BoardGameViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, yearTextView;
        ImageView imageView;

        public BoardGameViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            yearTextView = itemView.findViewById(R.id.year_published);
            imageView = itemView.findViewById(R.id.board_game_image);
        }
    }
}
