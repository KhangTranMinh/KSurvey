package com.ktm.ksurvey.presentation.di

import com.ktm.ksurvey.data.repository.SurveyRepositoryImpl
import com.ktm.ksurvey.data.repository.UserRepositoryImpl
import com.ktm.ksurvey.data.storage.SurveyStore
import com.ktm.ksurvey.data.storage.UserStore
import com.ktm.ksurvey.data.storage.room.RoomSurveyStore
import com.ktm.ksurvey.data.storage.room.RoomUserStore
import com.ktm.ksurvey.domain.repository.SurveyRepository
import com.ktm.ksurvey.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    abstract fun bindUserStore(impl: RoomUserStore): UserStore

    @Binds
    abstract fun bindSurveyStore(impl: RoomSurveyStore): SurveyStore

    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindSurveyRepository(impl: SurveyRepositoryImpl): SurveyRepository
}