package com.anish.gitsearch.data.remote

data class RepositoryDto(
    val id: Long,
    val name: String,
    val description: String?,
    val stargazers_count: Int,
    val language: String?,
    val owner: OwnerDto
)

data class OwnerDto(
    val login: String,
    val avatar_url: String
)

data class SearchResultDto(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<RepositoryDto>
)
