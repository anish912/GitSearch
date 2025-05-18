package com.anish.gitsearch.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anish.gitsearch.domain.model.Repository
import com.anish.gitsearch.domain.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RepositoryDetailsViewModel @Inject constructor(
    private val githubRepository: GithubRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _repository = MutableStateFlow<Repository?>(null)
    val repository = _repository.asStateFlow()

    private val repoId: Long = savedStateHandle.get<Long>("repoId") ?: -1

    init {
        loadRepository()
    }

    private fun loadRepository() {
        if (repoId == -1L) return

        viewModelScope.launch {
            githubRepository.getUserRepositories(false).collectLatest { result ->
                if (result.data != null) {
                    _repository.value = result.data.find { it.id == repoId }
                }
            }
        }
    }
}