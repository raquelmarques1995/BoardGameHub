package com.example.finalprojectandroid;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.finalprojectandroid.databinding.ActivityMainframeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;



public class MainFrameActivity extends AppCompatActivity {
    private ActivityMainframeBinding binding;

    private TextView userIdTextViewInterno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainframeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //Isto é apenas um teste para verificar se ele é capaz de ir buscar o id para dentro de qualquer activity
        //na qual esteja a ser necessária a sessão
        Log.e("Leitura userID",Utils.readUserID(this));

        // Get user ID from the internal file using the static method
        String fileContent = Utils.readUserID(this);  // Pass the context (this) to read the file


        BottomNavigationView navView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search,
                R.id.navigation_games,
                R.id.navigation_community,
                R.id.navigation_matches,
                R.id.navigation_profile)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }
}
