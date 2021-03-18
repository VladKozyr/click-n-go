package com.clickandgo.di.model;

import com.clickandgo.ProfileActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract ProfileActivity contributeProfileActivity();
}
