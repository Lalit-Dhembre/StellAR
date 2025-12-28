package com.cosmic_struck.stellar.classroom.presentation.viewmodel.delegate

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.cosmic_struck.stellar.classroom.domain.GetClassroomDetailsUseCase
import com.cosmic_struck.stellar.classroom.domain.GetClassroomMemberUseCase
import com.cosmic_struck.stellar.classroom.domain.GetClassroomModelListUseCase
import com.cosmic_struck.stellar.classroom.presentation.viewmodel.ClassroomHomeScreenState
import com.cosmic_struck.stellar.classroom.presentation.viewmodel.Options
import com.cosmic_struck.stellar.common.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ClassroomHomeScreenDelegate @Inject constructor(
    private val getClassroomModelListUseCase: GetClassroomModelListUseCase,
    private val getClassroomDetailsUseCase: GetClassroomDetailsUseCase,
    private val getClassroomMemberUseCase: GetClassroomMemberUseCase,
    private val savedStateHandle: SavedStateHandle
){
    private val _state = MutableStateFlow(ClassroomHomeScreenState())
    val state : MutableStateFlow<ClassroomHomeScreenState> = _state

    init {
        val classroomId = savedStateHandle.get<String>("classroom_id")
        if (classroomId != null){
            _state.value = _state.value.copy(classroom_id = classroomId)
        }
    }



    fun onToggle(ind: Int){
        if(ind == 0){
            _state.value = _state.value.copy(selected = Options.MEMBERS)
        }
        else{
            _state.value = _state.value.copy(selected = Options.MODELS)
        }
    }

    suspend fun joinClassroom(joinCode: String){

    }

    fun getClassroomId(){
        val id = savedStateHandle.get<String>("classroom_id")
        id?.let { _state.value = _state.value.copy(classroom_id = it) }
    }

    suspend fun getClassroomModelList(){
            getClassroomModelListUseCase(state.value.classroom_id).collect { it ->
                when(it){
                    is Resource.Loading<*> -> {_state.value = _state.value.copy(isLoading = true)}

                    is Resource.Error<*> -> {_state.value = _state.value.copy(isLoading = false, error = it.message)}

                    is Resource.Success<*> -> {
                        _state.value = _state.value.copy(
                            classroomModelsList = it.data ?: emptyList(),
                            isLoading = false
                        )
                        Log.d("ClassroomHomeViewModel",it.data.toString())
                    }
            }
        }
    }
    suspend fun getClassroomDetails(){
            getClassroomDetailsUseCase(state.value.classroom_id).collect { it->
                when(it) {
                    is Resource.Loading<*> -> {_state.value = _state.value.copy(isLoading = true)}

                    is Resource.Error<*> -> {_state.value = _state.value.copy(isLoading = false, error = it.message)}

                    is Resource.Success<*> -> {_state.value = _state.value.copy(classroomName = it.data?.classroom.toString(), classroomAuthor = it.data?.author.toString(), isLoading = false)}
                }
        }
    }
    suspend fun getClassroomMembers(){
            getClassroomMemberUseCase(state.value.classroom_id).collect{
                when(it){
                    is Resource.Loading<*> -> {_state.value = _state.value.copy(isLoading = true)}

                    is Resource.Error<*> -> {_state.value = _state.value.copy(isLoading = false, error = it.message)}

                    is Resource.Success<*> -> {_state.value = _state.value.copy(classroomMembersList = it.data ?: emptyList(), classroomMembers = it.data?.size.toString(), isLoading = false)}
                }
            }
    }
}