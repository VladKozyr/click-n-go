package com.clickandgo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.clickandgo.model.PlaceResult;
import com.clickandgo.usecase.PlaceResultsUseCase;

import java.util.List;

public abstract class PlaceResultsViewModel extends ViewModel {

    protected final PlaceResultsUseCase useCase = PlaceResultsUseCase.getInstance();

    abstract public LiveData<List<PlaceResult>> getPlacesData();

    public abstract void updatePlaceResults();

    public void onWishlistToggle(PlaceResult result) {
        result.setLiked(!result.isLiked());
        if (result.isLiked()) {
            useCase.addFavourite(result);
        } else {
            useCase.removeFavourite(result);
        }
    }
}
