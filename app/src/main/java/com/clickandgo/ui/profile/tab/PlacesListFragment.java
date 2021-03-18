package com.clickandgo.ui.profile.tab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.clickandgo.R;
import com.clickandgo.domain.model.PlaceResult;
import com.clickandgo.utils.ItemClickSupport;

import dagger.android.support.DaggerFragment;

public abstract class PlacesListFragment<T extends PlacesResultsViewModel> extends DaggerFragment implements FavoritesToggleListener {

    private RecyclerView mWishRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PlacesListAdapter mAdapter;

    public abstract T getViewModel();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mWishRecyclerView = view.findViewById(R.id.places_list);
        mSwipeRefreshLayout = view.findViewById(R.id.refresh_places_list);

        mAdapter = new PlacesListAdapter(this);
        mWishRecyclerView.setAdapter(mAdapter);
        mWishRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getViewModel().getPlacesData().observe(getViewLifecycleOwner(), placeResults -> {
            if (placeResults == null) return;
            mAdapter.update(placeResults);
        });

        setupItemClickListeners();
        setupRefreshListeners();
    }

    @Override
    public void onClicked(PlaceResult result) {
        getViewModel().onWishlistToggle(result);
    }

    private void setupRefreshListeners() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            getViewModel().updatePlaceResults();
            mSwipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setupItemClickListeners() {
        ItemClickSupport.addTo(mWishRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                PlaceResult result = ((PlacesListAdapter) recyclerView.getAdapter()).getItemAt(position);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, result.getMapUri());
                startActivity(mapIntent);
            }

            @Override
            public void onItemDoubleClicked(RecyclerView recyclerView, int position, View v) {
                PlaceResult result = ((PlacesListAdapter) recyclerView.getAdapter()).getItemAt(position);
                getViewModel().onWishlistToggle(result);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getViewModel().updatePlaceResults();
    }
}
