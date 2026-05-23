package com.eltex.firstapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.eltex.firstapp.feature.auth.ui.AuthScreenRoute
import com.eltex.firstapp.feature.event.data.EventsRepositoryImpl
import com.eltex.firstapp.feature.event.list.ui.EventListMessage
import com.eltex.firstapp.feature.event.list.ui.EventListViewModel
import com.eltex.firstapp.feature.main.MainScreen
import com.eltex.firstapp.feature.post.add.AddPostScreenRoute
import com.eltex.firstapp.feature.post.data.LocalPostsRepository
import com.eltex.firstapp.feature.post.edit.EditEventScreenRoute
import com.eltex.firstapp.feature.post.edit.EditPostScreenRoute
import com.eltex.firstapp.feature.post.ui.PostListMessage
import com.eltex.firstapp.feature.post.ui.PostListViewModel
import com.eltex.firstapp.feature.registration.ui.RegistrationScreenRoute
import kotlinx.serialization.Serializable

@Composable
fun NavigationScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val postListViewModel: PostListViewModel = viewModel {
        PostListViewModel(
            repository = LocalPostsRepository(context.applicationContext)
        )
    }

    val eventListViewModel: EventListViewModel = viewModel {
        EventListViewModel(
            repository = EventsRepositoryImpl()
        )
    }

    NavHost(navController = navController, startDestination = Navigation.Main) {
        composable<Navigation.Main> {
            MainScreen(navController, postListViewModel, eventListViewModel)
        }

        composable<Navigation.NewPost> {
            AddPostScreenRoute(
                onDone = { text ->
                    postListViewModel.accept(PostListMessage.AddPost(text))
                    navController.popBackStack()
                },
            )
        }

        composable<Navigation.NewEvent> {
            AddPostScreenRoute(
                onDone = { text ->
                    eventListViewModel.accept(EventListMessage.AddPost(text))
                    navController.popBackStack()
                },
            )
        }

        composable<Navigation.EditPost> { backStackEntry ->
            val route = backStackEntry.toRoute<Navigation.EditPost>()
            EditPostScreenRoute(
                postId = route.id,
                listViewModel = postListViewModel,
                onDone = { navController.popBackStack() },
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
    object Main : Navigation

    @Serializable
    object NewPost : Navigation

    @Serializable
    object NewEvent : Navigation

    @Serializable
    data class EditPost(val id: Long) : Navigation

    @Serializable
    data class EditEvent(val id: Long) : Navigation

    @Serializable
    object Authentication : Navigation

    @Serializable
    object Registration : Navigation
}
