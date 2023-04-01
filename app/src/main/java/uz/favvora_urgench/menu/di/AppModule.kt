package uz.favvora_urgench.menu.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.favvora_urgench.menu.db.AppDatabase
import uz.favvora_urgench.menu.network.AppService
import uz.favvora_urgench.menu.network.Server
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun getServer() = if (Server.IS_TESTER) Server.SERVER_DEVELOPMENT else Server.SERVER_PRODUCTION

    @Provides
    @Singleton
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(getServer())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun apiService(): AppService = getRetrofit().create(AppService::class.java)

    @Provides
    @Singleton
    fun appDatabase(context: Application) = AppDatabase.getAppDBInstance(context)

    @Provides
    @Singleton
    fun categoryDao(appDatabase: AppDatabase) = appDatabase.getCategoryDao()

    @Provides
    @Singleton
    fun mealsDao(appDatabase: AppDatabase) = appDatabase.getMealsDao()

    @Provides
    @Singleton
    fun sectionDao(appDatabase: AppDatabase) = appDatabase.getSectionDao()
}