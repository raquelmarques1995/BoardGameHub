package com.example.finalprojectandroid.ui.matches;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.finalprojectandroid.DatabaseHelper;
import com.example.finalprojectandroid.R;
import com.example.finalprojectandroid.Utils;

public class MatchDetailsFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private int matchId;
    private TextView txtMatchDetails;
    private Button btnEdit, btnDelete;

    public MatchDetailsFragment() {
        //Empty constructor required
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matchdetails, container, false);

        txtMatchDetails = view.findViewById(R.id.txtMatchDetails);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnDelete = view.findViewById(R.id.btnDelete);

        dbHelper = new DatabaseHelper(getContext());

        // Activate the back arrow
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            matchId = getArguments().getInt("match_id");
            loadMatchDetails(matchId);
        }

        // Initialize the Edit button
        btnEdit.setOnClickListener(v -> {
            // Create a Bundle to pass the match ID
            Bundle bundle = new Bundle();
            bundle.putInt("match_id", matchId);  // Pass the match ID to the EditMatchFragment
            Log.e("MatchDelailsFragment","id" + matchId);
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.EditMatchFragment, bundle);
        });

        btnDelete.setOnClickListener(v -> {
            boolean isDeleted = dbHelper.deleteMatch(matchId);
            if (isDeleted) {
                Toast.makeText(getContext(), "Partida eliminada!", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            } else {
                Toast.makeText(getContext(), "Erro ao eliminar a partida!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void loadMatchDetails(int matchId) {
        Match match = dbHelper.getMatchById(matchId);
        if (match != null) {
            StringBuilder matchDetails = new StringBuilder();

            // Game name
            matchDetails.append("Nome: ").append(match.getGameName()).append("\n");

            // Date
            if (match.getMatchDate() != null) {
                String formattedDate = Utils.formatDate(match.getMatchDate());
                matchDetails.append("Data: ").append(formattedDate).append("\n");
            } else {
                matchDetails.append("Data: Não disponível\n");
            }

            // Number of players
            if (match.getNumberPlayers() == -1000) {
                matchDetails.append("Nº de Jogadores: Sem informação\n");
            } else {
                matchDetails.append("Nº de Jogadores: ").append(match.getNumberPlayers()).append("\n");
            }

            // Duration
            if (match.getDuration() == -1000) {
                matchDetails.append("Duração: Sem informação\n");
            } else {
                matchDetails.append("Duração: ").append(match.getDuration()).append(" min\n");
            }

            // Score
            if (match.getScore() == -1000) {
                matchDetails.append("Pontuação: Sem pontuação\n");
            } else {
                matchDetails.append("Pontuação: ").append(match.getScore()).append(" pontos\n");
            }

            // Notes
            matchDetails.append("Notas: ").append(match.getNotes()).append("\n");


            txtMatchDetails.setText(matchDetails.toString());

        } else {
            Log.e("MatchDetailsFragment", "Error: Match not found for the ID: " + matchId);
            txtMatchDetails.setText("Erro ao carregar detalhes da partida.");
        }
    }

}


