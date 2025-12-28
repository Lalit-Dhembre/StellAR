package com.cosmic_struck.stellar.classroom.classroomHome.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosmic_struck.stellar.classroom.domain.GetClassroomDetailsUseCase
import com.cosmic_struck.stellar.classroom.domain.GetClassroomMemberUseCase
import com.cosmic_struck.stellar.classroom.domain.GetClassroomModelListUseCase
import com.cosmic_struck.stellar.common.util.Resource
import com.cosmic_struck.stellar.home.presentation.Options
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassHomeScreenViewModel @Inject constructor(

    private val savedStateHandle: SavedStateHandle
): ViewModel(){

    private val _state = mutableStateOf(ClassroomHomeScreenState())
    val state : State<ClassroomHomeScreenState> = _state

    init {
        val classroomId = savedStateHandle.get<String>("classroom_id")
        if(classroomId != null){
            _state.value = _state.value.copy(classroom_id = classroomId)
            getClassroomMembers()
            getClassroomModelList()
            getClassroomDetails()
        }
    }

    }