package com.clickandgo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.clickandgo.model.PlaceResult;
import com.clickandgo.repo.UserRepository;

import java.util.ArrayList;

public abstract class PlaceResultsViewModel extends ViewModel {
    protected MutableLiveData<ArrayList<PlaceResult>> placesLiveData;
    protected final UserRepository userRepository = new UserRepository();

    protected final ArrayList<PlaceResult> placeResults = new ArrayList<>();

    public void init() {
        if (placesLiveData != null) return;

        placesLiveData = new MutableLiveData<>();
        updatePlaceResults();
    }

    public MutableLiveData<ArrayList<PlaceResult>> getPlacesData() {
        return placesLiveData;
    }

    public abstract void updatePlaceResults();
}
