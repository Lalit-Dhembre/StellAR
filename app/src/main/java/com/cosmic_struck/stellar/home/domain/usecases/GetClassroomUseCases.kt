package com.cosmic_struck.stellar.home.domain.usecases

import android.util.Log
import com.cosmic_struck.stellar.common.util.Resource
import com.cosmic_struck.stellar.data.stellar.remote.dto.ClassroomDTOItem
import com.cosmic_struck.stellar.data.stellar.repository.homeRepo.HomeRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetClassroomUseCases @Inject constructor(
    private val homeRepo: HomeRepo
) {
    operator fun invoke(userId:String) : Flow<Resource<List<ClassroomDTOItem>>> = flow{
        try {
            emit(Resource.Loading())
            val items = homeRepo.getClassrooms(userId)
            emit(Resource.Success(items))
        }
        catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
            Log.d("Error",e.message.toString())
        }
    }
}