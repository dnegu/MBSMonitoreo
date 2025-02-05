package com.dnegusoft.mbsmonitoreo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dnegusoft.mbsmonitoreo.R
import com.dnegusoft.mbsmonitoreo.components.MBSActionButton
import com.dnegusoft.mbsmonitoreo.model.LoginAction
import com.dnegusoft.mbsmonitoreo.model.LoginState
import com.dnegusoft.mbsmonitoreo.ui.theme.NeutralMedium
import com.dnegusoft.mbsmonitoreo.ui.theme.PrimarioMedium
import com.dnegusoft.mbsmonitoreo.ui.theme.SecundarioDark
import com.dnegusoft.mbsmonitoreo.viewmodel.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    LoginScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                LoginAction.OnLoginClick -> {
                    onLoginSuccess()
                }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .clip(RoundedCornerShape(bottomEnd = 15.dp, bottomStart = 15.dp))
                .background(SecundarioDark),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 48.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.width(200.dp).align(Alignment.CenterHorizontally)
                )
                Text(
                    text = stringResource(R.string.slogan),
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth().weight(0.6f).padding(32.dp)
        ) {
            Text(
                text = stringResource(R.string.user),
                color = Color(0xFF9C9C9C),
                fontWeight = FontWeight.Medium
            )
            OutlinedTextField(
                value = state.user,
                onValueChange = { onAction(LoginAction.OnUserChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = NeutralMedium,
                    focusedBorderColor = PrimarioMedium,
                    cursorColor = PrimarioMedium
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.password),
                color = Color(0xFF9C9C9C),
                fontWeight = FontWeight.Medium
            )
            OutlinedTextField(
                value = state.password,
                onValueChange = { onAction(LoginAction.OnPasswordChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = NeutralMedium,
                    focusedBorderColor = PrimarioMedium,
                    cursorColor = PrimarioMedium
                ),
                visualTransformation = if (!state.isPasswordVisible) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onAction(LoginAction.OnTogglePasswordVisibility)
                        }
                    ) {
                        Icon(
                            painter = if (state.isPasswordVisible) {
                                painterResource(R.drawable.visibility_off)
                            } else {
                                painterResource(R.drawable.visibility_on)
                            },
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            MBSActionButton(
                text = stringResource(R.string.login),
                isLoading = state.isLoggingIn,
                enabled = state.canLogin && !state.isLoggingIn,
                onClick = { onAction(LoginAction.OnLoginClick) }
            )
        }
    }

}