package com.cosmic_struck.stellar.home.domain.usecases

import android.util.Log
import com.cosmic_struck.stellar.common.util.Resource
import com.cosmic_struck.stellar.data.stellar.remote.dto.JoinedClassroomDTO
import com.cosmic_struck.stellar.data.stellar.repository.homeRepo.HomeRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetJoinedClassroomsUseCases @Inject constructor(
    private val homeRepo: HomeRepo
) {
    operator fun invoke(userId: String): Flow<Resource<List<JoinedClassroomDTO>>> = flow{
        try {
            emit(Resource.Loading())
            val items = homeRepo.getJoinedClassrooms(userId)
            Log.d("JoinedClassrooms",items.toString())
            emit(Resource.Success(items))
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
            Log.d("Error",e.message.toString())
        }
    }
}