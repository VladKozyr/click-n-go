package com.clickandgo.usecase;

import android.util.Log;

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

/**
 * Represent entry point to communicate over lists of {@link PlaceResult}, which
 * are could exist in to states: <b>FAVORITES</b> and <b>HISTORY</b>
 */
public class PlaceResultsUseCase {

    /* Singleton implementation */
    private static PlaceResultsUseCase instance;

    /* Singleton implementation */
    public static synchronized PlaceResultsUseCase getInstance() {
        if (instance == null) {
            instance = new PlaceResultsUseCase();
        }
        return instance;
    }

    /* Singleton implementation */
    private PlaceResultsUseCase() {
    }

    private final UserRepository repository = UserRepository.getInstance();
    private final LinkedList<PlaceResult> history = new LinkedList<>();
    private final LinkedList<PlaceResult> favourites = new LinkedList<>();

    private final MutableLiveData<List<PlaceResult>> favoritesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<PlaceResult>> historyLiveData = new MutableLiveData<>();

    public LiveData<List<PlaceResult>> observeFavorites() {
        return Transformations.switchMap(repository.getFavourites(), input -> {
            favourites.clear();
            for (DocumentReference result : input) {
                favourites.add(mapToPlaceResult(result, true));
            }
            favoritesLiveData.setValue(favourites);
            return favoritesLiveData;
        });
    }

    public LiveData<List<PlaceResult>> observeHistory() {
        return Transformations.switchMap(repository.getHistory(), input -> {
            history.clear();
            for (DocumentReference result : input) {
                history.add(mapToPlaceResult(result, isDocumentsPresentInFavorites(result)));
            }
            historyLiveData.setValue(history);
            return historyLiveData;
        });
    }

    /**
     * Modifies both (favorite and history) lists to change {@link PlaceResult#isLiked()}
     * flag in <b>true</b> and notifies live data about changes.
     *
     * @param data where flag must be updated.
     */
    public void addFavourite(PlaceResult data) {
        updateHistory(data, true);
        favourites.addFirst(data);
        favoritesLiveData.postValue(favourites);
        historyLiveData.postValue(history);
    }

    /**
     * Modifies both (favorite and history) lists to change {@link PlaceResult#isLiked()}
     * flag in <b>false</b> and notifies live data about changes.
     *
     * @param data where flag must be updated.
     */
    public void removeFavourite(PlaceResult data) {
        updateHistory(data, false);
        favourites.remove(data);
        favoritesLiveData.postValue(favourites);
        historyLiveData.postValue(history);
    }

    /**
     * Notifies repository to update {@link FirebaseStorage}
     */
    public void updateRemoteDataBase() {
        repository.updateFavourites(mapToDocumentReferenceList(favourites));
        repository.updateHistory(mapToDocumentReferenceList(history));
    }

    /**
     * Finds current place result in history list and modifies it {@link PlaceResult#isLiked()} flag.
     *
     * @param result  to find matches
     * @param isLiked new value of place's property
     */
    private void updateHistory(PlaceResult result, boolean isLiked) {
        int indexInHistory = history.indexOf(result);
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

    private List<DocumentReference> mapToDocumentReferenceList(List<PlaceResult> sourceList) {
        ArrayList<DocumentReference> resultList = new ArrayList<>(sourceList.size());
        for (PlaceResult source : sourceList) {
            resultList.add(mapFromPlaceResult(source));
        }
        return resultList;
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
