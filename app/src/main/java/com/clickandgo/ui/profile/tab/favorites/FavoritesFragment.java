package com.clickandgo.ui.profile.tab.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.clickandgo.R;
import com.clickandgo.di.viewmodel.ViewModelFactory;
import com.clickandgo.ui.profile.tab.PlacesListFragment;

import javax.inject.Inject;

public class FavoritesFragment extends PlacesListFragment<FavoritesViewModel> {

    @Inject
    public ViewModelFactory factory;
    public FavoritesViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory).get(FavoritesViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wishlist, container, false);
    }

    @Override
    public FavoritesViewModel getViewModel() {
        return viewModel;
    }
}
