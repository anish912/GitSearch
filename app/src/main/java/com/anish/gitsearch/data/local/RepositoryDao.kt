package com.anish.gitsearch.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepositories(repositories: List<RepositoryEntity>)

    @Query("SELECT * FROM repositories ORDER BY timestamp DESC")
    fun getRepositories(): Flow<List<RepositoryEntity>>

    @Query("SELECT * FROM repositories WHERE name LIKE :query OR description LIKE :query")
    fun searchRepositories(query: String): Flow<List<RepositoryEntity>>

    @Query("DELETE FROM repositories WHERE ownerLogin = :username")
    suspend fun clearUserRepositories(username: String)
}