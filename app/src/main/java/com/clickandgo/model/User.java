package com.clickandgo.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {

    public static final String USER_COLLECTION = "users";

    public static final String NAME = "name";
    public static final String UID = "uid";
    public static final String PASSWORD = "password";
    public static final String FAVOURITES = "favourites";
    public static final String HISTORY = "history";

    public static DocumentReference getUserDocument(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(USER_COLLECTION)
                .document(email);
    }

    public static void addUserInfoToFireStore(String name, String password) {
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

    public static DocumentReference getUserDocument() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return getUserDocument(user.getEmail());
    }
}
