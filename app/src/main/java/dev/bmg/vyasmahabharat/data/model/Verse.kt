package dev.bmg.vyasmahabharat.data.model

import com.google.gson.annotations.SerializedName

data class Verse(
    @SerializedName("verse_id")
    val verseId: Int,
    @SerializedName("verse_number")
    val verseNumber: String,
    @SerializedName("verse_data")
    val verseData: List<String>,
    @SerializedName("verse_translation")
    val verseTranslation: VerseTranslation
)
