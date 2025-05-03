package dev.bmg.vyasmahabharat.data.model

data class Chapter(
    val chapter_id: Int,
    val chapter_name: String,
    val verses: List<Verse>
)