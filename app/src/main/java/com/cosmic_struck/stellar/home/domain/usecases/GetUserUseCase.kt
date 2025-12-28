package com.cosmic_struck.stellar.home.domain.usecases

import android.util.Log
import com.cosmic_struck.stellar.common.util.Resource
import com.cosmic_struck.stellar.data.stellar.remote.dto.UserDTO
import com.cosmic_struck.stellar.data.stellar.repository.homeRepo.HomeRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: HomeRepo
) {
    operator fun invoke(userId: String): Flow<Resource<UserDTO>> = flow {
        try {
            emit(Resource.Loading())
            val user = repository.getUser(userId)
            emit(Resource.Success(user))
        }catch (e: Exception){
            Log.d("GetUserUseCase", e.message.toString())
            emit(Resource.Error(e.message.toString()))
        }
    }
}