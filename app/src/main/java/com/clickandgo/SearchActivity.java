package com.clickandgo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.clickandgo.di.viewmodel.ViewModelFactory;
import com.clickandgo.domain.model.PlaceResult;
import com.clickandgo.ui.navigation.OnCorrectResultListener;
import com.clickandgo.ui.navigation.OnEmptyResultListener;
import com.clickandgo.ui.navigation.SearchViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

public class SearchActivity extends AppCompatActivity implements
        ViewSwitcher.ViewFactory,
        OnEmptyResultListener,
        OnCorrectResultListener {

    private static final int RESULT_PHOTO_UPDATED = 3;

    private NavHostFragment navHostFragment;
    private TextSwitcher mTextSwitcher;
    private Button mActionButton;
    private Button mBackButton;
    private ImageView profileIcon;
    private MotionLayout motionLayout;
    private NavOptions navOptions;

    @Inject
    public ViewModelFactory factory;
    public SearchViewModel searchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            Log.d("ACTION BAR", e.getMessage());
        }

        searchViewModel = ViewModelProviders.of(this, factory).get(SearchViewModel.class);

        mActionButton = findViewById(R.id.action_button);
        mBackButton = findViewById(R.id.back_button);
        profileIcon = findViewById(R.id.profile_icon);

        mTextSwitcher = findViewById(R.id.text_switcher);
        mTextSwitcher.setFactory(this);

        setupNavOptions();
        setUpQuestionNavigation();
        setupProfileIcon();
    }

    private void setupNavOptions() {
        navOptions = new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_left)
                .setExitAnim(R.anim.slide_out_right)
                .setPopEnterAnim(R.anim.slide_in_right)
                .setPopExitAnim(R.anim.slide_out_left)
                .build();
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


        //TODO fix bug
        if (motionLayout.getCurrentState() == R.id.base)
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

        setupActionButton();

        mBackButton.setOnClickListener(v -> {
            setupTransition(R.id.place_expanded, R.id.choosePlaceFragment, "Where?");
            setupActionButton();
        });
    }

    private void setupActionButton() {
        mActionButton.setText("GO");
        mActionButton.setOnClickListener(v -> {
            if (searchViewModel.isAllOptions())
                setupTransition(R.id.result, R.id.action_choosePlaceFragment_to_chooseResultFragment, "Go!");
            else
                Toast.makeText(this, "Select all options", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupTransition(@IdRes int anim, @IdRes int fragment, String title) {
        motionLayout.setTransition(motionLayout.getCurrentState(), anim);
        motionLayout.transitionToEnd();
        navHostFragment.getNavController().navigate(fragment, null, navOptions);
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

    @Override
    public void updateUI() {
        mTextSwitcher.setText("Sorry");
        mActionButton.setText("back");
        mActionButton.setOnClickListener(v -> {
            setupTransition(R.id.place_expanded, R.id.choosePlaceFragment, "Where?");
            setupActionButton();
        });
    }

    @Override
    public void onCorrectResult(PlaceResult result) {
        mActionButton.setOnClickListener(v -> {
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, result.getMapUri());
            startActivity(mapIntent);
        });
    }
}