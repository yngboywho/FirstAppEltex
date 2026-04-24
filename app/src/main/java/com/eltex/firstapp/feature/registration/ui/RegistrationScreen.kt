package com.eltex.firstapp.feature.registration.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.eltex.firstapp.feature.registration.domain.LoginEmpty
import com.eltex.firstapp.feature.registration.domain.PasswordEmpty
import com.eltex.firstapp.feature.registration.domain.PasswordMismatch
import com.eltex.firstapp.ui.theme.FirstAppTheme


@Composable
fun RegistrationScreenRoute(
   onRegistered: () -> Unit = {},
   onBack: () -> Unit = {},
   modifier: Modifier = Modifier,
   viewModel: RegistrationViewModel = viewModel(),
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                RegistrationEffect.NavigateToFeed -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.registration_success),
                        Toast.LENGTH_SHORT,
                    ).show()
                    onRegistered()
                }
            }
        }
    }

    RegistrationScreen(
        state = viewModel.state,
        modifier = modifier,
        onMessage = viewModel::accept,
        onBack = onBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    state: RegistrationState,
    modifier: Modifier = Modifier,
    onMessage: (RegistrationMessage) -> Unit = {},
    onBack: () -> Unit = {},
) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isConfirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.registration_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        }
    ) { innerPadding ->
        Column (
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.login,
                onValueChange = { value ->
                    if (value.length <= 32) onMessage(RegistrationMessage.LoginChanged(value))
                },
                isError = state.loginError != null,
                singleLine = true,
                label = { Text(stringResource(R.string.login_hint)) },
                supportingText = { Text(state.loginError.toReadableString().orEmpty()) },
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.name,
                onValueChange = { onMessage(RegistrationMessage.NameChanged(it)) },
                singleLine = true,
                label = { Text(stringResource(R.string.name_hint)) },
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.password,
                onValueChange = { onMessage(RegistrationMessage.PasswordChanged(it)) },
                isError = state.passwordError != null,
                singleLine = true,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                label = { Text(stringResource(R.string.password_hint)) },
                supportingText = {
                    Text(state.passwordError.toReadableString().orEmpty())
                },
                trailingIcon = {
                    PasswordVisibilityToggle(
                        visible = isPasswordVisible,
                        onToggle = { isPasswordVisible = !isPasswordVisible },
                    )
                },
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.confirmPassword,
                onValueChange = { onMessage(RegistrationMessage.ConfirmPasswordChanged(it)) },
                isError = state.confirmPasswordError != null,
                singleLine = true,
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                label = { Text(stringResource(R.string.confirm_password_hint)) },
                supportingText = {
                    Text(state.confirmPasswordError.toReadableString().orEmpty())
                },
                trailingIcon = {
                    PasswordVisibilityToggle(
                        visible = isConfirmPasswordVisible,
                        onToggle = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
                    )
                },
            )

            Spacer(Modifier.height(8.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onMessage(RegistrationMessage.Submit) },
            ) {
                Text(stringResource(R.string.register))
            }
        }
    }
}

@Composable
private fun PasswordVisibilityToggle(visible: Boolean, onToggle: () -> Unit){
    val (icon, desc) = if (visible) {
        Icons.Filled.VisibilityOff to stringResource(R.string.hide_password_description)
    } else {
        Icons.Filled.Visibility to stringResource(R.string.show_password_description)
    }

    IconButton(onClick = onToggle) {
        Icon(imageVector = icon, contentDescription = desc)
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    FirstAppTheme {
        RegistrationScreen(
            RegistrationState(
                name = "dfroero",
                password = "wewe432ifew",
                confirmPassword = "wewe432ifew",
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenErrorPreview() {
    FirstAppTheme {
        RegistrationScreen(
            RegistrationState(
                loginError = LoginEmpty,
                passwordError = PasswordEmpty,
                confirmPasswordError = PasswordMismatch,
            )
        )
    }
}