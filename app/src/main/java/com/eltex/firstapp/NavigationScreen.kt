package com.eltex.firstapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.eltex.firstapp.feature.auth.ui.AuthScreenRoute
import com.eltex.firstapp.feature.main.MainScreen
import com.eltex.firstapp.feature.post.add.AddPostScreenRoute
import com.eltex.firstapp.feature.post.edit.EditEventScreenRoute
import com.eltex.firstapp.feature.post.ui.EventListMessage
import com.eltex.firstapp.feature.post.ui.EventListViewModel
import com.eltex.firstapp.feature.registration.ui.RegistrationScreenRoute
import kotlinx.serialization.Serializable

@Composable
fun NavigationScreen() {
    val navController = rememberNavController()
    val eventListViewModel: EventListViewModel = viewModel()

    NavHost(navController = navController, startDestination = Navigation.Main) {
        composable<Navigation.Main> {
            MainScreen(navController, eventListViewModel)
        }

        composable<Navigation.NewPost> {
            AddPostScreenRoute(
                onDone = { text ->
                    eventListViewModel.accept(EventListMessage.AddPost(text))
                    navController.popBackStack()
                },
            )
        }

        composable<Navigation.EditEvent> { backStackEntry ->
            val route = backStackEntry.toRoute<Navigation.EditEvent>()
            EditEventScreenRoute(
                eventId = route.id,
                listViewModel = eventListViewModel,
                onDone = { navController.popBackStack() },
            )
        }

        composable<Navigation.Authentication> {
            AuthScreenRoute(
                onBack = { navController.popBackStack() },
                onLoggedIn = { navController.popBackStack() },
                onNavigateToRegistration = {
                    navController.navigate(Navigation.Registration) {
                        popUpTo(Navigation.Authentication) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
            )
        }

        composable<Navigation.Registration> {
            RegistrationScreenRoute(
                onBack = { navController.popBackStack() },
                onRegistered = { navController.popBackStack() },
            )
        }
    }
}

@Serializable
sealed interface Navigation {
    @Serializable
    object Main: Navigation

    @Serializable
    object NewPost: Navigation

    @Serializable
    data class EditEvent(val id: Long): Navigation

    @Serializable
    object Authentication: Navigation

    @Serializable
    object Registration: Navigation
}