package com.eltex.firstapp.feature.auth.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eltex.firstapp.R
import com.eltex.firstapp.feature.auth.domain.AuthLoginEmpty
import com.eltex.firstapp.feature.auth.domain.AuthPasswordEmpty
import com.eltex.firstapp.ui.theme.FirstAppTheme

@Composable
fun AuthScreenRoute(
    modifier: Modifier = Modifier,
    onLoggedIn: () -> Unit = {},
    onBack: () -> Unit = {},
    onNavigateToRegistration: () -> Unit = {},
    viewModel: AuthViewModel = viewModel(),
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                AuthEffect.ShowSuccess -> {
                    Toast.makeText(context, context.getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                    onLoggedIn()
                }
            }
        }
    }

    AuthScreen(
        state = viewModel.state,
        modifier = modifier,
        onEvent = viewModel::accept,
        onBack = onBack,
        onNavigateToRegistration = onNavigateToRegistration,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    state: AuthState,
    modifier: Modifier = Modifier,
    onEvent: (AuthMessage) -> Unit = {},
    onBack: () -> Unit = {},
    onNavigateToRegistration: () -> Unit = {},
) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.auth_title)) },
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
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.login,
                onValueChange = { onEvent(AuthMessage.LoginChanged(it)) },
                isError = state.loginError != null,
                singleLine = true,
                label = { Text(stringResource(R.string.login_hint)) },
                supportingText = { Text(state.loginError.toReadableString().orEmpty()) },
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.password,
                onValueChange = { onEvent(AuthMessage.PasswordChanged(it)) },
                isError = state.passwordError != null,
                singleLine = true,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                label = { Text(stringResource(R.string.password_hint)) },
                supportingText = { Text(state.passwordError.toReadableString().orEmpty()) },
                trailingIcon = {
                    val (icon, desc) = if (isPasswordVisible)
                        Icons.Filled.VisibilityOff to stringResource(R.string.hide_password_description)
                    else
                        Icons.Filled.Visibility to stringResource(R.string.show_password_description)
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(imageVector = icon, contentDescription = desc)
                    }
                },
            )

            Spacer(Modifier.height(8.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onEvent(AuthMessage.Submit) },
            ) {
                Text(stringResource(R.string.login))
            }

            Spacer(Modifier.height(8.dp))

            TextButton(onClick = onNavigateToRegistration) {
                Text(stringResource(R.string.no_account_register))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenEmptyPreview() {
    FirstAppTheme { AuthScreen(AuthState()) }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenErrorPreview() {
    FirstAppTheme {
        AuthScreen(AuthState(loginError = AuthLoginEmpty, passwordError = AuthPasswordEmpty))
    }
}