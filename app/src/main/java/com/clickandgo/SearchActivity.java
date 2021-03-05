package com.clickandgo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private TextView mSearchQuestion;
    private TextSwitcher mTextSwitcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.option);
        try {
            this.getSupportActionBar().hide();
        } catch (Exception e) {
            Log.d("ACTION BAR", e.getMessage());
        }

        //mSearchQuestion = findViewById(R.id.searchQuestionView);
        mTextSwitcher = findViewById(R.id.text_switcher);
        mTextSwitcher.setFactory(() -> {
            mSearchQuestion = new TextView(this);
            mSearchQuestion.setText("");
            mSearchQuestion.setTextSize(36);
            mSearchQuestion.setTextColor(Color.WHITE);
            mSearchQuestion.setGravity(View.TEXT_ALIGNMENT_CENTER);
            return mSearchQuestion;
        });
        setUpQuestionNavigation();
    }

    private void setUpQuestionNavigation() {
        mBottomNavigationView = findViewById(R.id.question_navigation_view);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.question_fragment_view);

        navHostFragment.getNavController().addOnDestinationChangedListener((controller, destination, arguments) -> {
            MenuItem menuItem = mBottomNavigationView.getMenu().findItem(destination.getId());
            CharSequence title = menuItem.getTitle();
            mTextSwitcher.setText(title);
        });

        NavigationUI.setupWithNavController(mBottomNavigationView, navHostFragment.getNavController());
    }

    public void onProfileIconClick(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }


}