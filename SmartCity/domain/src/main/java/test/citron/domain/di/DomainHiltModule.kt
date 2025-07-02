package test.citron.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import test.citron.domain.repository.CityRepository
import test.citron.domain.usecase.AddFavoriteUseCase
import test.citron.domain.usecase.FetchCityListUseCase
import test.citron.domain.usecase.GetCityUseCase
import test.citron.domain.usecase.RemoveFavoriteUseCase
import test.citron.domain.usecase.SearchCityUseCase

@Module
@InstallIn(ViewModelComponent::class)
internal object DomainHiltModule {

    @Provides
    @ViewModelScoped
    fun providesGetCityListUseCase(repository: CityRepository) = FetchCityListUseCase(repository)

    @Provides
    @ViewModelScoped
    fun providesSearchCityUseCase(repository: CityRepository) = SearchCityUseCase(repository)

    @Provides
    @ViewModelScoped
    fun providesAddFavoriteUseCase(repository: CityRepository) = AddFavoriteUseCase(repository)

    @Provides
    @ViewModelScoped
    fun providesRemoveFavoriteUseCase(repository: CityRepository) =
        RemoveFavoriteUseCase(repository)

    @Provides
    @ViewModelScoped
    fun providesGetCityUseCase(repository: CityRepository) = GetCityUseCase(repository)
}
