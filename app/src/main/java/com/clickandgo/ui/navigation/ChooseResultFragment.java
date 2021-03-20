package com.clickandgo.ui.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.clickandgo.R;
import com.clickandgo.SearchActivity;
import com.clickandgo.databinding.SearchResultBinding;
import com.clickandgo.di.viewmodel.ViewModelFactory;

import javax.inject.Inject;

public class ChooseResultFragment extends Fragment {

    private TextView result;
    private LinearLayout layout;

    @Inject
    public ViewModelFactory factory;
    public SearchViewModel searchViewModel;

    private OnEmptyResultListener onEmptyResultListener;
    private OnCorrectResultListener onCorrectResultListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onEmptyResultListener = ((SearchActivity) getActivity());
        onCorrectResultListener = ((SearchActivity) getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchViewModel = ViewModelProviders.of(requireActivity(), factory).get(SearchViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        result = getView().findViewById(R.id.result);
        layout = getView().findViewById(R.id.result_layout);

        setupOnLoad();
    }

    private void setupOnLoad() {
        searchViewModel.getSearchResult().observe(getViewLifecycleOwner(), result -> {
            if (result == null) {
                onEmptyResultListener.updateUI();
                getView().findViewById(R.id.sorry_message).setVisibility(View.VISIBLE);
            } else {
                SearchResultBinding binding = SearchResultBinding
                        .inflate(LayoutInflater.from(getContext()), layout, false);
                binding.setPlaceResult(result);

                ViewGroup.LayoutParams layoutParams = binding.getRoot().getLayoutParams();
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                binding.getRoot().setLayoutParams(layoutParams);

                binding.resultLike.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    searchViewModel.toggleFavourites();
                });

                layout.addView(binding.getRoot());
                onCorrectResultListener.onCorrectResult(result);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        searchViewModel.updatePlaceResultState();
    }
}