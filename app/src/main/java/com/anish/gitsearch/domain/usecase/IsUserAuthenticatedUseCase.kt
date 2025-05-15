package com.anish.gitsearch.domain.usecase

import com.anish.gitsearch.domain.repository.AuthRepository
import javax.inject.Inject

class IsUserAuthenticatedUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() = authRepository.isAuthenticated
}