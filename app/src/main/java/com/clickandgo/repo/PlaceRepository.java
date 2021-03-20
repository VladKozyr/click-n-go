package com.clickandgo.repo;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
    public MutableLiveData<DocumentReference> searchForRandomPlace() {
        MutableLiveData<DocumentReference> referenceMutableLiveData = new MutableLiveData<>();
        db.collection(PLACES_COLLECTION)
                .document("Контрактова площа")
                .collection("300")
                .whereArrayContains(AMOUNT, 2)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                    if (documentSnapshots.size() == 0) return;

                    int randomIndex = new Random().nextInt(documentSnapshots.size());
                    DocumentReference reference = documentSnapshots.get(randomIndex).getReference();
                    referenceMutableLiveData.setValue(reference);
                    //referenceMutableLiveData.setValue(null);
                });
        return referenceMutableLiveData;
    }
}
