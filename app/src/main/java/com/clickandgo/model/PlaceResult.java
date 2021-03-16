package com.clickandgo.model;

import java.util.List;

public class PlaceResult {
    private String name;

    public PlaceResult(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<PlaceResult> getUserFavorites() {
        return null;
    }

}
