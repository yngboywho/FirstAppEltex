package com.eltex.firstapp.feature.post.add

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eltex.firstapp.R

@Composable
fun AddPostScreenRoute(
    onDone: (String) -> Unit = {},
    viewModel: AddPostViewModel = viewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.effects.collect { event ->
            when (event) {
                is AddPostEffect.Saved -> onDone(event.text)
            }
        }
    }

    AddPostScreen(
        state = viewModel.state,
        addPostHandler = viewModel::accept,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen (
    state: AddPostState,
    addPostHandler: (AddPostMessage) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                navigationIcon = {
                    val backPressedDispatcherOwner = LocalOnBackPressedDispatcherOwner.current
                    IconButton(onClick =  {
                        backPressedDispatcherOwner?.onBackPressedDispatcher?.onBackPressed()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        addPostHandler(AddPostMessage.Save)
                    }) {
                        Icon(Icons.Default.Check, null)
                    }
                }
            )
        }
    ) {
        TextField(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            value = state.text,
            onValueChange = {
                addPostHandler(AddPostMessage.TextChanged(it))
            },
        )
    }
}