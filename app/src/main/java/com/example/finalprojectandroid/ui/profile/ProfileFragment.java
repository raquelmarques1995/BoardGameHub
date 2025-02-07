package com.example.finalprojectandroid.ui.profile;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.finalprojectandroid.R;
import com.example.finalprojectandroid.DatabaseHelper;
import com.example.finalprojectandroid.Utils;
import com.example.finalprojectandroid.User;

public class ProfileFragment extends Fragment {

    private TextView tvUsername, tvEmail, tvName, tvBirthdate, tvCity, tvCountry;
    private EditText etName, etBirthdate, etCity, etCountry;
    private Button btnEdit, btnSave;
    private DatabaseHelper databaseHelper;
    private int userId;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inicializar elementos do layout
        tvUsername = root.findViewById(R.id.tvUsername);
        tvEmail = root.findViewById(R.id.tvEmail);
        tvName = root.findViewById(R.id.tvName);
        tvBirthdate = root.findViewById(R.id.tvBirthdate);
        tvCity = root.findViewById(R.id.tvCity);
        tvCountry = root.findViewById(R.id.tvCountry);

        etName = root.findViewById(R.id.etName);
        etBirthdate = root.findViewById(R.id.etBirthdate);
        etCity = root.findViewById(R.id.etCity);
        etCountry = root.findViewById(R.id.etCountry);

        btnEdit = root.findViewById(R.id.btnEdit);
        btnSave = root.findViewById(R.id.btnSave);

        databaseHelper = new DatabaseHelper(getContext());
        userId = Integer.parseInt(Utils.readUserID(getContext())); // Obter o ID do user logado

        loadUserData(); // Carregar os dados do utilizador

        // Botão "Editar"
        btnEdit.setOnClickListener(v -> toggleEditMode(true));

        // Botão "Guardar"
        btnSave.setOnClickListener(v -> {
            saveUserData();
            toggleEditMode(false);
        });

        return root;
    }

    // Metodo para carregar os dados do utilizador
    private void loadUserData() {
        User user = databaseHelper.getUserById(userId);
        if (user != null) {
            tvUsername.setText(user.getUsername());
            tvEmail.setText(user.getEmail());
            tvName.setText(user.getName());
            tvBirthdate.setText(user.getBirthdate());
            tvCity.setText(user.getCity());
            tvCountry.setText(user.getCountry());

            etName.setText(user.getName());
            etBirthdate.setText(user.getBirthdate());
            etCity.setText(user.getCity());
            etCountry.setText(user.getCountry());
        }
    }

    // Método para alternar entre os modos de exibição e edição
    private void toggleEditMode(boolean isEditing) {
        tvName.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        tvBirthdate.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        tvCity.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        tvCountry.setVisibility(isEditing ? View.GONE : View.VISIBLE);

        etName.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etBirthdate.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etCity.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etCountry.setVisibility(isEditing ? View.VISIBLE : View.GONE);

        btnEdit.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        btnSave.setVisibility(isEditing ? View.VISIBLE : View.GONE);
    }

    // Metodo para guardar os dados alterados na base de dados
    private void saveUserData() {
        String newName = etName.getText().toString().trim();
        String newBirthdate = etBirthdate.getText().toString().trim();
        String newCity = etCity.getText().toString().trim();
        String newCountry = etCountry.getText().toString().trim();

        databaseHelper.updateUser(userId, newName, newBirthdate, newCity, newCountry);
        loadUserData(); // Recarregar os dados atualizados
        toggleEditMode(false);
    }


    // Metodo para esconder o teclado
    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}