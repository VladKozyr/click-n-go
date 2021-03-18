package com.clickandgo.ui.profile.tab.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clickandgo.R;
import com.clickandgo.ui.profile.tab.PlacesListFragment;

public class FavoritesFragment extends PlacesListFragment {

    public FavoritesFragment() {
        super(FavouritesViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wishlist, container, false);
    }
}
