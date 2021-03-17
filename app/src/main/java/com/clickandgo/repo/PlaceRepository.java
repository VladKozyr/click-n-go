package com.clickandgo.repo;

import android.content.Context;

import com.clickandgo.model.PlaceResult;

import java.util.ArrayList;

public class PlaceRepository {

    private static PlaceRepository instance;
    private ArrayList<PlaceResult> places = new ArrayList<>();

    public static synchronized PlaceRepository getInstance(Context context) {
        if (instance == null) {
            instance = new PlaceRepository();
        }
        return instance;
    }
}
