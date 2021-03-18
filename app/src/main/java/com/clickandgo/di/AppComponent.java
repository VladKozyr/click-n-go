package com.clickandgo.di;

import android.app.Application;

import com.clickandgo.App;
import com.clickandgo.di.model.ActivityBuilderModule;
import com.clickandgo.di.model.FragmentBuilderModule;
import com.clickandgo.di.model.ProfileModule;
import com.clickandgo.di.model.ViewModelModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ActivityBuilderModule.class,
        FragmentBuilderModule.class,
        ProfileModule.class,
        ViewModelModule.class}
)
public interface AppComponent extends AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
