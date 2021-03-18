package com.clickandgo.ui.profile.tab.history;

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

public class HistoryFragment extends PlacesListFragment<HistoryViewModel> {

    @Inject
    public ViewModelFactory factory;
    public HistoryViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory).get(HistoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public HistoryViewModel getViewModel() {
        return viewModel;
    }
}
