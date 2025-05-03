package dev.bmg.vyasmahabharat.data.model

data class Verse(
    val verse_id: Int,
    val verse_number: String,
    val verse_data: List<String>,
    val verse_translation: VerseTranslation
)
