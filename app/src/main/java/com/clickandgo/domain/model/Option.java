package com.clickandgo.domain.model;

import android.graphics.drawable.Drawable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.Objects;

public class Option extends BaseObservable {
    public String name;
    public String description;
    public Drawable icon;
    private boolean isChecked;

    private final String menuKey;
    private final String postValue;

    public Option(String name, String description, Drawable icon, String menuKey, String postValue) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.menuKey = menuKey;
        this.postValue = postValue;
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

    public String getPostValue() {
        return postValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option option = (Option) o;
        return name.equals(option.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getMenuKey() {
        return menuKey;
    }

    @Override
    public String toString() {
        return "Option{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", icon=" + icon +
                ", isChecked=" + isChecked +
                ", menuKey='" + menuKey + '\'' +
                '}';
    }
}
