package com.anish.gitsearch.domain.usecase

import com.anish.gitsearch.domain.repository.GithubRepository
import javax.inject.Inject

class GetUserRepositoriesUseCase @Inject constructor(
    private val repository: GithubRepository
) {
    operator fun invoke(forceRefresh: Boolean = false) =
        repository.getUserRepositories(forceRefresh)
}