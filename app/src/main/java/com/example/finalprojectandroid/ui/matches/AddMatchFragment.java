package com.example.finalprojectandroid.ui.matches;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.finalprojectandroid.DatabaseHelper;
import com.example.finalprojectandroid.R;
import com.example.finalprojectandroid.Utils;

import java.util.Calendar;
import java.util.Date;

public class AddMatchFragment extends Fragment {
    private EditText edtGameName, edtMatchDate, edtNumberPlayers, edtDuration, edtScore, edtNotes;
    private DatabaseHelper dbHelper;
    private String userId;
    private Date selectedDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addmatch, container, false);

        // Enable the back arrow
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        setHasOptionsMenu(true); // Allows capturing menu events

        // Initialize components
        edtGameName = view.findViewById(R.id.edtGameName);
        edtMatchDate = view.findViewById(R.id.edtMatchDate);
        edtNumberPlayers = view.findViewById(R.id.edtNumberPlayers);
        edtDuration = view.findViewById(R.id.edtDuration);
        edtScore = view.findViewById(R.id.edtScore);
        edtNotes = view.findViewById(R.id.edtNotes);
        Button btnSaveMatch = view.findViewById(R.id.btnSaveMatch);

        // Retrieve the gameName from arguments if it was sent through MyGames or Search
        if (getArguments() != null && getArguments().containsKey("gameName")) {
            String gameName = getArguments().getString("gameName", ""); // Default to empty string if null
            edtGameName.setText(gameName);
        } else {
            edtGameName.setText(""); // Leave it empty if no gameName was passed
        }

        dbHelper = new DatabaseHelper(requireContext());
        userId = Utils.readUserID(requireContext());


        // Set up DatePicker
        edtMatchDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        selectedDate = calendar.getTime();
                        edtMatchDate.setText(Utils.formatDate(selectedDate));
                    }, year, month, day);

            datePickerDialog.show();
        });

        // Save match button
        btnSaveMatch.setOnClickListener(v -> {
            String gameName = edtGameName.getText().toString().trim();
            String matchDate = edtMatchDate.getText().toString().trim();
            String numberPlayersStr = edtNumberPlayers.getText().toString().trim();
            String durationStr = edtDuration.getText().toString().trim();
            String scoreStr = edtScore.getText().toString().trim();
            String notes = edtNotes.getText().toString().trim();

            // Check if the required fields are not empty
            if (gameName.isEmpty() || matchDate.isEmpty()) {
                Toast.makeText(getContext(), "Nome do jogo e Data da partida são obrigatórios!", Toast.LENGTH_SHORT).show();
                return;
            }

            //Convert the non-required fields to numbers (if empty)
            int numberPlayers = numberPlayersStr.isEmpty() ? -1000 : Integer.parseInt(numberPlayersStr);
            int duration = durationStr.isEmpty() ? -1000 : Integer.parseInt(durationStr);
            int score = scoreStr.isEmpty() ? -1000 : Integer.parseInt(scoreStr);

            // Update in the database
            boolean isSaved = dbHelper.insertMatch(userId, gameName, matchDate, numberPlayers, duration, score, notes);

            if (isSaved) {
                Toast.makeText(getContext(), "Partida salva com sucesso!", Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigateUp();
            } else {
                Toast.makeText(getContext(), "Erro ao salvar partida", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().onBackPressed(); //Return to the previous fragment
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
