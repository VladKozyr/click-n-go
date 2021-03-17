package com.clickandgo.repo;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
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

    /*
           if (!favourites.isEmpty()) {
            docFavoritesLiveData.setValue(favourites);
            // update firebase
            if (isUpdated) updateFavourites();
            return docFavoritesLiveData;
        }
     */
    public MutableLiveData<List<DocumentReference>> getFavourites() {
        getUserDocument().get().addOnSuccessListener(documentSnapshot -> {
            docFavoritesLiveData.setValue((ArrayList<DocumentReference>) documentSnapshot.get(FAVOURITES));
        });
        return docFavoritesLiveData;
    }

    /*
         if (!history.isEmpty()) {
            docHistoryLiveData.setValue(history);
            return docHistoryLiveData;
        }
     */
    public MutableLiveData<List<DocumentReference>> getHistory() {
        getUserDocument().get().addOnSuccessListener(documentSnapshot -> {
            docHistoryLiveData.setValue((ArrayList<DocumentReference>) documentSnapshot.get(HISTORY));
        });
        return docHistoryLiveData;
    }

     public synchronized void updateFavourites(List<DocumentReference> list) {
        getUserDocument().update(FAVOURITES, list);
        isUpdated = false;
    }

    public  synchronized void updateHistory(List<DocumentReference> list) {
        getUserDocument().update(HISTORY, list);
        isUpdated = false;
    }

    public void pushToHistory(DocumentReference documentReference) {

    }
}
