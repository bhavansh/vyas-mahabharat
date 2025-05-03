package dev.bmg.vyasmahabharat.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import dev.bmg.vyasmahabharat.data.model.Chapter
import dev.bmg.vyasmahabharat.data.model.MahabharataBook

class MahabharataRepository(private val context: Context) {
    private var booksList: List<MahabharataBook> = emptyList()
    
    init {
        loadBooksFromAssets()
    }
    
    private fun loadBooksFromAssets() {
        try {
            val jsonFileNames = context.assets.list("") ?: emptyArray()
            val booksList = mutableListOf<MahabharataBook>()
            
            jsonFileNames.filter { it.endsWith(".json") }.forEach { fileName ->
                val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
                val book = Gson().fromJson(jsonString, MahabharataBook::class.java)
                booksList.add(book)
            }
            
            this.booksList = booksList.sortedBy { it.book_number }
        } catch (e: Exception) {
            Log.e("MahabharataRepository", "Error loading books: ${e.message}")
        }
    }
    
    fun getAllBooks(): List<MahabharataBook> = booksList
    
    fun getBookByNumber(bookNumber: Int): MahabharataBook? {
        return booksList.find { it.book_number == bookNumber }
    }
    
    fun getChaptersByBookNumber(bookNumber: Int): List<Chapter>? {
        return getBookByNumber(bookNumber)?.chapters
    }
    
    fun getChapter(bookNumber: Int, chapterId: Int): Chapter? {
        return getBookByNumber(bookNumber)?.chapters?.find { it.chapter_id == chapterId }
    }
}