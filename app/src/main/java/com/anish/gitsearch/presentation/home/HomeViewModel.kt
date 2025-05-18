package com.anish.gitsearch.presentation.home

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anish.gitsearch.domain.model.Repository
import com.anish.gitsearch.domain.repository.AuthRepository
import com.anish.gitsearch.domain.usecase.GetUserRepositoriesUseCase
import com.anish.gitsearch.domain.usecase.IsUserAuthenticatedUseCase
import com.anish.gitsearch.domain.usecase.SearchRepositoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.anish.gitsearch.domain.model.User
import com.anish.gitsearch.domain.repository.GithubRepository
import com.anish.gitsearch.domain.usecase.SignOutUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.anish.gitsearch.util.Resource

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserRepositoriesUseCase: GetUserRepositoriesUseCase,
    private val searchRepositoriesUseCase: SearchRepositoriesUseCase,
    private val isUserAuthenticatedUseCase: IsUserAuthenticatedUseCase,
    private val authRepository: AuthRepository,
    private val githubRepository: GithubRepository,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {
    // This is just a stub for now

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState = _homeState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    init {
        checkAuthState()
        loadCurrentUser()
        loadRepositories(false)
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            isUserAuthenticatedUseCase().collectLatest { isAuthentiCated ->
                _isAuthenticated.value = isAuthentiCated
            }
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            authRepository.currentUser.collectLatest { user ->
                _currentUser.value = user
            }
        }
    }

    fun loadRepositories(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            getUserRepositoriesUseCase(forceRefresh).collectLatest { result ->
                _homeState.value = when (result) {
                    is Resource.Success -> HomeState.Success(result.data ?: emptyList())
                    is Resource.Error -> HomeState.Error(result.message ?: "Unknown error")
                    is Resource.Loading -> HomeState.Loading
                }
            }
        }
    }

    fun searchRepositories(query: String) {
        _searchQuery.value = query

        if (query.isBlank()) {
            loadRepositories(false)
            return
        }

        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            searchRepositoriesUseCase(query).collectLatest { result ->
                _homeState.value = when (result) {
                    is Resource.Success -> HomeState.Success(result.data ?: emptyList())
                    is Resource.Error -> HomeState.Error(result.message ?: "Unknown error")
                    is Resource.Loading -> HomeState.Loading
                }
            }
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        loadRepositories(false)
    }

    fun signOut(){
        viewModelScope.launch {
            try {
                githubRepository.clearUserData()
                signOutUseCase()
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
    }



}

sealed class HomeState{
    object Loading : HomeState()
    data class Success(val data: List<Repository>) : HomeState()
    data class Error(val message: String) : HomeState()
}