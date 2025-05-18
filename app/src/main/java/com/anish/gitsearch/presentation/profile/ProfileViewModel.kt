package com.anish.gitsearch.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anish.gitsearch.domain.model.User
import com.anish.gitsearch.domain.repository.AuthRepository
import com.anish.gitsearch.domain.repository.GithubRepository
import com.anish.gitsearch.domain.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val githubRepository: GithubRepository,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            authRepository.currentUser.collectLatest { user ->
                _user.value = user
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                githubRepository.clearUserData()
                signOutUseCase()
            } catch (e: Exception) {
                // Handle error if needed
                Timber.e(e, "Error signing out")
            } finally {
                _isLoading.value = false
            }
        }
    }
}