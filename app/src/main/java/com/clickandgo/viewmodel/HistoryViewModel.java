package com.clickandgo.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.clickandgo.model.PlaceResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class HistoryViewModel extends PlaceResultsViewModel {

    @Override
    public void updatePlaceResults() {
        MutableLiveData<List<DocumentReference>> placesReferences = userRepository.getHistory();
        placeResults.clear();

        placesReferences.observeForever(favourites -> {
            List<DocumentReference> cachedFavourites = userRepository.getCachedFavourites();
            for (DocumentReference reference : favourites) {

                String[] fields = reference.getPath().split("/");
                boolean isLiked = cachedFavourites.contains(reference);
                PlaceResult placeResult = new PlaceResult(fields[3], fields[1], fields[2], reference, isLiked);

                reference.get().addOnSuccessListener(documentSnapshot -> {
                    placeResult.setMap(documentSnapshot.getString("link"));
                });

                placeResults.add(placeResult);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                        .child("places")
                        .child(fields[3] + ".jpg");
                storageReference.getDownloadUrl()
                        .addOnSuccessListener(placeResult::setImageUri)
                        .addOnFailureListener(e -> {
                            Log.d("PHOTO", e.getMessage());
                        });
            }
            placesLiveData.setValue(placeResults);
        });
    }
}
