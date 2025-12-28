package com.cosmic_struck.stellar.auth.domain

import com.cosmic_struck.stellar.common.util.Resource
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val supabaseClient: SupabaseClient
) {
    operator fun invoke(username: String, email: String, password: String) : Flow<Resource<Boolean>> =
        flow {
            try {
                emit(Resource.Loading())
                val user = supabaseClient.auth.signUpWith(Email){
                    this.email = email
                    this.password = password
                }
                val userId = user?.id
                supabaseClient.postgrest.from("users").update({
                    set("user_name",username)
                }){
                    filter {
                        userId?.let { eq("id",it) }
                    }
                }
                emit(Resource.Success(true))
            }catch (e: Exception){
                emit(Resource.Error(e.localizedMessage ?: "Unknown Error"))
            }
        }
}