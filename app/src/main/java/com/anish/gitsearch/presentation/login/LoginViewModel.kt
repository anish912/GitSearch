package com.anish.gitsearch.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anish.gitsearch.domain.usecase.IsUserAuthenticatedUseCase
import com.anish.gitsearch.domain.usecase.SignInWithGoogleUseCase
import com.anish.gitsearch.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val isUserAuthenticatedUseCase: IsUserAuthenticatedUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            isUserAuthenticatedUseCase().collectLatest { isAuthenticated ->
                _isAuthenticated.value = isAuthenticated
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            when (val result = signInWithGoogleUseCase(idToken)) {
                is Resource.Success -> {
                    _loginState.value = LoginState.Success
                }
                is Resource.Error -> {
                    _loginState.value = LoginState.Error(result.message ?: "Authentication failed")
                }
                is Resource.Loading -> {
                    _loginState.value = LoginState.Loading
                }
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}