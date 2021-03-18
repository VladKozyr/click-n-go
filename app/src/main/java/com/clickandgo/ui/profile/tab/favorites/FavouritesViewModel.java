package com.clickandgo.ui.profile.tab.favorites;

import androidx.lifecycle.LiveData;

import com.clickandgo.domain.model.PlaceResult;
import com.clickandgo.ui.profile.tab.PlacesResultsViewModel;

import java.util.List;

public class FavouritesViewModel extends PlacesResultsViewModel {

    @Override
    public void updatePlaceResults() {
        //TODO: add implementation
    }

    @Override
    public LiveData<List<PlaceResult>> getPlacesData() {
        return useCase.observeFavorites();
    }
}
