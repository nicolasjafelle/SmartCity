package test.citron.search.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import test.citron.search.view.SearchMapper

@Module
@InstallIn(ViewModelComponent::class)
object SearchHiltModule {
    @Provides
    @ViewModelScoped
    fun providesSearchMapper(@ApplicationContext context: Context): SearchMapper =
        SearchMapper(context.resources)
}
