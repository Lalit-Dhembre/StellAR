package com.cosmic_struck.stellar.classroom.presentation.viewmodel

import com.cosmic_struck.stellar.data.remote.dto.ModelDTO
import com.cosmic_struck.stellar.data.stellar.remote.dto.ClassroomMemberDTO

data class ClassroomModelScreenState(
    val isLoading: Boolean = false,
    val error: String = "",
    val modelURL: String = "",
    val modelPath: String = "",
    val modelName: String = "",
    val modelDescription: String = ""
)

data class ClassroomHomeScreenState(
    val classroom_id : String = "",
    val classroomName: String = "",
    val classroomAuthor : String = "",
    val classroomMembers: String = "",
    val classroomMembersList: List<ClassroomMemberDTO> = emptyList(),
    val classroomModelsList: List<ModelDTO> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val options: List<Options> = listOf(Options.MEMBERS,Options.MODELS),
    val selected : Options = Options.MEMBERS
)

enum class Options{
    MEMBERS,
    MODELS
}