package com.anish.gitsearch.domain.usecase

import com.anish.gitsearch.domain.repository.GithubRepository
import javax.inject.Inject


class SearchRepositoriesUseCase @Inject constructor(
    private val repository: GithubRepository
) {
    operator fun invoke(query: String) = repository.searchRepositories(query)
}