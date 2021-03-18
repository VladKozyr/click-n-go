package com.clickandgo.di.model;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.clickandgo.di.viewmodel.ViewModelKey;
import com.clickandgo.ui.profile.tab.favorites.FavoritesViewModel;
import com.clickandgo.ui.profile.tab.history.HistoryViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(model = HistoryViewModel.class)
    public abstract ViewModel provideHistoryViewModel(HistoryViewModel model);

    @Binds
    @IntoMap
    @ViewModelKey(model = FavoritesViewModel.class)
    public abstract ViewModel provideFavoritesViewModel(FavoritesViewModel model);

    @Binds
    public abstract ViewModelProvider.Factory provideViewModelFactory(ViewModelProvider.Factory factory);
}
