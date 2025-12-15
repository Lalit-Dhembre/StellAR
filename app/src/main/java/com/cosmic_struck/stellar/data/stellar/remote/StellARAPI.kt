package com.cosmic_struck.stellar.data.remote

import com.cosmic_struck.stellar.data.remote.dto.PlanetListDTO
import retrofit2.http.GET

interface StellARAPI {
    @GET("/api/models")
    suspend fun getModels() : PlanetListDTO

    @GET("/api/modelurl")
    suspend fun getUrl() : String

}