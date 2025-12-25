package com.cosmic_struck.stellar.auth.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosmic_struck.stellar.common.util.userId
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val supabaseClient: SupabaseClient
) : ViewModel() {

    val auth = supabaseClient.auth
    private val _state = mutableStateOf(AuthScreenState())
    val state: State<AuthScreenState> = _state

    fun signUpWithEmail(){
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            try {
                val user = auth.signUpWith(
                    provider = Email){
                    email = "lalitdhembre@gmail.com"
                    password = "lalit123"
                }
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "",
                    success = true
                )
            }catch (e: Exception){
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message.toString()
                )
                Log.d("VIEWMODEL",e.message.toString())
            }
        }
    }


    fun signInWithEmail(){
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            try {
                val user =  auth.signInWith(Email){
                    email = state.value.email
                    password = state.value.password
                }
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "",
                    success = true
                )
            }catch (e: Exception){
                _state.value.copy(
                    isLoading = false,
                    error = e.message.toString()
                )
                Log.d("VIEWMODEL",e.message.toString())
            }
        }
    }

    fun setEmailAddress(email: String){
        viewModelScope.launch {
            _state.value = _state.value.copy(
                email = email
            )
        }
    }

    fun setPassword(password: String){
        viewModelScope.launch {
            _state.value = _state.value.copy(
                password = password
            )
        }
    }

    fun signInWithGoogle(){
        viewModelScope.launch {
            val user = auth.signInWith(Google,
                redirectUrl = "https://cosmic_struck.com/home")
        }
    }
}
