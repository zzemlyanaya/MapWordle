package edu.skku.cs.pa1.model

data class WordleItem(
    val chars: List<CharUiModel>
)

data class CharUiModel(
    val char: String,
    val state: CharState
)