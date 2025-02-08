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
        // Construtor vazio necessário
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matchdetails, container, false);

        txtMatchDetails = view.findViewById(R.id.txtMatchDetails);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnDelete = view.findViewById(R.id.btnDelete);

        dbHelper = new DatabaseHelper(getContext());

        // Ativar a seta de voltar
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setHasOptionsMenu(true); // Permite capturar eventos do menu

        if (getArguments() != null) {
            matchId = getArguments().getInt("match_id");
            loadMatchDetails(matchId);
        }

        // Inicializar o botão de editar
        btnEdit.setOnClickListener(v -> {
            // Criar um Bundle para passar o ID da partida
            Bundle bundle = new Bundle();
            bundle.putInt("match_id", matchId);  // Passa o ID da partida para o EditMatchFragment
            Log.e("MatchDelailsFragment","id" + matchId);
            // Navegar para o EditMatchFragment
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.EditMatchFragment, bundle);  // R.id.EditMatchFragment é o ID do seu fragmento de edição no navigation_graph.xml
        });

        btnDelete.setOnClickListener(v -> {
            boolean isDeleted = dbHelper.deleteMatch(matchId);
            if (isDeleted) {
                Toast.makeText(getContext(), "Partida eliminada!", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed(); // Volta para a lista de partidas
            } else {
                Toast.makeText(getContext(), "Erro ao eliminar a partida!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().onBackPressed(); // Volta para o fragment anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void loadMatchDetails(int matchId) {
        Match match = dbHelper.getMatchById(matchId);
        if (match != null) {
            StringBuilder matchDetails = new StringBuilder();

            // Nome do jogo
            matchDetails.append("Nome: ").append(match.getGameName()).append("\n");

            // Data
            if (match.getMatchDate() != null) {
                String formattedDate = Utils.formatDate(match.getMatchDate());
                matchDetails.append("Data: ").append(formattedDate).append("\n");
            } else {
                Log.e("MatchDetailsFragment", "Erro: matchDate é null!");
                matchDetails.append("Data: Não disponível\n");
            }

            // Número de jogadores
            if (match.getNumberPlayers() == -1000) {
                matchDetails.append("Nº de Jogadores: Sem informação\n");
            } else {
                matchDetails.append("Nº de Jogadores: ").append(match.getNumberPlayers()).append("\n");
            }

            // Duração
            if (match.getDuration() == -1000) {
                matchDetails.append("Duração: Sem informação\n");
            } else {
                matchDetails.append("Duração: ").append(match.getDuration()).append(" min\n");
            }

            // Pontuação
            if (match.getScore() == -1000) {
                matchDetails.append("Pontuação: Sem pontuação\n");
            } else {
                matchDetails.append("Pontuação: ").append(match.getScore()).append(" pontos\n");
            }

            // Notas
            matchDetails.append("Notas: ").append(match.getNotes()).append("\n");

            // Definir o texto do TextView
            txtMatchDetails.setText(matchDetails.toString());

        } else {
            Log.e("MatchDetailsFragment", "Erro: partida não encontrada para o ID: " + matchId);
            txtMatchDetails.setText("Erro ao carregar detalhes da partida.");
        }
    }

}


