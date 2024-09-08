package com.kelimelerleispanyolca.vocabulary;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kelimelerleispanyolca.vocabulary.databinding.ActivityMainBinding;

import java.util.Set;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = binding.botNag;
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);

        Set<Integer> topLevelDestinations = Set.of(
                R.id.modeSelectionFragment,
                R.id.wordsFragment,
                R.id.wordGraph
        );

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).build();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (topLevelDestinations.contains(destination.getId())) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            } else {
                bottomNavigationView.setVisibility(View.GONE);
            }
        });
    }
}
