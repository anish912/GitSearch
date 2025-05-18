package com.anish.gitsearch.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object RepositoryDetails : Screen("repository/{repoId}"){
        fun createRoute(repoId: Long) = "repository/$repoId"
    }
}