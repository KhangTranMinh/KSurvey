package com.ktm.ksurvey.di

import android.content.Context
import androidx.room.Room
import com.ktm.ksurvey.data.network.ApiConfig
import com.ktm.ksurvey.data.network.service.AuthService
import com.ktm.ksurvey.data.network.service.UserService
import com.ktm.ksurvey.data.storage.room.db.KSurveyDatabase
import com.ktm.ksurvey.data.storage.room.db.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ): KSurveyDatabase {
        return Room.databaseBuilder(
            context,
            KSurveyDatabase::class.java,
            "KSurveyDatabase"
        ).build()
    }

    @Provides
    fun provideUserDao(
        database: KSurveyDatabase
    ): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }
}