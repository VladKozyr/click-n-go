package com.clickandgo.ui.navigation;

import android.os.Bundle;
import android.util.Log;
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

import com.clickandgo.R;
import com.clickandgo.databinding.OptionBinding;
import com.clickandgo.model.Option;

import java.util.ArrayList;
import java.util.List;

public class ChooseFragment extends Fragment {

    @IdRes
    private final int menuId;
    @IdRes
    private final int layoutId;

    private LinearLayout layout;

    public ChooseFragment(@IdRes int menuId, @IdRes int layoutId) {
        this.menuId = menuId;
        this.layoutId = layoutId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("ONCREATE", "FRAGMENT");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout = getView().findViewById(layoutId);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        List<OptionBinding> bindingList = createBindViews(menu);
        addMenuObservables(bindingList);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void addMenuObservables(List<OptionBinding> bindingList) {
        for (OptionBinding binding : bindingList) {
            binding.cardView.setOnClickListener(v -> {
                MenuItem menuItem = binding.getOption().getMenuItem();
                //TODO go to next fragment
                if (!menuItem.isChecked()) {
                    menuItem.setChecked(true);
                    for (OptionBinding bind : bindingList) {
                        Option tempOption = bind.getOption();
                        tempOption.setChecked(tempOption.getMenuItem().isChecked());
                    }
                }
            });
        }
    }

    private List<OptionBinding> createBindViews(Menu menu) {
        SubMenu subMenu = menu.findItem(menuId).getSubMenu();
        List<OptionBinding> bindingList = new ArrayList<>();

        for (int i = 0; i < subMenu.size(); i++) {
            MenuItem menuItem = subMenu.getItem(i);
            OptionBinding binding = OptionBinding.inflate(getLayoutInflater(), layout, false);
            binding.setOption(
                    new Option(
                            (String) menuItem.getTitle(),
                            (String) menuItem.getTitleCondensed(),
                            menuItem.getIcon(),
                            menuItem
                    )
            );
            layout.addView(binding.cardView);
            bindingList.add(binding);
        }
        return bindingList;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            Log.d("MENU_ITEM", String.valueOf(savedInstanceState.get("SELECTED_MENU_ITEM")));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("SAVE", "INSTANCE");
        outState.putInt("SELECTED_MENU_ITEM", 1);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
}
