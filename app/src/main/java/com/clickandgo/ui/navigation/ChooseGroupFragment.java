package com.clickandgo.ui.navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.clickandgo.R;
import com.clickandgo.di.viewmodel.ViewModelFactory;

import javax.inject.Inject;


public class ChooseGroupFragment extends ChooseFragment {

    public ChooseGroupFragment() {
        super(R.id.chooseGroupFragment, R.id.group_layout, "GROUP");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_group, container, false);
    }
}