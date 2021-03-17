package com.clickandgo.model;

import android.graphics.drawable.Drawable;
import android.view.MenuItem;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class Option extends BaseObservable {
    public String name;
    public String description;
    public Drawable icon;
    private final MenuItem menuItem;
    private boolean isChecked;


    public Option(String name, String description, Drawable icon, MenuItem menuItem) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.menuItem = menuItem;
    }

    @Bindable
    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean flag) {
        if (flag != isChecked) {
            isChecked = flag;
            notifyPropertyChanged(BR.checked);
        }
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }
}
