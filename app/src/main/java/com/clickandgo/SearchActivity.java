package com.clickandgo;

import android.animation.AnimatorSet;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.databinding.DataBindingUtil;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity implements ViewSwitcher.ViewFactory {

    private BottomNavigationView mBottomNavigationView;
    private NavHostFragment navHostFragment;
    private TextSwitcher mTextSwitcher;
    private Button mActionButton;
    private Button mBackButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            Log.d("ACTION BAR", e.getMessage());
        }

        mActionButton = findViewById(R.id.action_button);
        mBackButton = findViewById(R.id.back_button);

        mTextSwitcher = findViewById(R.id.text_switcher);
        mTextSwitcher.setFactory(this);
        setUpQuestionNavigation();
    }

    private void setUpQuestionNavigation() {
//        mBottomNavigationView = findViewById(R.id.question_navigation_view);
        navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.question_fragment_view);

        navHostFragment.getNavController().addOnDestinationChangedListener((controller, destination, arguments) -> {

//            MenuItem menuItem = mBottomNavigationView.getMenu().findItem(destination.getId());
//            CharSequence title = menuItem.getTitle();
//            mTextSwitcher.setText(title);
        });
//
//        NavigationUI.setupWithNavController(mBottomNavigationView, navHostFragment.getNavController());

        MotionLayout motionLayout = findViewById(R.id.question_navigation_view);
        findViewById(R.id.type_icon).setOnClickListener(v -> {
            motionLayout.setTransition(motionLayout.getCurrentState(), R.id.type_expanded);
            motionLayout.transitionToEnd();
            navHostFragment.getNavController().navigate(R.id.chooseTypeFragment);
        });

        findViewById(R.id.party_icon).setOnClickListener(v -> {
            motionLayout.setTransition(motionLayout.getCurrentState(), R.id.party_expanded);
            motionLayout.transitionToEnd();
            navHostFragment.getNavController().navigate(R.id.chooseGroupFragment);
        });

        findViewById(R.id.money_icon).setOnClickListener(v -> {
            motionLayout.setTransition(motionLayout.getCurrentState(), R.id.money_expanded);
            motionLayout.transitionToEnd();
            navHostFragment.getNavController().navigate(R.id.chooseMoneyFragment);
        });

        findViewById(R.id.place_icon).setOnClickListener(v -> {
            motionLayout.setTransition(motionLayout.getCurrentState(), R.id.place_expanded);
            motionLayout.transitionToEnd();
            navHostFragment.getNavController().navigate(R.id.choosePlaceFragment);
        });

        findViewById(R.id.action_button).setOnClickListener(v -> {
            motionLayout.setTransition(motionLayout.getCurrentState(), R.id.result);
            motionLayout.transitionToEnd();
            navHostFragment.getNavController().navigate(R.id.action_choosePlaceFragment_to_chooseResultFragment);
        });

        findViewById(R.id.back_button).setOnClickListener(v -> {
            motionLayout.setTransition(motionLayout.getCurrentState(), R.id.place_expanded);
            motionLayout.transitionToEnd();
            NavController navController = navHostFragment.getNavController();
            findViewById(R.id.action_button).setOnClickListener(v1 -> {
                motionLayout.setTransition(motionLayout.getCurrentState(), R.id.result);
                motionLayout.transitionToEnd();
                navHostFragment.getNavController().navigate(R.id.action_choosePlaceFragment_to_chooseResultFragment);
            });
            navController.navigate(navController.getPreviousBackStackEntry().getDestination().getId());
        });
    }

    public void onProfileIconClick(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    @Override
    public View makeView() {
        TextView textView = new TextView(this);
        textView.setTextAppearance(this, R.style.Search_Question);
        return textView;
    }
}