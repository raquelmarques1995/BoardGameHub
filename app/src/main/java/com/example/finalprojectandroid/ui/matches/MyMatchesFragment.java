package com.example.finalprojectandroid.ui.matches;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectandroid.DatabaseHelper;
import com.example.finalprojectandroid.R;
import com.example.finalprojectandroid.Utils;

import java.util.ArrayList;

public class MyMatchesFragment extends Fragment {
        private RecyclerView recyclerView;
        private MatchAdapter matchAdapter;
        private ArrayList<Match> matchList;
        private DatabaseHelper dbHelper;

        private String userId; // ID do used logged In

        public MyMatchesFragment() {
            // Empty constructor required
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_mymatches, container, false);

            //Layout manager (LinearLayoutManager), which arranges items in a vertical list
            recyclerView = view.findViewById(R.id.recyclerViewMatches);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            Button btnAddMatch = view.findViewById(R.id.btnAddMatch);
            btnAddMatch.setOnClickListener(v -> {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.addMatchFragment);
            });

            dbHelper = new DatabaseHelper(getContext());

            loadMatches(view);

            //Return the created View so that the fragment can display it.
            return view;
        }



    private void loadMatches(View view) {
        userId = Utils.readUserID(requireContext());

        matchList = dbHelper.getMatchesByUserId(userId);

        TextView messageView = view.findViewById(R.id.textViewMessage);
        if (matchList.isEmpty()) {
            messageView.setVisibility(View.VISIBLE);
            messageView.setText("Nenhuma partida encontrada!");
        } else {
            messageView.setVisibility(View.GONE);
        }

        // Create and configure the adapter
        matchAdapter = new MatchAdapter(getContext(), matchList);
        recyclerView.setAdapter(matchAdapter);

        // Set up the click on list items
        matchAdapter.setOnItemClickListener(match -> {
            Bundle bundle = new Bundle();
            bundle.putInt("match_id", match.getId());
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.matchDetailsFragment, bundle);
        });
    }

}