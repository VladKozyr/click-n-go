package com.clickandgo.domain.model;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;

import java.util.Objects;

public class PlaceResult extends BaseObservable {

    private String name;
    private String place;
    private String price;
    private Uri mapUri;
    private Uri imageUri;
    private boolean isLiked;

    private DocumentReference reference;

    public PlaceResult(String name, String place, String price, DocumentReference reference, boolean isLiked) {
        this.name = name;
        this.place = place;
        this.price = price;
        this.isLiked = isLiked;
        this.reference = reference;
    }

    public PlaceResult(String name, String place, String price, DocumentReference reference) {
        this(name, place, price, reference, false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public DocumentReference getReference() {
        return reference;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPrice() {
        return "~₴" + price;
    }

    public Uri getMapUri() {
        return mapUri;
    }

    public void setMap(String mapPath) {
        mapUri = Uri.parse(mapPath);
    }

    @Bindable
    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
        notifyPropertyChanged(BR.liked);
    }

    @Bindable
    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
        notifyPropertyChanged(BR.imageUri);
    }

    @BindingAdapter({"imageUri", "placeholder"})
    public static void bindImage(ImageView view, Uri imageUri, Drawable placeholder) {
        if (imageUri == null) {
            view.setImageDrawable(placeholder);
            return;
        }
        Glide.with(view.getContext())
                .load(imageUri)
                .into(view);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceResult result = (PlaceResult) o;
        return Objects.equals(reference, result.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference);
    }
}
