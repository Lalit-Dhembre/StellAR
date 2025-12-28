package com.cosmic_struck.stellar.classroom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.cosmic_struck.stellar.classroom.presentation.viewmodel.delegate.ClassroomHomeScreenDelegate
import com.cosmic_struck.stellar.classroom.presentation.viewmodel.delegate.ClassroomModelScreenDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ClassroomViewModel @Inject constructor(
    private val classroomModelScreenDelegate: ClassroomModelScreenDelegate,
    private val classroomHomeScreenDelegate: ClassroomHomeScreenDelegate
) : ViewModel(){

    val homeState : StateFlow<ClassroomHomeScreenState> = classroomHomeScreenDelegate.state
    val modelState : StateFlow<ClassroomModelScreenState> = classroomModelScreenDelegate.state



}