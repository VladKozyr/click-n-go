package com.clickandgo.di.model;

import com.clickandgo.domain.usecase.PlaceResultsUseCase;
import com.clickandgo.repo.PlaceRepository;
import com.clickandgo.repo.UserRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ProfileModule {

    @Singleton
    @Provides
    public UserRepository provideUserRepository() {
        return new UserRepository();
    }

    @Singleton
    @Provides
    public PlaceRepository providePlaceRepository() {
        return new PlaceRepository();
    }

    @Singleton
    @Provides
    public PlaceResultsUseCase provideUseCase(UserRepository repository, PlaceRepository placeRepository) {
        return new PlaceResultsUseCase(repository, placeRepository);
    }
}
