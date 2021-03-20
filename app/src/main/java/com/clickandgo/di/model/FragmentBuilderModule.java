package com.clickandgo.di.model;

import com.clickandgo.ui.navigation.ChooseGroupFragment;
import com.clickandgo.ui.navigation.ChooseMoneyFragment;
import com.clickandgo.ui.navigation.ChoosePlaceFragment;
import com.clickandgo.ui.navigation.ChooseResultFragment;
import com.clickandgo.ui.navigation.ChooseTypeFragment;
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

    @ContributesAndroidInjector
    abstract ChooseMoneyFragment contributeChooseMoneyFragment();

    @ContributesAndroidInjector
    abstract ChooseTypeFragment contributeChooseTypeFragment();

    @ContributesAndroidInjector
    abstract ChooseGroupFragment contributeChooseGroupFragment();

    @ContributesAndroidInjector
    abstract ChoosePlaceFragment contributeChoosePlaceFragment();

    @ContributesAndroidInjector
    abstract ChooseResultFragment contributeChooseResultFragment();
}
