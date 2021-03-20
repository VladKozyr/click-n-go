package com.clickandgo.ui.navigation;

import android.location.Address;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.clickandgo.domain.model.Option;
import com.clickandgo.domain.model.PlaceResult;
import com.clickandgo.domain.usecase.PlaceResultsUseCase;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class SearchViewModel extends ViewModel {

    private final HashMap<String, MutableLiveData<Option>> searchOptionsData;
    private final MutableLiveData<Address> placeData;
    private String place;

    private LiveData<PlaceResult> currentResult;

    private final PlaceResultsUseCase useCase;

    @Inject
    public SearchViewModel(PlaceResultsUseCase useCase) {
        this.useCase = useCase;
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

    public LiveData<PlaceResult> getSearchResult() {
        currentResult = useCase.getSearchResult(
                getParamOrNull(ChooseTypeFragment.KEY),
                getParamOrNull(ChooseGroupFragment.KEY),
                getParamOrNull(ChooseMoneyFragment.KEY),
                place
        );
        return currentResult;
    }

    private String getParamOrNull(String key) {
        MutableLiveData<Option> data = searchOptionsData.get(key);
        String res = null;
        if (data != null && data.getValue() != null) {
            res = data.getValue().getPostValue();
        }
        return res;
    }

    public LiveData<Address> getPlace() {
        return placeData;
    }

    public void setPlace(String place, Address address) {
        placeData.setValue(address);
        this.place = place;
    }

    public void toggleFavourites() {
        PlaceResult results = currentResult.getValue();
        if (!results.isLiked()) {
            useCase.addSingleFavourite(results);
        } else {
            useCase.removeSingleFavourite(results);
        }
    }

    public void updatePlaceResultState() {
        PlaceResult result = currentResult.getValue();
        if (result != null)
            result.setLiked(useCase.isDocumentsPresentInFavorites(result.getReference()));
    }

    public boolean isAllOptions() {
        boolean flag = searchOptionsData.size() == 3 && place != null;

        if (!flag) return false;

        for (Map.Entry<String, MutableLiveData<Option>> entry : searchOptionsData.entrySet()) {
            if (entry.getValue().getValue() == null) {
                flag = false;
                break;
            }
        }
        return flag;
    }
}
