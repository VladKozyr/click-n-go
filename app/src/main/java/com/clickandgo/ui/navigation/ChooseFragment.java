package com.clickandgo.ui.navigation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.clickandgo.R;
import com.clickandgo.databinding.OptionBinding;
import com.clickandgo.di.viewmodel.ViewModelFactory;
import com.clickandgo.domain.model.Option;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public abstract class ChooseFragment extends DaggerFragment {

    @IdRes
    private final int menuId;
    @IdRes
    private final int layoutId;
    private LinearLayout layout;
    private List<Option> options;

    public final String MENU_KEY;

    @Inject
    public ViewModelFactory factory;
    public SearchViewModel searchViewModel;

    public ChooseFragment(@IdRes int menuId, @IdRes int layoutId, String menu_key) {
        this.menuId = menuId;
        this.layoutId = layoutId;
        MENU_KEY = menu_key;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        options = new ArrayList<>();
        searchViewModel = ViewModelProviders.of(requireActivity(), factory).get(SearchViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout = getView().findViewById(layoutId);

        searchViewModel.getSelectedOption(MENU_KEY).observe(getViewLifecycleOwner(), selectedOption -> {
            for (Option option : options) {
                if (option.equals(selectedOption)) {
                    selectedOption.setChecked(true);
                } else if (option.getChecked()) {
                    option.setChecked(false);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        SubMenu subMenu = menu.findItem(menuId).getSubMenu();
        createBindViews(subMenu);
        Option selected = searchViewModel.getSelectedOption(MENU_KEY).getValue();
        if (selected != null) {
            int index = options.indexOf(selected);
            if (index != -1) options.get(index).setChecked(true);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void createBindViews(SubMenu subMenu) {

        for (int i = 0; i < subMenu.size(); i++) {
            MenuItem menuItem = subMenu.getItem(i);
            OptionBinding binding = OptionBinding.inflate(getLayoutInflater(), layout, false);
            Option option = new Option(
                    (String) menuItem.getTitle(),
                    (String) menuItem.getTitleCondensed(),
                    menuItem.getIcon(),
                    MENU_KEY
            );
            binding.setOption(option);
            binding.setPresenter(this);
            layout.addView(binding.cardView);
            options.add(option);
        }
    }

    public void onOptionClick(Option option) {
        searchViewModel.selectOption(option);
    }
}
