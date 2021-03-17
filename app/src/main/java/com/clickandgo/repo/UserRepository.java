package com.clickandgo.repo;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserRepository {
    public static final String USER_COLLECTION = "users";

    public static final String NAME = "name";
    public static final String UID = "uid";
    public static final String PASSWORD = "password";

    public static final String FAVOURITES = "favourites";
    public static final String HISTORY = "history";

    private static UserRepository instance;

    private static LinkedList<DocumentReference> favourites;
    private static LinkedList<DocumentReference> history;

    private static boolean isUpdated = false;


    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public DocumentReference getUserDocument(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(USER_COLLECTION)
                .document(email);
    }

    public void addUserInfo(String name, String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put(NAME, name);
        userProfile.put(PASSWORD, password);
        userProfile.put(UID, user.getUid());
        userProfile.put(FAVOURITES, new ArrayList<>());
        userProfile.put(HISTORY, new ArrayList<>());

        getUserDocument(user.getEmail()).set(userProfile);
    }

    public DocumentReference getUserDocument() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return getUserDocument(user.getEmail());
    }

    public MutableLiveData<List<DocumentReference>> getFavourites() {
        MutableLiveData<List<DocumentReference>> placesList = new MutableLiveData<>();
        if (favourites != null) {
            placesList.setValue(favourites);
            if (isUpdated) updateFavourites();
            return placesList;
        }

        favourites = new LinkedList<>();

        getUserDocument().get().addOnSuccessListener(documentSnapshot -> {
            favourites.addAll((Collection<? extends DocumentReference>) documentSnapshot.get(FAVOURITES));
            placesList.setValue(favourites);
        });
        return placesList;
    }

    public MutableLiveData<List<DocumentReference>> getHistory() {
        MutableLiveData<List<DocumentReference>> placesList = new MutableLiveData<>();
        if (history != null) {
            placesList.setValue(history);
            return placesList;
        }

        history = new LinkedList<>();

        getUserDocument().get().addOnSuccessListener(documentSnapshot -> {
            history.addAll((Collection<? extends DocumentReference>) documentSnapshot.get(HISTORY));
            placesList.setValue(history);
        });
        return placesList;
    }

    public List<DocumentReference> getCachedFavourites() {
        return favourites;
    }

    private synchronized void updateFavourites() {
        getUserDocument().update(FAVOURITES, favourites);
        isUpdated = false;
    }

    private synchronized void updateHistory() {
        getUserDocument().update(HISTORY, history);
    }

    public void addFavourite(DocumentReference placeReference) {
        favourites.addFirst(placeReference);
        isUpdated = true;
    }

    public void removeFavourite(DocumentReference placeReference) {
        favourites.remove(placeReference);
        isUpdated = true;
    }

    public void pushToHistory(DocumentReference documentReference) {

    }
}
