package com.clickandgo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.clickandgo.model.PlaceResult;
import com.clickandgo.repo.UserRepository;

import java.util.ArrayList;

public abstract class PlaceResultsViewModel extends ViewModel {

    protected final MutableLiveData<ArrayList<PlaceResult>> placesLiveData = new MutableLiveData<>();
    protected final UserRepository repository = UserRepository.getInstance();
    protected final ArrayList<PlaceResult> placeResults = new ArrayList<>();

    public void init() {
        if (placesLiveData.getValue() != null) return;
        updatePlaceResults();
    }

    public MutableLiveData<ArrayList<PlaceResult>> getPlacesData() {
        return placesLiveData;
    }

    public abstract void updatePlaceResults();

    public void onWishlistToggle(PlaceResult result) {
        result.setLiked(!result.isLiked());
        if (result.isLiked()) {
            repository.addFavourite(result.getReference());
        } else {
            repository.removeFavourite(result.getReference());
        }
    }
}
