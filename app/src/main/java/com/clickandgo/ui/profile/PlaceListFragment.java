package com.clickandgo.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.clickandgo.R;
import com.clickandgo.model.PlaceResult;
import com.clickandgo.utils.ItemClickSupport;
import com.clickandgo.viewmodel.PlaceResultsViewModel;

public abstract class PlaceListFragment extends Fragment implements FavoritesToggleListener {

    private RecyclerView mWishRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProfileListAdapter mAdapter;
    private PlaceResultsViewModel viewModel;

    private Class<? extends PlaceResultsViewModel> viewModelClass;

    public PlaceListFragment(Class<? extends PlaceResultsViewModel> viewModelClass) {
        this.viewModelClass = viewModelClass;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(viewModelClass);
        viewModel.init();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mWishRecyclerView = view.findViewById(R.id.places_list);
        mSwipeRefreshLayout = view.findViewById(R.id.refresh_places_list);

        mAdapter = new ProfileListAdapter(this);
        mWishRecyclerView.setAdapter(mAdapter);
        mWishRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.getPlacesData().observe(getViewLifecycleOwner(), placeResults -> {
            if (placeResults == null) return;
            mAdapter.update(placeResults);
        });

        setupItemClickListeners();
        setupRefreshListeners();
    }

    @Override
    public void onClicked(PlaceResult result) {
        viewModel.onWishlistToggle(result);
    }

    private void setupRefreshListeners() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.updatePlaceResults();
            mSwipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setupItemClickListeners() {
        ItemClickSupport.addTo(mWishRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                PlaceResult result = ((ProfileListAdapter) recyclerView.getAdapter()).getItemAt(position);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, result.getMapUri());
                startActivity(mapIntent);
            }

            @Override
            public void onItemDoubleClicked(RecyclerView recyclerView, int position, View v) {
                PlaceResult result = ((ProfileListAdapter) recyclerView.getAdapter()).getItemAt(position);
                viewModel.onWishlistToggle(result);
            }
        });
    }
}
