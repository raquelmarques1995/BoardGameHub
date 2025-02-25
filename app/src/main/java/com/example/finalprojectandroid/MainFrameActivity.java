package com.example.finalprojectandroid;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.finalprojectandroid.databinding.ActivityMainframeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainFrameActivity extends AppCompatActivity {
    private ActivityMainframeBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainframeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String fileContent = Utils.readUserID(this);

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
