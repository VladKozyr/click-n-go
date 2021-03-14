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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.databinding.DataBindingUtil;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SearchActivity extends AppCompatActivity implements ViewSwitcher.ViewFactory {

    private static final int RESULT_PHOTO_UPDATED = 3;

    private NavHostFragment navHostFragment;
    private TextSwitcher mTextSwitcher;
    private Button mActionButton;
    private Button mBackButton;
    private ImageView profileIcon;
    private MotionLayout motionLayout;


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
        profileIcon = findViewById(R.id.profile_icon);

        mTextSwitcher = findViewById(R.id.text_switcher);
        mTextSwitcher.setFactory(this);
        setUpQuestionNavigation();

        setupProfileIcon();
    }

    private void setupProfileIcon() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .circleCrop()
                        .into(profileIcon);
            }
        }
    }

    private void setUpQuestionNavigation() {

        navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.question_fragment_view);

        motionLayout = findViewById(R.id.question_navigation_view);

        setupTransition(R.id.type_expanded, R.id.chooseTypeFragment, "What?");

        findViewById(R.id.type_icon).setOnClickListener(v -> {
            setupTransition(R.id.type_expanded, R.id.chooseTypeFragment, "What?");
        });

        findViewById(R.id.party_icon).setOnClickListener(v -> {
            setupTransition(R.id.party_expanded, R.id.chooseGroupFragment, "How many?");
        });

        findViewById(R.id.money_icon).setOnClickListener(v -> {
            setupTransition(R.id.money_expanded, R.id.chooseMoneyFragment, "How much?");
        });

        findViewById(R.id.place_icon).setOnClickListener(v -> {
            setupTransition(R.id.place_expanded, R.id.choosePlaceFragment, "Where?");
        });

        mActionButton.setOnClickListener(v -> {
            setupTransition(R.id.result, R.id.action_choosePlaceFragment_to_chooseResultFragment, "Go!");
        });

        findViewById(R.id.back_button).setOnClickListener(v -> {
            setupTransition(R.id.place_expanded, R.id.choosePlaceFragment, "Where?");
            mActionButton.setOnClickListener(v1 -> {
                setupTransition(R.id.result, R.id.action_choosePlaceFragment_to_chooseResultFragment, "Go!");
            });
        });
    }

    private void setupTransition(@IdRes int anim, @IdRes int fragment, String title) {
        motionLayout.setTransition(motionLayout.getCurrentState(), anim);
        motionLayout.transitionToEnd();
        navHostFragment.getNavController().navigate(fragment);
        mTextSwitcher.setText(title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_PHOTO_UPDATED && resultCode == RESULT_OK) {
            setupProfileIcon();
        }
    }

    public void onProfileIconClick(View view) {
        startActivityForResult(new Intent(this, ProfileActivity.class), RESULT_PHOTO_UPDATED);
    }

    @Override
    public View makeView() {
        TextView textView = new TextView(this);
        textView.setTextAppearance(this, R.style.Search_Question);
        return textView;
    }
}