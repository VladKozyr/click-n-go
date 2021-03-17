package com.clickandgo.viewmodel;

import androidx.lifecycle.LiveData;

import com.clickandgo.model.PlaceResult;

import java.util.List;

public class HistoryViewModel extends PlaceResultsViewModel {

    @Override
    public void updatePlaceResults() {
        //TODO add implementation
    }

    @Override
    public LiveData<List<PlaceResult>> getPlacesData() {
        return useCase.observeHistory();
    }
}
