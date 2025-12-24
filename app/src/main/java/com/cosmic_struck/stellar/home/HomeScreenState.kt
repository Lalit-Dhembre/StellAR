package com.cosmic_struck.stellar.home

data class HomeScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val options: List<Options> = listOf(Options.MODULES,Options.CLASSROOM),
    val selected : Options = Options.MODULES
)

enum class Options(){
    MODULES,
    CLASSROOM
}
