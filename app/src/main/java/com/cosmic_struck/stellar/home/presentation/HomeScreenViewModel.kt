package com.cosmic_struck.stellar.home.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import com.cosmic_struck.stellar.common.util.Resource
import com.cosmic_struck.stellar.home.domain.usecases.GetClassroomUseCases
import com.cosmic_struck.stellar.home.domain.usecases.GetJoinedClassroomsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getJoinedClassroomsUseCases: GetJoinedClassroomsUseCases,
    private val getClassroomUseCases: GetClassroomUseCases,
    private val supabaseClient: SupabaseClient
): ViewModel() {

    private val _state = mutableStateOf(HomeScreenState())
    val state: State<HomeScreenState> = _state

    init {
        getJoinedClassrooms()
    }

//    fun getClassroomList(){
//        viewModelScope.launch {
//            val userId = supabaseClient.auth.retrieveUserForCurrentSession().id
//            getClassroomUseCases(userId).collect{it->
//                when(it){
//                    is Resource.Loading<*> -> _state.value = _state.value.copy(isLoading = true)
//
//                    is Resource.Error<*> -> _state.value = _state.value.copy(error = it.message)
//
//                    is Resource.Success<*> -> _state.value = _state.value.copy(classrooms = it.data ?: emptyList())
//                }
//            }
//        }
//    }

    fun getJoinedClassrooms(){
        viewModelScope.launch {
            val userId = supabaseClient.auth.retrieveUserForCurrentSession().id
            Log.d("UserId",userId.toString())
            getJoinedClassroomsUseCases(userId).collect { it->
                when (it){
                    is Resource.Loading<*> -> _state.value = _state.value.copy(isLoading = true)

                    is Resource.Error<*> -> _state.value = _state.value.copy(error = it.message, isLoading = false)

                    is Resource.Success<*> -> _state.value = _state.value.copy(joinedClassrooms = it.data?:emptyList(), isLoading = false)

                }
            }
        }
    }
    fun onToggle(ind: Int){
        if(ind == 0){
            _state.value = _state.value.copy(selected = Options.MODULES)
        }
        else{
            _state.value = _state.value.copy(selected = Options.CLASSROOM)
        }
    }

}