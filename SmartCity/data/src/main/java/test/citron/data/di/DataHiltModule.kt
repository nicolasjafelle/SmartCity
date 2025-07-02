package test.citron.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton
import test.citron.data.CityRepositoryImpl
import test.citron.data.api.CityApiClient
import test.citron.data.local.CityLocalStorage
import test.citron.data.local.FavoriteLocalStorage
import test.citron.data.local.InMemoryCityStorage
import test.citron.data.local.PreferenceCityStorage
import test.citron.domain.repository.CityRepository

@Module
@InstallIn(SingletonComponent::class)
internal object DataHiltModule {
    @Provides
    fun providesApiClient(httpClient: HttpClient): CityApiClient =
        CityApiClient.CityApiClientImpl(httpClient)

    @Provides
    @Singleton
    fun providesCitiLocalStorage(): CityLocalStorage = InMemoryCityStorage()

    @Provides
    @Singleton
    fun providesFavoriteLocalStorage(@ApplicationContext context: Context): FavoriteLocalStorage {
        val sharedPref = context.getSharedPreferences("favorite_preferences", Context.MODE_PRIVATE)
        return PreferenceCityStorage(sharedPref)
    }

    @Provides
    @Singleton
    fun providesCityRepository(
        apiClient: CityApiClient,
        localStorage: CityLocalStorage,
        favoriteLocalStorage: FavoriteLocalStorage
    ): CityRepository = CityRepositoryImpl(
        apiClient = apiClient,
        localDataSource = localStorage,
        favoriteLocalStorage = favoriteLocalStorage
    )
}
