package com.eltex.firstapp.feature.post.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eltex.firstapp.R
import com.eltex.firstapp.feature.post.ui.EventListMessage
import com.eltex.firstapp.feature.post.ui.EventListViewModel
import com.eltex.firstapp.ui.theme.FirstAppTheme

@Composable
fun EditEventScreenRoute(
    eventId: Long,
    listViewModel: EventListViewModel,
    onDone: () -> Unit = {},
) {
    val event = listViewModel.findById(eventId)
    if (event == null) {
        LaunchedEffect(Unit) { onDone() }
        return
    }

    val editViewModel: EditEventViewModel = viewModel(
        factory = EditEventViewModelFactory(id = event.id, content = event.content)
    )

    LaunchedEffect(Unit) {
        editViewModel.effects.collect { effect ->
            when (effect) {
                is EditPostEffect.NavigateBack -> {
                    listViewModel.accept(
                        EventListMessage.SaveEdited(effect.id, effect.content)
                    )
                    onDone()
                }
            }
        }
    }

    EditEventScreen(
        state = editViewModel.state,
        onMessage = editViewModel::accept,
        onBack = onDone,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventScreen(
    state: EditPostState,
    modifier: Modifier = Modifier,
    onMessage: (EditPostMessage) -> Unit = {},
    onBack: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_event_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                value = state.content,
                onValueChange = { onMessage(EditPostMessage.ContentChanged(it)) },
                label = { Text(stringResource(R.string.event_content_hint)) },
                minLines = 5,
            )

            Spacer(Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onMessage(EditPostMessage.Save) },
            ) {
                Text(stringResource(R.string.save))
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditEventScreenPreview() {
    FirstAppTheme {
        EditEventScreen(
            state = EditPostState(
                id = 1L,
                content = "Приглашаю провести уютный вечер за настолками!",
            )
        )
    }
}