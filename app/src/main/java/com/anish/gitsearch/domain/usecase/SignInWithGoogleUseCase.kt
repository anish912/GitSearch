package com.anish.gitsearch.domain.usecase

import com.anish.gitsearch.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String) = authRepository.signInWithGoogle(idToken)
}
