package test.citron.maps.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import test.citron.maps.view.MapsMapper

@Module
@InstallIn(ViewModelComponent::class)
internal class MapsHiltModule {

    @Provides
    @ViewModelScoped
    fun providesMapsMapper(): MapsMapper = MapsMapper()
}
