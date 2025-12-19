package com.cosmic_struck.stellar.homeScreen.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val supabaseClient: SupabaseClient
) : ViewModel(){

    init {
        val auth = supabaseClient.auth
        viewModelScope.launch {
            val user = auth.retrieveUserForCurrentSession(updateSession = true)
            Log.d("HomeScreenViewModel", "User: $user")
        }
    }
}