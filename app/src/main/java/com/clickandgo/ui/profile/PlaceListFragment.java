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

import java.util.LinkedList;

public abstract class PlaceListFragment extends Fragment {

    private RecyclerView mWishRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProfileListAdapter mAdapter;
    private PlaceResultsViewModel viewModel;

    private LinkedList<PlaceResult> results;

    private Class<? extends PlaceResultsViewModel> viewModelClass;

    public PlaceListFragment(Class<? extends PlaceResultsViewModel> viewModelClass) {
        this.viewModelClass = viewModelClass;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        results = new LinkedList<>();

        mWishRecyclerView = view.findViewById(R.id.places_list);
        mSwipeRefreshLayout = view.findViewById(R.id.refresh_places_list);

        mAdapter = new ProfileListAdapter(getContext(), results);
        mWishRecyclerView.setAdapter(mAdapter);
        mWishRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        viewModel = ViewModelProviders.of(this).get(viewModelClass);
        viewModel.init();

        viewModel.getPlacesData().observe(getViewLifecycleOwner(), placeResults -> {
            if (placeResults == null) return;
            mAdapter.clear();
            mAdapter.addAll(placeResults);
            mAdapter.notifyDataSetChanged();
        });

        setupItemClickListeners();
        setupRefreshListeners();
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
                result.toggle();
            }
        });
    }
}
