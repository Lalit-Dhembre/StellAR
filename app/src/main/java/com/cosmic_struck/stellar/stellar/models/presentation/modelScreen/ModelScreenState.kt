package com.cosmic_struck.stellar.stellar.models.presentation.modelScreen

import com.cosmic_struck.stellar.stellar.models.domain.model.Planet

data class ModelScreenState(
    val currentIndex : Int = 0,
    val currentList: ListType = ListType.MY_COLLECTION,
    val planetsList: List<Planet> = emptyList(),
    val error: String = "",
    val isLoading : Boolean = false
)

enum class ListType{
    MY_COLLECTION,
    DISCOVER
}


