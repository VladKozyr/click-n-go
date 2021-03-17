package com.clickandgo.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clickandgo.R;
import com.clickandgo.viewmodel.FavouritesViewModel;

public class WishListFragment extends PlaceListFragment {

    public WishListFragment() {
        super(FavouritesViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wishlist, container, false);
    }
}
