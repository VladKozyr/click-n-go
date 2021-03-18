package com.clickandgo.ui.profile.tab;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.clickandgo.domain.model.PlaceResult;
import com.clickandgo.domain.usecase.PlaceResultsUseCase;

import java.util.List;

public abstract class PlacesResultsViewModel extends ViewModel {

    protected final PlaceResultsUseCase useCase;

    public PlacesResultsViewModel(PlaceResultsUseCase useCase) {
        this.useCase = useCase;
    }

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
