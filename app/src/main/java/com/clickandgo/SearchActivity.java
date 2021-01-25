package com.clickandgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        try {
            this.getSupportActionBar().hide();
        } catch (Exception e) {
            Log.d("ACTION BAR", e.getMessage());
        }
        setUpQuestionNavigation();
    }

    private void setUpQuestionNavigation() {
        bottomNavigationView = findViewById(R.id.question_navigation_view);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.question_fragment_view);

        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());
    }

    public void onProfileIconClick(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }
}