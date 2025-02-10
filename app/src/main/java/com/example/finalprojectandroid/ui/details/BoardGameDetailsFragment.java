package com.example.finalprojectandroid.ui.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;  // For image loading
import com.example.finalprojectandroid.R;
import com.example.finalprojectandroid.BoardGameDetails;

public class BoardGameDetailsFragment extends Fragment {

    private TextView nameTextView;
    private TextView yearTextView;
    private TextView descriptionTextView;
    private ImageView gameImageView;
    private BoardGameDetails boardGameDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_boardgame_details, container, false);

        // Retrieve the BoardGameDetails object from the bundle
        Bundle bundle = getArguments();
        boardGameDetails = (BoardGameDetails) bundle.getSerializable("board_game_details");
        String gameName = bundle.getString("game_name");  // Get game name
        int gameYear = bundle.getInt("game_year");  // Get game year
        int gameId= bundle.getInt("game_id"); //Get game id, it's working properly

        // Access the UI elements
        nameTextView = rootView.findViewById(R.id.nameTextView);
        yearTextView = rootView.findViewById(R.id.yearTextView);
        descriptionTextView = rootView.findViewById(R.id.descriptionTextView);
        gameImageView = rootView.findViewById(R.id.gameImageView);

        // Set the game name and year
        nameTextView.setText(gameName);
        yearTextView.setText("Year: " + boardGameDetails.getYearPublished());

        // Set the description text
        if (boardGameDetails != null && boardGameDetails.getDescription() != null) {
            descriptionTextView.setText(boardGameDetails.getDescription());
        } else {
            descriptionTextView.setText("No description available");
        }

        // Load the game image if available
        if (boardGameDetails != null && boardGameDetails.getImageUrl() != null) {
            Glide.with(getContext())
                    .load(boardGameDetails.getImageUrl())
                    .placeholder(R.drawable.ic_imageloading)  // Placeholder image
                    .error(R.drawable.ic_boardgame_background)  // Error image
                    .into(gameImageView);
        } else {
            gameImageView.setImageResource(R.drawable.ic_boardgame_background); // Fallback image
        }

        // Back button functionality
        Button backButton = rootView.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            // You can use NavController to navigate back
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack();  // Pop the current fragment from the back stack
        });

        return rootView;
    }
}
