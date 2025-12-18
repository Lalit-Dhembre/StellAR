package com.cosmic_struck.stellar.data.stellar.remote.dto

data class ScanDTO(
    val success : Boolean,
    val detections: List<String>,
    val count : Int
)
