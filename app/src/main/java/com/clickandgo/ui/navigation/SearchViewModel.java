package com.clickandgo.ui.navigation;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.clickandgo.domain.model.Option;

import java.util.HashMap;

public class ChooseViewModel extends ViewModel {
    private final HashMap<String, MutableLiveData<Option>> searchOptionsData;
    private final MutableLiveData<String> placeData;

    public ChooseViewModel() {
        this.searchOptionsData = new HashMap<>();
        this.placeData = new MutableLiveData<>();
    }

    public void selectOption(Option option) {
        MutableLiveData<Option> selected = searchOptionsData.get(option.getMenuKey());
        if (selected != null) {
            selected.setValue(option);
        } else {
            Log.d("OPTION", "Incorrect menu item");
        }
    }

    public LiveData<Option> getSelectedOption(String menuKey) {
        if (searchOptionsData.containsKey(menuKey)) return searchOptionsData.get(menuKey);

        MutableLiveData<Option> optionLiveData = new MutableLiveData<>();
        searchOptionsData.put(menuKey, optionLiveData);
        return optionLiveData;
    }

    public LiveData<String> getPlace() {
        return placeData;
    }

    public void setPlace(String place) {
        placeData.setValue(place);
    }
}
