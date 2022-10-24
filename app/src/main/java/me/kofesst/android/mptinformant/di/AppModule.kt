package me.kofesst.android.mptinformant.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import me.kofesst.android.mptinformant.BuildConfig
import me.kofesst.android.mptinformant.data.remote.InformantApiService
import me.kofesst.android.mptinformant.data.repositories.ChangesRepositoryImpl
import me.kofesst.android.mptinformant.data.repositories.DepartmentsRepositoryImpl
import me.kofesst.android.mptinformant.data.repositories.PreferencesRepositoryImpl
import me.kofesst.android.mptinformant.data.repositories.ScheduleRepositoryImpl
import me.kofesst.android.mptinformer.domain.repositories.ChangesRepository
import me.kofesst.android.mptinformer.domain.repositories.DepartmentsRepository
import me.kofesst.android.mptinformer.domain.repositories.PreferencesRepository
import me.kofesst.android.mptinformer.domain.repositories.ScheduleRepository
import me.kofesst.android.mptinformer.domain.usecases.UseCases
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideInformantApiService(): InformantApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(InformantApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("app_preferences")
        }
    }

    @Provides
    @Singleton
    fun provideDepartmentsRepository(apiService: InformantApiService): DepartmentsRepository {
        return DepartmentsRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideScheduleRepository(apiService: InformantApiService): ScheduleRepository {
        return ScheduleRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideChangesRepository(apiService: InformantApiService): ChangesRepository {
        return ChangesRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(dataStore: DataStore<Preferences>): PreferencesRepository {
        return PreferencesRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideUseCases(
        departmentsRepository: DepartmentsRepository,
        scheduleRepository: ScheduleRepository,
        changesRepository: ChangesRepository,
        preferencesRepository: PreferencesRepository,
    ): UseCases {
        return UseCases(
            departmentsRepository = departmentsRepository,
            scheduleRepository = scheduleRepository,
            changesRepository = changesRepository,
            preferencesRepository = preferencesRepository
        )
    }
}
