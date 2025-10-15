package com.teddy.mirandaytoledo.auth.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teddy.mirandaytoledo.auth.domain.LoginDataSource
import com.teddy.mirandaytoledo.auth.domain.User
import com.teddy.mirandaytoledo.core.domain.util.onError
import com.teddy.mirandaytoledo.core.domain.util.onSuccess
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginDataSource: LoginDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _events = Channel<LoginEvents>()

    val events = _events.receiveAsFlow()
    fun onUsernameChanged(newValue: String) {
        _state.update {
            _state.value.copy(
                username = newValue
            )
        }
    }

    fun onPasswordChanged(newValue: String) {
        _state.update {
            _state.value.copy(
                password = newValue
            )
        }
    }


    fun login() {
        viewModelScope.launch {
            _state.update { state.value.copy(isLoading = true) }

            val result = loginDataSource.doLogin(
                user = User(
                    username = _state.value.username,
                    password = _state.value.password
                )
            )

            result.onSuccess { success ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = success,
                    )
                }
            }
            result.onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false
                    )
                }
                Log.i("ERROR LOG", error.toString())
                _events.send(LoginEvents.Error(error))
            }
        }

    }
}