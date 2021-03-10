package com.clickandgo.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.clickandgo.R;
import com.clickandgo.SearchActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class AuthFragment extends Fragment {
    FragmentAdapter myFragmentAdapter;
    ViewPager viewPager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //todo replace to another method

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            startActivity(new Intent(getContext(), SearchActivity.class));// Navigate to the next Fragment
        }

        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //TODO delete app name from tool bar another way
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
        myFragmentAdapter = new FragmentAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, getContext());
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(myFragmentAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);

        changeTabLayoutUI(tabLayout);

    }

    /**
     * Change UI when tab selected or unselected
     *
     * @param tabLayout
     */
    private void changeTabLayoutUI(TabLayout tabLayout) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                TextView tabTextView = new TextView(getContext());
                tabTextView.setTextColor(Color.WHITE);
                tabTextView.setAlpha(0.5f);
                tabTextView.setTextSize(19);
                tab.setCustomView(tabTextView);
                tabTextView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                tabTextView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                tabTextView.setText(tab.getText());

                //By default first tab is selected
                if (i == 0) {
                    tabTextView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                    tabTextView.setAlpha(1);
                }
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                TextView text = (TextView) tab.getCustomView();
                text.setAlpha(1);
                text.setTextSize(19);
                text.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView text = (TextView) tab.getCustomView();
                Typeface tf = ResourcesCompat.getFont(requireContext(), R.font.sf_light);
                text.setAlpha(0.5f);
                text.setTextSize(18);
                text.setTypeface(tf);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}