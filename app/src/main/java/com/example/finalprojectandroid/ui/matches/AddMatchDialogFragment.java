package com.example.finalprojectandroid.ui.matches;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.finalprojectandroid.DatabaseHelper;
import com.example.finalprojectandroid.R;
import com.example.finalprojectandroid.Utils;

public class AddMatchDialogFragment extends DialogFragment {
    private EditText edtGameName, edtMatchDate, edtScore;
    private DatabaseHelper dbHelper;
    private String userId;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_addmatch, null);

        // Inicializar os componentes do layout
        edtGameName = view.findViewById(R.id.edtGameName);
        edtMatchDate = view.findViewById(R.id.edtMatchDate);
        edtScore = view.findViewById(R.id.edtScore);
        Button btnSaveMatch = view.findViewById(R.id.btnSaveMatch);

        dbHelper = new DatabaseHelper(getContext());
        userId = Utils.readUserID(requireContext());

        btnSaveMatch.setOnClickListener(v -> {
            String gameName = edtGameName.getText().toString();
            String matchDate = edtMatchDate.getText().toString();
            int score = edtScore.getText().toString().isEmpty() ? 0 : Integer.parseInt(edtScore.getText().toString());

            if (!gameName.isEmpty() && !matchDate.isEmpty()) {
                boolean success = dbHelper.insertMatch(userId, gameName, matchDate, score, "");

                if (success) {
                    Toast.makeText(getContext(), "Partida adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                    dismiss(); // Fecha o diálogo
                } else {
                    Toast.makeText(getContext(), "Erro ao adicionar a partida.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(view);
        return builder.create();
    }
}

