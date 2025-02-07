package com.example.finalprojectandroid.ui.matches;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
        private int userId; // ID do usuário logado

        public MyMatchesFragment() {
            // Construtor vazio obrigatório
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_mymatches, container, false);

            recyclerView = view.findViewById(R.id.recyclerViewMatches);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            Button btnAddMatch = view.findViewById(R.id.btnAddMatch);
            btnAddMatch.setOnClickListener(v -> {
                AddMatchDialogFragment dialog = new AddMatchDialogFragment();
                dialog.show(getParentFragmentManager(), "AddMatchDialog");
            });

            dbHelper = new DatabaseHelper(getContext());

            // Obter o ID do usuário logado (podes passar isso através do SharedPreferences ou argumento)
            String userId = Utils.readUserID(requireContext());

            loadMatches();

            return view;
        }


        private void loadMatches() {
            matchList = dbHelper.getMatchesByUserId(userId);
            matchAdapter = new MatchAdapter(getContext(), matchList);
            recyclerView.setAdapter(matchAdapter);
        }
}