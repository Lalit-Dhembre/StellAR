package com.cosmic_struck.stellar.home.presentation

import com.cosmic_struck.stellar.data.remote.dto.ModelDTO
import com.cosmic_struck.stellar.data.stellar.remote.dto.ClassroomDTOItem
import com.cosmic_struck.stellar.data.stellar.remote.dto.JoinedClassroomDTO

data class HomeScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val options: List<Options> = listOf(Options.MODULES,Options.CLASSROOM),
    val selected : Options = Options.MODULES,
    val joinedClassrooms: List<JoinedClassroomDTO> = emptyList(),
    val codeText : String = "",
    val modalSheetState : Boolean = false,
    val userName : String = "",
    val userLevel: String = "",
    val classroomJoinStatus: ClassroomJoinStatus = ClassroomJoinStatus.NOT_JOINED,
)

enum class ClassroomJoinStatus(){
    JOINED,
    NOT_JOINED,
    ERROR
}

enum class Options(){
    MODULES,
    CLASSROOM
}
