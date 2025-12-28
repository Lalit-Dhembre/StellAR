package com.cosmic_struck.stellar.home.data.supabase

import android.util.Log
import com.cosmic_struck.stellar.data.stellar.remote.dto.JoinedClassroomDTO
import com.cosmic_struck.stellar.home.data.dto.InsertMemberDTO
import com.cosmic_struck.stellar.home.data.dto.JoinClassroomDTO
import com.cosmic_struck.stellar.home.data.dto.UserDTO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import javax.inject.Inject

class HomeSupabaseService @Inject constructor(
    private val supabaseClient: SupabaseClient
) {

    suspend fun getJoinedClassrooms(userId: String) : List<JoinClassroomDTO>? {

        try {
            val joinedClassroomsList = supabaseClient.postgrest.from(
                "classroom_members"
            ).select(
                columns = Columns.ALL
            ){
                filter {
                    eq("user_id",userId)
                }
            }.decodeList<JoinClassroomDTO>()
            Log.d("Joined Classroom",joinedClassroomsList.toString())
            return joinedClassroomsList

        }catch (e: Exception){
            Log.d("Error",e.message.toString())
            return null
        }

    }

    suspend fun getUser(userId: String) : UserDTO? {

        try {
            val user = supabaseClient.postgrest.from(
                "users"
            ).select(
                columns = Columns.list(
                    "user_name",
                    "level",
                    "total_xp"
                )
            ){
                filter {
                    eq("id",userId)
                }
            }.decodeSingle<UserDTO>()
            Log.d("User",user.toString())
            return user
        }catch (e: Exception){
            Log.d("Error",e.message.toString())
            return null
        }
    }

    suspend fun joinClassroom(userId: String, joinCode: String) : Boolean{
        try {
            val result1 = supabaseClient.postgrest.from(
                "classroom"
            ).select{
                count(Count.EXACT)
                filter {
                    eq("join_code",joinCode)
                }
            }.decodeSingle<Int>()
            Log.d("Result 1",result1.toString())
            if(result1 == 0){
                return false
            }
            val classroomId = supabaseClient.postgrest.from(
                "classroom"
            ).select(
                columns = Columns.list(
                    "id"
                )
            ){
                filter {
                    eq("join_code",joinCode)
                }
            }.decodeSingle<String>()
            Log.d("Classroom Id",classroomId)
            val result2 = supabaseClient.postgrest.from(
                "classroom_members"
            ).insert(
                InsertMemberDTO(
                    classroom_id = classroomId,
                    user_id = userId
                )
            )
            Log.d("Result 2",result2.toString())
            return true

        }catch (e : Exception){
            Log.d("Error",e.message.toString())
            return false
        }

    }

}