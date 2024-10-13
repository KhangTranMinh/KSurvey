package com.ktm.ksurvey.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktm.ksurvey.domain.repository.SurveyRepository
import com.ktm.ksurvey.domain.repository.UserRepository
import com.ktm.ksurvey.domain.repository.result.Error
import com.ktm.ksurvey.domain.repository.result.Result
import com.ktm.ksurvey.domain.usecase.SurveyUseCase
import com.ktm.ksurvey.domain.usecase.UserUseCase
import com.ktm.ksurvey.presentation.util.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    userRepository: UserRepository,
    surveyRepository: SurveyRepository
) : ViewModel() {

    private val userUseCase = UserUseCase(userRepository)
    private val surveyUseCase = SurveyUseCase(userRepository, surveyRepository)

    var splashUiState = MutableStateFlow<SplashUiState>(SplashUiState.Default)
        private set

    var loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Default)
        private set

    var resetPasswordUiState = MutableStateFlow<ResetPasswordUiState>(ResetPasswordUiState.Default)
        private set

    fun validateUser() {
        log("validateUser")
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val result = userUseCase.validateUserToken()
                if (result) {
                    splashUiState.value = SplashUiState.Home
                } else {
                    surveyUseCase.clearSurveyData()
                    userUseCase.clearUserData()
                    splashUiState.value = SplashUiState.Login
                }
            }
        }
    }

    fun login(email: String, password: String) {
        log("login($email, $password)")
        viewModelScope.launch {
            loginUiState.value = LoginUiState.Loading
            withContext(Dispatchers.Default) {
                when (val result = userUseCase.login(email, password)) {
                    is Result.Error -> {
                        when (val error = result.error) {
                            is Error.ApiError -> {
                                loginUiState.value = LoginUiState.ErrorCode(error.errorCode)
                            }

                            is Error.GeneralError -> {
                                loginUiState.value = LoginUiState.ErrorException(error.throwable)
                            }
                        }
                    }

                    is Result.Success -> {
                        loginUiState.value = LoginUiState.Success
                    }
                }
            }
        }
    }

    fun resetPassword(email: String) {
        log("resetPassword($email)")
        viewModelScope.launch {
            resetPasswordUiState.value = ResetPasswordUiState.Loading
            withContext(Dispatchers.Default) {
                when (val result = userUseCase.resetPassword(email)) {
                    is Result.Error -> {
                        when (val error = result.error) {
                            is Error.ApiError -> {
                                resetPasswordUiState.value =
                                    ResetPasswordUiState.ErrorCode(error.errorCode)
                            }

                            is Error.GeneralError -> {
                                resetPasswordUiState.value =
                                    ResetPasswordUiState.ErrorException(error.throwable)
                            }
                        }
                    }

                    is Result.Success -> {
                        resetPasswordUiState.value = ResetPasswordUiState.Success(result.data)
                    }
                }
            }
        }
    }
}

sealed class LoginUiState {
    data object Default : LoginUiState()
    data object Loading : LoginUiState()
    data object Success : LoginUiState()
    data class ErrorCode(val errorCode: Int) : LoginUiState()
    data class ErrorException(val throwable: Throwable) : LoginUiState()
}

sealed class ResetPasswordUiState {
    data object Default : ResetPasswordUiState()
    data object Loading : ResetPasswordUiState()
    data class Success(val message: String) : ResetPasswordUiState()
    data class ErrorCode(val errorCode: Int) : ResetPasswordUiState()
    data class ErrorException(val throwable: Throwable) : ResetPasswordUiState()
}

sealed class SplashUiState {
    data object Default : SplashUiState()
    data object Login : SplashUiState()
    data object Home : SplashUiState()
}