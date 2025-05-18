package com.anish.gitsearch.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anish.gitsearch.presentation.details.RepositoryDetailsScreen
import com.anish.gitsearch.presentation.home.HomeScreen
import com.anish.gitsearch.presentation.login.LoginScreen
import com.anish.gitsearch.presentation.login.LoginViewModel
import com.anish.gitsearch.presentation.profile.ProfileScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val isAuthenticated by loginViewModel.isAuthenticated.collectAsState()

    // Determine start destination based on authentication state
    val actualStartDestination = if (isAuthenticated) {
        Screen.Home.route
    } else {
        startDestination
    }
    Log.d("anish", "isAuthenticated: $isAuthenticated")

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = actualStartDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = loginViewModel,
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onNavigateToDetails = {  repository->
                    navController.navigate(Screen.RepositoryDetails.createRoute(repository.id))
                }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onSignOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Screen.RepositoryDetails.route,
            arguments = listOf(
                navArgument(name = "repoId") {
                    type = NavType.LongType
                }
            )
        ) {
            RepositoryDetailsScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
            )


        }
    }
}