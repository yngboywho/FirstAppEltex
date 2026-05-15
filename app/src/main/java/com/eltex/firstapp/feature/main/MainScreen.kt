package com.eltex.firstapp.feature.main

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.eltex.firstapp.Navigation
import com.eltex.firstapp.R
import com.eltex.firstapp.feature.event.list.ui.EventListScreenRoute
import com.eltex.firstapp.feature.event.list.ui.EventListViewModel
import com.eltex.firstapp.feature.post.ui.PostListScreenRoute
import com.eltex.firstapp.feature.post.ui.PostListViewModel
enum class Tab(
    @param:StringRes val titleRes: Int,
    val icon: ImageVector
) {
    Posts(R.string.tab_posts, Icons.Default.RssFeed),
    Events(R.string.tab_events, Icons.Default.Event),
    Users(R.string.tab_users, Icons.Default.People),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController = rememberNavController(),
    postListViewModel: PostListViewModel,
    eventListViewModel: EventListViewModel,
) {
    var selectedTab by rememberSaveable { mutableStateOf(Tab.Posts) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Navigation.Authentication)
                    }) {
                        Icon(Icons.Default.AccountCircle, null)
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar {
                Tab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                tab.icon,
                                contentDescription = stringResource(tab.titleRes)
                            )
                        },
                        label = { Text(stringResource(tab.titleRes)) }
                    )
                }
            }
        },
        floatingActionButton = {
            when (selectedTab) {
                Tab.Posts -> {
                    FloatingActionButton(onClick = {
                        navController.navigate(Navigation.NewPost)
                    }) {
                        Icon(Icons.Default.Add, null)
                    }
                }

                Tab.Events -> {
                    FloatingActionButton(onClick = {
                        navController.navigate(Navigation.NewEvent)
                    }) {
                        Icon(Icons.Default.Add, null)
                    }
                }

                Tab.Users -> Unit
            }
        }
    ) { insets ->

        Crossfade(modifier = Modifier.padding(insets), targetState = selectedTab) { tab ->
            when (tab) {
                Tab.Posts -> PostListScreenRoute(
                    viewModel = postListViewModel,
                    onEditPost = { id -> navController.navigate(Navigation.EditPost(id)) },
                )

                Tab.Events -> EventListScreenRoute(
                    viewModel = eventListViewModel,
                    onEditEvent = { id -> navController.navigate(Navigation.EditEvent(id)) },
                )

                Tab.Users -> Unit
            }
        }
    }
}

