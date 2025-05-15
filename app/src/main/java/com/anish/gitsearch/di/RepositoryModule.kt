package com.anish.gitsearch.di

import com.anish.gitsearch.data.repository.AuthRepositoryImpl
import com.anish.gitsearch.data.repository.GithubRepositoryImpl
import com.anish.gitsearch.domain.repository.AuthRepository
import com.anish.gitsearch.domain.repository.GithubRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGithubRepository(
        githubRepositoryImpl: GithubRepositoryImpl
    ): GithubRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}