package com.example.afbe.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.afbe.R
import com.example.afbe.utils.ClaveSolLoader
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(viewModel: LoginViewModel, onLoginSuccess: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.login_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            LoginForm(viewModel, onLoginSuccess)
        }
    }
}


@Composable
fun LoginForm(viewModel: LoginViewModel, onLoginSuccess: () -> Unit) {
    val email by viewModel.email.observeAsState(initial = "")
    val password by viewModel.password.observeAsState(initial = "")
    val loginEnabled by viewModel.loginEnabled.observeAsState(initial = false)
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()

    var passwordVisible by remember { mutableStateOf(false) }

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ClaveSolLoader()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(200.dp))

            Image(
                painter = painterResource(id = R.drawable.afbelogo),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))

            TransparentTextField(
                value = email,
                placeholder = "E-mail",
                onValueChange = { viewModel.onLoginChanged(it, password) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            TransparentTextField(
                value = password,
                placeholder = "Password",
                onValueChange = { viewModel.onLoginChanged(email, it) },
                isPassword = true,
                passwordVisible = passwordVisible,
                onVisibilityToggle = { passwordVisible = !passwordVisible }
            )

            Spacer(modifier = Modifier.height(16.dp))

            GradientButton("Login", enabled = loginEnabled) {
                coroutineScope.launch {
                    viewModel.onLoginSelected(onLoginSuccess) {}
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransparentTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onVisibilityToggle: (() -> Unit)? = null
) {
    val visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None

    val trailingIcon: (@Composable () -> Unit)? = if (isPassword && onVisibilityToggle != null) {
        {
            IconButton(onClick = onVisibilityToggle) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    tint = White
                )
            }
        }
    } else null

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder, color = White.copy(alpha = 0.7f)) },
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true,
        maxLines = 1,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        keyboardOptions = if (placeholder.contains("mail", ignoreCase = true))
            KeyboardOptions(keyboardType = KeyboardType.Email)
        else KeyboardOptions.Default,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = White.copy(alpha = 0.1f),
            unfocusedContainerColor = White.copy(alpha = 0.1f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = White,
            unfocusedTextColor = White,
            cursorColor = White
        )
    )
}



@Composable
fun GradientButton(text: String, enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFFB5F5DC), Color(0xFF77F0B1))
                    ),
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}
