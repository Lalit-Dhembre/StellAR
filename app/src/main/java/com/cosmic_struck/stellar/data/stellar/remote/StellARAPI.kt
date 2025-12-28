package com.cosmic_struck.stellar.data.stellar.remote

import com.cosmic_struck.stellar.data.remote.dto.ModelDTO
import com.cosmic_struck.stellar.data.stellar.remote.dto.ClassroomDTOItem
import com.cosmic_struck.stellar.data.stellar.remote.dto.ClassroomMemberDTO
import com.cosmic_struck.stellar.data.stellar.remote.dto.JoinClassroomResponseDTO
import com.cosmic_struck.stellar.data.stellar.remote.dto.JoinedClassroomDTO
import com.cosmic_struck.stellar.data.stellar.remote.dto.ModelUrlDTO
import com.cosmic_struck.stellar.data.stellar.remote.dto.ScanDTO
import com.cosmic_struck.stellar.data.stellar.remote.dto.UserDTO
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface StellARAPI {

    @GET("/api/classroommodels/{classroom_id}")
    suspend fun getClassroomModels(
        @Path ("classroom_id") classroom_id: String
    ) : List<ModelDTO>

    @GET("/api/models")
    suspend fun getModels(
        @Query("subject") subject: String = "Planet",
    ) : List<ModelDTO>

    @GET("/api/modelurl")
    suspend fun getUrl(
        @Query("model_id") id : String
    ) : ModelUrlDTO

    @Multipart
    @POST("/api/scan")
    suspend fun getScanResults(
        @Part image: MultipartBody.Part
    ) : ScanDTO


    @POST("/api/join_classroom")
    suspend fun joinClassroom(
        @Query("join_code") join_code: String,
        @Query("user_id") user_id: String
    ): JoinClassroomResponseDTO

    @GET("/api/classroom/{classroom_id}")
    suspend fun getClassroom(
        @Path("classroom_id") classroom_id : String
    ) :  ClassroomDTOItem

    @GET("/api/classroomjoined/{user_id}")
    suspend fun getJoinedClassrooms(
        @Path("user_id") user_id: String
    ): List<JoinedClassroomDTO>

    @GET("/api/users/{user_id}")
    suspend fun getUser(
        @Path("user_id") user_id: String
    ) : UserDTO

    @GET("/api/classroom_members/{classroom_id}")
    suspend fun getClassroomMembers(
        @Path("classroom_id") classroom_id: String
    ): List<ClassroomMemberDTO>
}