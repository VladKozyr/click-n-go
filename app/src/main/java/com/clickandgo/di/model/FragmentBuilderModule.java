package com.clickandgo.di.model;

import com.clickandgo.ui.profile.tab.favorites.FavoritesFragment;
import com.clickandgo.ui.profile.tab.history.HistoryFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract HistoryFragment contributeHistoryFragment();

    @ContributesAndroidInjector
    abstract FavoritesFragment contributeFavoritesFragment();
}
