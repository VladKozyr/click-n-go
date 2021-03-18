package com.clickandgo.ui.profile.tab.history;

import androidx.lifecycle.LiveData;

import com.clickandgo.domain.model.PlaceResult;
import com.clickandgo.domain.usecase.PlaceResultsUseCase;
import com.clickandgo.ui.profile.tab.PlacesResultsViewModel;

import java.util.List;

import javax.inject.Inject;

public class HistoryViewModel extends PlacesResultsViewModel {

    @Inject
    public HistoryViewModel(PlaceResultsUseCase useCase) {
        super(useCase);
    }

    @Override
    public void updatePlaceResults() {
        //TODO add implementation
    }

    @Override
    public LiveData<List<PlaceResult>> getPlacesData() {
        return useCase.observeHistory();
    }
}
