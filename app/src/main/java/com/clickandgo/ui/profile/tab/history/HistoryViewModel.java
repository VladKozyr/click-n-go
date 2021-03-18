package com.clickandgo.ui.profile.tab.history;

import androidx.lifecycle.LiveData;

import com.clickandgo.domain.model.PlaceResult;
import com.clickandgo.ui.profile.tab.PlacesResultsViewModel;

import java.util.List;

public class HistoryViewModel extends PlacesResultsViewModel {

    @Override
    public void updatePlaceResults() {
        //TODO add implementation
    }

    @Override
    public LiveData<List<PlaceResult>> getPlacesData() {
        return useCase.observeHistory();
    }
}
