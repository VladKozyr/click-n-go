package com.clickandgo.repo;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Random;

public class PlaceRepository {

    public static final String PLACES_COLLECTION = "places";
    public static final String AMOUNT = "amount";
    public static final String TYPE = "type";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static PlaceRepository instance;

    private boolean isUpdated = false;

    public static synchronized PlaceRepository getInstance() {
        if (instance == null) {
            instance = new PlaceRepository();
        }
        return instance;
    }

    //TODO put real params
    public MutableLiveData<DocumentReference> searchForRandomPlace(String type, String group, String money, String place) {
        MutableLiveData<DocumentReference> referenceMutableLiveData = new MutableLiveData<>();
        Log.d("SEARCH", type + " " + group + " " + money + " " + place);
        db.collection(PLACES_COLLECTION)
                .document(place)
                .collection(money)
                .whereArrayContains(TYPE, type)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                    if (documentSnapshots.size() == 0) {
                        referenceMutableLiveData.setValue(null);
                        return;
                    }

                    int randomIndex = new Random().nextInt(documentSnapshots.size());
                    DocumentReference reference = documentSnapshots.get(randomIndex).getReference();
                    referenceMutableLiveData.setValue(reference);
                    Log.d("SEARCH", "END");
                    //referenceMutableLiveData.setValue(null);
                });
        return referenceMutableLiveData;
    }
}
