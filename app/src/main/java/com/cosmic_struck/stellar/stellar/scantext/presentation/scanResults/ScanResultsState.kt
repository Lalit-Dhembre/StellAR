package com.cosmic_struck.stellar.stellar.scantext.presentation.scanResults

import com.cosmic_struck.stellar.stellar.models.domain.model.Planet

data class ScanResultsState(
    val detections: List<String> = emptyList(),
    val planets : List<Planet> = emptyList(),
    val isError :String =  "",
    val isLoading : Boolean = false
)
