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

        private String userId; // ID do usuário logado

        public MyMatchesFragment() {
            // Construtor vazio obrigatório
        }

        @Override // Metodo subscrito da classe Fragment e é responsável por criar e retornar a View deste fragmento
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_mymatches, container, false);//O layout xml é transformado na view

            //Gestor de layout (LinearLayoutManager), que organiza os itens numa lista vertical.
            recyclerView = view.findViewById(R.id.recyclerViewMatches);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            Button btnAddMatch = view.findViewById(R.id.btnAddMatch);
            btnAddMatch.setOnClickListener(v -> {
                // NavController é usado para a navegação entre fragmentos, vai buscar o nav_host_fragment e direciona-o para addMatchFragment.
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.addMatchFragment);
            });

            //Cria uma instância do DatabaseHelper, que gere o banco de dados SQLite.
            dbHelper = new DatabaseHelper(getContext());

            // metodo que carrega as partidas do user logged in
            loadMatches(view);

            //Retorna a View criada para que o fragmento possa exibi-la.
            return view;
        }



    private void loadMatches(View view) {
        // Obter o userID guardado no arquivo através do metodo readUserID
        userId = Utils.readUserID(requireContext());

        // Chama o metodo getMatchesByUserId(userId) do DatabaseHelper, para consultar a base de dados SQLite e retornar uma lista de partidas associadas ao user logged in.
        matchList = dbHelper.getMatchesByUserId(userId);

        // Verifica se a lista de partidas está vazia
        TextView messageView = view.findViewById(R.id.textViewMessage); // Usando o 'view' passado como parâmetro
        if (matchList.isEmpty()) {
            messageView.setVisibility(View.VISIBLE);  // Torna o TextView visível
            messageView.setText("Nenhuma partida encontrada!");  // Atualiza o texto da mensagem
        } else {
            messageView.setVisibility(View.GONE);  // Esconde o TextView se houver partidas
        }

        // Cria e configura o adaptador
        matchAdapter = new MatchAdapter(getContext(), matchList);
        recyclerView.setAdapter(matchAdapter);

        // Configura o clique nos itens da lista
        matchAdapter.setOnItemClickListener(match -> {
            Bundle bundle = new Bundle();
            bundle.putInt("match_id", match.getId()); // Passa o ID da partida

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.matchDetailsFragment, bundle);
        });
    }

}