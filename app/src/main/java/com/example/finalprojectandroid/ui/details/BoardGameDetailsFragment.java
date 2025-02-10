//package com.example.finalprojectandroid.ui.details;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.fragment.app.Fragment;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//
//import com.bumptech.glide.Glide;  // For image loading
//import com.example.finalprojectandroid.R;
//import com.example.finalprojectandroid.BoardGameDetails;
//
//public class BoardGameDetailsFragment extends Fragment {
//
//    private TextView nameTextView;
//    private TextView yearTextView;
//    private TextView descriptionTextView;
//    private ImageView gameImageView;
//    private BoardGameDetails boardGameDetails;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_boardgame_details, container, false);
//
//        // Initialize UI components
//        nameTextView = view.findViewById(R.id.nameTextView);
//        yearTextView = view.findViewById(R.id.yearTextView);
//        descriptionTextView = view.findViewById(R.id.descriptionTextView);
//        gameImageView = view.findViewById(R.id.gameImageView);
//
//        // Get the board game details from the arguments
//        if (getArguments() != null) {
//            boardGameDetails = (BoardGameDetails) getArguments().getSerializable("board_game_details");
//        }
//
//        if (boardGameDetails != null) {
//            // Populate UI with the board game details
//            nameTextView.setText(boardGameDetails.getName());
//            yearTextView.setText("Year: " + boardGameDetails.getYearPublished());
//            descriptionTextView.setText(boardGameDetails.getDescription());
//
//            // Load the image using Glide
//            Glide.with(getContext())
//                    .load(boardGameDetails.getImageUrl())
//                    .into(gameImageView);
//        }
//
//        // Navigate back to the SearchFragment
//        view.findViewById(R.id.backButton).setOnClickListener(v -> {
//            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
//            navController.navigateUp();  // Navigate back to the previous fragment
//        });
//
//        return view;
//    }
//}
