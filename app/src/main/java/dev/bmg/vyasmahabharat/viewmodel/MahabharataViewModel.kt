package dev.bmg.vyasmahabharat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bmg.vyasmahabharat.data.model.Chapter
import dev.bmg.vyasmahabharat.data.model.MahabharataBook
import dev.bmg.vyasmahabharat.data.repository.MahabharataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MahabharataViewModel(private val repository: MahabharataRepository) : ViewModel() {
    
    private val _books = MutableStateFlow<List<MahabharataBook>>(emptyList())
    val books: StateFlow<List<MahabharataBook>> = _books.asStateFlow()
    
    private val _selectedBook = MutableStateFlow<MahabharataBook?>(null)
    val selectedBook: StateFlow<MahabharataBook?> = _selectedBook.asStateFlow()
    
    private val _selectedChapter = MutableStateFlow<Chapter?>(null)
    val selectedChapter: StateFlow<Chapter?> = _selectedChapter.asStateFlow()
    
    init {
        loadBooks()
    }
    
    private fun loadBooks() {
        viewModelScope.launch {
            _books.value = repository.getAllBooks()
        }
    }
    
    fun selectBook(bookNumber: Int) {
        _selectedBook.value = repository.getBookByNumber(bookNumber)
    }
    
    fun selectChapter(chapterId: Int) {
        _selectedChapter.value = _selectedBook.value?.chapters?.find { it.chapter_id == chapterId }
    }
}