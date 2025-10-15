package com.teddy.mirandaytoledo.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teddy.mirandaytoledo.R
import com.teddy.mirandaytoledo.core.presentation.util.ObserveAsEvent
import com.teddy.mirandaytoledo.core.presentation.util.toString
import com.teddy.mirandaytoledo.ui.theme.MirandaytoledoTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
    onSuccessLogin: () -> Unit,

    ) {
    val state: LoginState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    ObserveAsEvent(events = viewModel.events) { events ->
        when (events) {
            is LoginEvents.Error -> {
                Toast.makeText(
                    context,
                    events.error.toString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    var passwordVisibility by remember { mutableStateOf(false) }
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFD4A200),
            Color(0xFFFFDD00),
            Color(0xFFFFF689)
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(brush = gradientBrush),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = modifier.weight(0.3f))
        if (state.isSuccess) {
            onSuccessLogin()
        } else {
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(10))
                    .background(color = Color.White)
                    .shadow(elevation = 1.dp)
                    .padding(vertical = 20.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = modifier.size(200.dp),
                        painter = painterResource(id = R.drawable.logologin),
                        contentDescription = "LOGO",
                        tint = Color.Black
                    )
                    Text(
                        text = stringResource(R.string.miranda_y_toledo),
                        modifier = modifier,
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = modifier.height(12.dp))
                    TextField(
                        modifier = modifier.padding(horizontal = 12.dp),
                        value = state.username,
                        onValueChange = viewModel::onUsernameChanged,
                        label = { Text(text = stringResource(R.string.login_username_textfield)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null
                            )
                        })
                    Spacer(modifier = modifier.height(12.dp))
                    TextField(
                        modifier = modifier.padding(horizontal = 12.dp),
                        value = state.password,
                        onValueChange = { viewModel.onPasswordChanged(newValue = it) },
                        label = { Text(text = stringResource(R.string.login_password_textfield)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null
                            )
                        },
                        visualTransformation = if (passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    passwordVisibility = !passwordVisibility
                                },
                                modifier = modifier
                            ) {
                                Icon(
                                    imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null
                                )
                            }
                        })
                    Spacer(modifier = modifier.height(12.dp))
                    if (state.isLoading) {
                        CircularProgressIndicator()
                    }
                    Button(
                        modifier = modifier.fillMaxWidth(0.5f),
                        onClick = viewModel::login,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            modifier = modifier.padding(4.dp),
                            text = stringResource(R.string.login_accept_button)
                        )
                    }
                }

            }
            Spacer(modifier = modifier.weight(0.6f))
        }
    }
}


@Preview
@Composable
fun LoginScreenPreview() {
    MirandaytoledoTheme {
        LoginScreen(onSuccessLogin = {})
    }
}