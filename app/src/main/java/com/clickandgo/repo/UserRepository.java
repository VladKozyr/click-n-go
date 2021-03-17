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

    private final LinkedList<DocumentReference> favourites = new LinkedList<>();
    private final LinkedList<DocumentReference> history = new LinkedList<>();
    private final MutableLiveData<List<DocumentReference>> docFavoritesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<DocumentReference>> docHistoryLiveData = new MutableLiveData<>();

    private boolean isUpdated = false;

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public DocumentReference getUserDocument(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(USER_COLLECTION).document(email);
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
        if (!favourites.isEmpty()) {
            docFavoritesLiveData.setValue(favourites);
            // update firebase
            if (isUpdated) updateFavourites();
            return docFavoritesLiveData;
        }
        getUserDocument().get().addOnSuccessListener(documentSnapshot -> {
            favourites.clear();
            favourites.addAll((Collection<? extends DocumentReference>) documentSnapshot.get(FAVOURITES));
            docFavoritesLiveData.setValue(favourites);
        });
        return docFavoritesLiveData;
    }

    public MutableLiveData<List<DocumentReference>> getHistory() {
        if (!history.isEmpty()) {
            docHistoryLiveData.setValue(history);
            return docHistoryLiveData;
        }

        getUserDocument().get().addOnSuccessListener(documentSnapshot -> {
            history.clear();
            history.addAll((Collection<? extends DocumentReference>) documentSnapshot.get(HISTORY));
            docHistoryLiveData.setValue(history);
        });
        return docHistoryLiveData;
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

    public void updateHistoryLocal() {
        docHistoryLiveData.postValue(history);
    }

    public void updateFavoritesLocal() {
        docFavoritesLiveData.postValue(favourites);
    }

    public void addFavourite(DocumentReference placeReference) {
        favourites.addFirst(placeReference);
        isUpdated = true;
        docFavoritesLiveData.postValue(favourites);
    }

    public void removeFavourite(DocumentReference placeReference) {
        favourites.remove(placeReference);
        isUpdated = true;
        docFavoritesLiveData.postValue(favourites);
    }

    public void pushToHistory(DocumentReference documentReference) {

    }
}
