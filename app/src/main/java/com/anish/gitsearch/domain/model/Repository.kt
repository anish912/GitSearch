package com.anish.gitsearch.domain.model

data class Repository(
    val id: Long,
    val name: String,
    val description: String?,
    val starCount: Int,
    val language: String?,
    val owner: Owner
)