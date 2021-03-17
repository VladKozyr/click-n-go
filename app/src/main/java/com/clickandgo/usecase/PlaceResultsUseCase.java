package com.clickandgo.usecase;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.clickandgo.model.PlaceResult;
import com.clickandgo.repo.UserRepository;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PlaceResultsUseCase {

    private static PlaceResultsUseCase instance;

    public static synchronized PlaceResultsUseCase getInstance() {
        if (instance == null) {
            instance = new PlaceResultsUseCase();
        }
        return instance;
    }

    private PlaceResultsUseCase() { }

    private final UserRepository repository = UserRepository.getInstance();
    private final LinkedList<PlaceResult> history = new LinkedList<>();
    private final LinkedList<PlaceResult> favourites = new LinkedList<>();

    private final MutableLiveData<List<PlaceResult>> favoritesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<PlaceResult>> historyLiveData = new MutableLiveData<>();

    public LiveData<List<PlaceResult>> observeFavorites() {
        return Transformations.switchMap(
                repository.getFavourites(),
                (Function<List<DocumentReference>, LiveData<List<PlaceResult>>>) input -> {
                    favourites.clear();
                    for (DocumentReference result : input) {
                        favourites.add(mapToPlaceResult(result, true));
                    }
                    favoritesLiveData.setValue(favourites);
                    return favoritesLiveData;
                });
    }

    public LiveData<List<PlaceResult>> observeHistory() {
        return Transformations.switchMap(
                repository.getHistory(),
                (Function<List<DocumentReference>, LiveData<List<PlaceResult>>>) input -> {
                    history.clear();
                    for (DocumentReference result : input) {
                        history.add(mapToPlaceResult(result, isDocumentsPresentInFavorites(result)));
                    }
                    historyLiveData.setValue(history);
                    return historyLiveData;
                });
    }

    public void addFavourite(PlaceResult data) {
        updateHistory(data, true);
        favourites.addFirst(data);
        favoritesLiveData.postValue(favourites);
        historyLiveData.postValue(history);
    }

    public void removeFavourite(PlaceResult data) {
        updateHistory(data, false);
        favourites.remove(data);
        favoritesLiveData.postValue(favourites);
        historyLiveData.postValue(history);
    }

    public void updateRemoteDataBase() {
        ArrayList<DocumentReference> historyRef = new ArrayList<>(history.size());
        ArrayList<DocumentReference> favoritesRef = new ArrayList<>(favourites.size());
        for (PlaceResult result : history) {
            historyRef.add(mapFromPlaceResult(result));
        }
        for (PlaceResult result : favourites) {
            favoritesRef.add(mapFromPlaceResult(result));
        }
        repository.updateFavourites(favoritesRef);
        repository.updateHistory(historyRef);
    }

    private void updateHistory(PlaceResult data, boolean isLiked) {
        int indexInHistory = history.indexOf(data);
        if (indexInHistory != -1) {
            PlaceResult resultInHistory = history.get(indexInHistory);
            resultInHistory.setLiked(isLiked);
        } else {
            Log.w("UPDATE_HISTORY", "Item in FAVORITES isn't exist in HISTORY");
        }
    }

    private PlaceResult mapToPlaceResult(DocumentReference reference, boolean defaultLike) {
        String[] fields = reference.getPath().split("/");
        PlaceResult placeResult = new PlaceResult(fields[3], fields[1], fields[2], reference, defaultLike);

        reference.get().addOnSuccessListener(documentSnapshot -> {
            placeResult.setMap(documentSnapshot.getString("link"));
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("places")
                .child(fields[3] + ".jpg");
        storageReference.getDownloadUrl()
                .addOnSuccessListener(placeResult::setImageUri)
                .addOnFailureListener(e -> {
                    Log.d("PHOTO", e.getMessage());
                });
        return placeResult;
    }

    private DocumentReference mapFromPlaceResult(PlaceResult result) {
        return result.getReference();
    }

    private boolean isDocumentsPresentInFavorites(DocumentReference reference) {
        boolean isExist = false;
        for (PlaceResult result : favourites) {
            isExist = result.getReference().equals(reference);
            if (isExist) {
                break;
            }
        }
        Log.w("TEST", "isExist " + isExist);
        return isExist;
    }
}
