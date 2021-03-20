package com.clickandgo.ui.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.clickandgo.R;
import com.clickandgo.di.viewmodel.ViewModelFactory;

import javax.inject.Inject;

public class ChooseTypeFragment extends ChooseFragment {

    public ChooseTypeFragment() {
        super(R.id.chooseTypeFragment, R.id.type_layout, "TYPE");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_type, container, false);
    }
}