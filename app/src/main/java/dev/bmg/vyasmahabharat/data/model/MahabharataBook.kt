package dev.bmg.vyasmahabharat.data.model

data class MahabharataBook(
    val book_name: String,
    val book_number: Int,
    val num_chapters: Int,
    val chapters: List<Chapter>
)