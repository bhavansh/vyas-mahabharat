package dev.bmg.vyasmahabharat.data.model

import com.google.gson.annotations.SerializedName

data class Chapter(
    @SerializedName("chapter_id")
    val chapterId: Int,
    @SerializedName("chapter_name")
    val chapterName: String,
    val verses: List<Verse>
)