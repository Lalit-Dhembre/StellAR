package com.cosmic_struck.stellar.data.remote

import com.cosmic_struck.stellar.data.remote.dto.PlanetDTO
import com.cosmic_struck.stellar.data.stellar.remote.dto.ModelUrlDTO
import com.cosmic_struck.stellar.data.stellar.remote.dto.ScanDTO
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface StellARAPI {
    @GET("/api/models")
    suspend fun getModels(
        @Query("subject") subject: String = "Planet",
    ) : List<PlanetDTO>

    @GET("/api/modelurl")
    suspend fun getUrl(
        @Query("model_id") id : String
    ) : ModelUrlDTO

    @Multipart
    @POST("/api/scan")
    suspend fun getScanResults(
        @Part image: MultipartBody.Part
    ) : ScanDTO

}