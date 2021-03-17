package com.clickandgo.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.clickandgo.R;
import com.clickandgo.viewmodel.HistoryViewModel;
import com.clickandgo.viewmodel.PlaceResultsViewModel;

public class HistoryFragment extends PlaceListFragment {

    public HistoryFragment() {
        super(HistoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }
}
