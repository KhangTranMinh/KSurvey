package com.ktm.ksurvey.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ktm.ksurvey.domain.entity.Survey
import com.ktm.ksurvey.domain.entity.User
import com.ktm.ksurvey.domain.repository.SurveyRepository
import com.ktm.ksurvey.domain.repository.UserRepository
import com.ktm.ksurvey.domain.repository.result.Error
import com.ktm.ksurvey.domain.repository.result.Result
import com.ktm.ksurvey.domain.usecase.SurveyUseCase
import com.ktm.ksurvey.domain.usecase.UserUseCase
import com.ktm.ksurvey.presentation.ui.pagination.SurveyPagingSource
import com.ktm.ksurvey.presentation.util.Const
import com.ktm.ksurvey.presentation.util.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    userRepository: UserRepository,
    surveyRepository: SurveyRepository
) : ViewModel() {

    private val userUseCase = UserUseCase(userRepository)
    private val surveyUseCase = SurveyUseCase(userRepository, surveyRepository)

    var homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Default)
        private set

    var surveysState = MutableStateFlow<PagingData<Survey>>(PagingData.empty())
        private set

    var userState = MutableStateFlow(User())
        private set

    fun getUser() {
        log("getUser")
        viewModelScope.launch {
            val user = withContext(Dispatchers.Default) {
                surveyUseCase.getUser()
            }
            user?.let {
                userState.value = user
            }
        }
    }

    fun setForceRefresh() {
        surveyUseCase.isForceRefresh = true
    }

    fun fetchSurveys() {
        log("fetchSurveys")
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    pageSize = Const.SURVEY_PAGE_SIZE,
                    initialLoadSize = Const.SURVEY_PAGE_SIZE,
                ),
                pagingSourceFactory = {
                    SurveyPagingSource(surveyUseCase)
                }
            ).flow.cachedIn(viewModelScope).collectLatest {
                surveysState.value = it
            }
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                surveyUseCase.clearSurveyData()
                userUseCase.clearUserData()
                homeUiState.value = HomeUiState.Logout
            }
        }
    }

    fun logout() {
        log("logout")
        viewModelScope.launch {
            homeUiState.value = HomeUiState.Loading
            val result = withContext(Dispatchers.Default) {
                userUseCase.logout()
            }
            when (result) {
                is Result.Error -> {
                    when (val error = result.error) {
                        is Error.ApiError -> {
                            homeUiState.value = HomeUiState.ErrorCode(error.errorCode)
                        }

                        is Error.GeneralError -> {
                            homeUiState.value = HomeUiState.ErrorException(error.throwable)
                        }
                    }
                }

                is Result.Success -> {
                    homeUiState.value = HomeUiState.Logout
                }
            }
        }
    }
}

sealed class HomeUiState {
    data object Default : HomeUiState()
    data object Loading : HomeUiState()
    data object Logout : HomeUiState()
    data class ErrorCode(val errorCode: Int) : HomeUiState()
    data class ErrorException(val throwable: Throwable) : HomeUiState()
}