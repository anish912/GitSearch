package com.anish.gitsearch.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repositories")
data class RepositoryEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String?,
    val starCount: Int,
    val language: String?,
    val ownerLogin: String,
    val ownerAvatarUrl: String,
    val timestamp: Long = System.currentTimeMillis()
)