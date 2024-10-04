package com.ktm.ksurvey.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktm.ksurvey.domain.repository.UserRepository
import com.ktm.ksurvey.domain.repository.result.Error
import com.ktm.ksurvey.domain.repository.result.Result
import com.ktm.ksurvey.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {

    private val userUseCase = UserUseCase(userRepository)

    var splashUiState = MutableStateFlow<SplashUiState>(SplashUiState.Default)
        private set

    private val _loginUiState: MutableStateFlow<LoginUiState> =
        MutableStateFlow(value = LoginUiState.Default)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    fun validateUser() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val result = userUseCase.validateUserToken()
                if (result) {
                    splashUiState.value = SplashUiState.Home
                } else {
                    splashUiState.value = SplashUiState.Login
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                when (val result = userUseCase.login(email, password)) {
                    is Result.Error -> {
                        when (val error = result.error) {
                            is Error.ApiError -> {
                                _loginUiState.value = LoginUiState.ErrorCode(error.errorCode)
                            }

                            is Error.GeneralError -> {
                                _loginUiState.value = LoginUiState.ErrorException(error.throwable)
                            }
                        }
                    }

                    is Result.Success -> {
                        _loginUiState.value = LoginUiState.Success
                    }
                }
            }
        }
    }
}

sealed class LoginUiState {
    data object Default : LoginUiState()
    data object Success : LoginUiState()
    data class ErrorCode(val errorCode: Int) : LoginUiState()
    data class ErrorException(val throwable: Throwable) : LoginUiState()
}

sealed class SplashUiState {
    data object Default : SplashUiState()
    data object Login : SplashUiState()
    data object Home : SplashUiState()
}