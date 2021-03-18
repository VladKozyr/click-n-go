package com.clickandgo.di;

import com.clickandgo.ui.profile.tab.favorites.FavouritesViewModel;
import com.clickandgo.ui.profile.tab.history.HistoryViewModel;

import dagger.Component;

@Component
public interface AppComponent {

    void inject(FavouritesViewModel viewModel);

    void inject(HistoryViewModel viewModel);
}
