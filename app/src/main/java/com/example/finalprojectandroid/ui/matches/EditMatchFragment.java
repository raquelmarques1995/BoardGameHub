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
    private int matchId;

    private Date selectedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editmatch, container, false);

        //Activate the back arrow
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setHasOptionsMenu(true);


        // Initialize the EditText BEFORE calling loadMatchData()
        editTextGameName = view.findViewById(R.id.editTextGameName);
        editTextMatchDate = view.findViewById(R.id.editTextMatchDate);
        editTextNumberPlayers = view.findViewById(R.id.editTextNumberPlayers);
        editTextDuration = view.findViewById(R.id.editTextDuration);
        editTextScore = view.findViewById(R.id.editTextScore);
        editTextNotes = view.findViewById(R.id.editTextNotes);

        // Set up DatePicker
        editTextMatchDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        selectedDate = calendar.getTime();
                        editTextMatchDate.setText(Utils.formatDate(selectedDate));
                    }, year, month, day);

            datePickerDialog.show();
        });

        dbHelper = new DatabaseHelper(getContext());

        if (getArguments() != null) {
            matchId = getArguments().getInt("match_id", -1);
            //Log.d("EditMatchFragment", "Match ID recebido: " + matchId);
        } else {
            //Log.e("EditMatchFragment", "Erro: matchId não recebido!");
        }

        // ONLY call loadMatchData() AFTER initializing the EditText
        if (matchId != -1) {
            loadMatchData();
        } else {
            //Log.e("EditMatchFragment", "Erro: matchId inválido!");
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
            editTextMatchDate.setText("");
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
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
            // Format and update the field with the selected date
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

        // Check if the required fields are not empty
        if (gameName.isEmpty() || matchDate.isEmpty()) {
            Toast.makeText(getContext(), "Nome do jogo e Data da partida são obrigatórios!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert the non-required fields to numbers (if empty)
        int numberPlayers = numberPlayersStr.isEmpty() ? -1000 : Integer.parseInt(numberPlayersStr);
        int duration = durationStr.isEmpty() ? -1000 : Integer.parseInt(durationStr);
        int score = scoreStr.isEmpty() ? -1000 : Integer.parseInt(scoreStr);

        // Update in the database
        boolean updated = dbHelper.updateMatch(matchId, gameName, matchDate, numberPlayers, duration, score, notes);

        if (updated) {
            Toast.makeText(getContext(), "Partida atualizada com sucesso!", Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigateUp();
        } else {
            Toast.makeText(getContext(), "Falha ao atualizar a partida.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
