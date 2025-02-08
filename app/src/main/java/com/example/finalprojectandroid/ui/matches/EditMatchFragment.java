package com.example.finalprojectandroid.ui.matches;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.finalprojectandroid.DatabaseHelper;
import com.example.finalprojectandroid.R;
import com.example.finalprojectandroid.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditMatchFragment extends Fragment {

    private EditText editTextGameName, editTextMatchDate, editTextNumberPlayers, editTextDuration, editTextScore, editTextNotes;
    private DatabaseHelper dbHelper;
    private int matchId; // ID da partida que vai ser editada

    private Date selectedDate; // Para armazenar a data selecionada como Date

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editmatch, container, false);

        // Ativar a seta de voltar
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setHasOptionsMenu(true); // Permite capturar eventos do menu


        // Inicializa os EditText ANTES de chamar loadMatchData()
        editTextGameName = view.findViewById(R.id.editTextGameName);
        editTextMatchDate = view.findViewById(R.id.editTextMatchDate);
        editTextNumberPlayers = view.findViewById(R.id.editTextNumberPlayers);
        editTextDuration = view.findViewById(R.id.editTextDuration);
        editTextScore = view.findViewById(R.id.editTextScore);
        editTextNotes = view.findViewById(R.id.editTextNotes);

        // Configurar DatePicker
        editTextMatchDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        selectedDate = calendar.getTime(); // Armazena a data como Date
                        editTextMatchDate.setText(Utils.formatDate(selectedDate)); // Exibe no campo já formatado
                    }, year, month, day);

            datePickerDialog.show();
        });

        // Inicializa dbHelper
        dbHelper = new DatabaseHelper(getContext());

        if (getArguments() != null) {
            matchId = getArguments().getInt("match_id", -1);
            Log.e("EditMatchFragment", "Match ID recebido: " + matchId);
        } else {
            Log.e("EditMatchFragment", "Erro: matchId não recebido!");
        }

        // SOMENTE CHAMA loadMatchData() APÓS A INICIALIZAÇÃO DOS EDITTEXT
        if (matchId != -1) {
            loadMatchData();
        } else {
            Log.e("EditMatchFragment", "Erro: matchId inválido!");
        }

        view.findViewById(R.id.btnSaveMatch).setOnClickListener(v -> saveMatch());

        return view;
    }

    private void loadMatchData() {
        if (dbHelper == null) {
            Log.e("EditMatchFragment", "Erro: dbHelper é null!");
            return;
        }

        Match match = dbHelper.getMatchById(matchId);
        if (match.getMatchDate() != null) {
            editTextMatchDate.setText(Utils.formatDate(match.getMatchDate()));
        } else {
            editTextMatchDate.setText(""); // Ou definir um valor padrão, se necessário
            Log.e("EditMatchFragment", "Erro: matchDate é null!");
        }

        editTextGameName.setText(match.getGameName());
        editTextMatchDate.setText(Utils.formatDate(match.getMatchDate()));
        editTextNumberPlayers.setText(String.valueOf(match.getNumberPlayers() == -1000 ? "" : match.getNumberPlayers()));
        editTextDuration.setText(String.valueOf(match.getDuration() == -1000 ? "" : match.getDuration()));
        editTextScore.setText(String.valueOf(match.getScore() == -1000 ? "" : match.getScore()));
        editTextNotes.setText(match.getNotes());
    }

    private void showDatePickerDialog() {
        // Exibe o DatePickerDialog
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
            // Formatar e atualizar o campo com a data escolhida
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year1, month1, dayOfMonth);
            editTextMatchDate.setText(dateFormat.format(selectedDate.getTime()));
        }, year, month, day);

        datePickerDialog.show();
    }

    public void saveMatch() {
        String gameName = editTextGameName.getText().toString().trim();
        String matchDate = editTextMatchDate.getText().toString().trim();
        String numberPlayersStr = editTextNumberPlayers.getText().toString().trim();
        String durationStr = editTextDuration.getText().toString().trim();
        String scoreStr = editTextScore.getText().toString().trim();
        String notes = editTextNotes.getText().toString().trim();

        // Verificar se os campos obrigatórios não estão vazios
        if (gameName.isEmpty() || matchDate.isEmpty()) {
            Toast.makeText(getContext(), "Nome do jogo e Data da partida são obrigatórios!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Converter os campos não obrigatórios para números (caso vazio)
        int numberPlayers = numberPlayersStr.isEmpty() ? -1000 : Integer.parseInt(numberPlayersStr);
        int duration = durationStr.isEmpty() ? -1000 : Integer.parseInt(durationStr);
        int score = scoreStr.isEmpty() ? -1000 : Integer.parseInt(scoreStr);

        // Atualizar a partida no banco de dados
        boolean updated = dbHelper.updateMatch(matchId, gameName, matchDate, numberPlayers, duration, score, notes);

        if (updated) {
            Toast.makeText(getContext(), "Partida atualizada com sucesso!", Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigateUp(); // Volta para a lista de partidas
        } else {
            Toast.makeText(getContext(), "Falha ao atualizar a partida.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().onBackPressed(); // Volta para o fragment anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
