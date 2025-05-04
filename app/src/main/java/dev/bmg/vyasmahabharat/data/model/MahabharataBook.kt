package dev.bmg.vyasmahabharat.data.model

import com.google.gson.annotations.SerializedName

data class MahabharataBook(
    @SerializedName("book_name")
    val bookName: String,
    @SerializedName("book_number")
    val bookNumber: Int,
    @SerializedName("num_chapters")
    val numChapters: Int,
    val chapters: List<Chapter>
)