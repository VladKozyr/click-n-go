package com.clickandgo.ui.navigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clickandgo.R;
import com.clickandgo.di.viewmodel.ViewModelFactory;

import javax.inject.Inject;

public class ChooseMoneyFragment extends ChooseFragment {

    public static final String KEY = "MONEY";

    public ChooseMoneyFragment() {
        super(R.id.chooseMoneyFragment, R.id.money_layout, KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_money, container, false);
    }
}