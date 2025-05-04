package dev.bmg.vyasmahabharat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bmg.vyasmahabharat.data.model.Book
import dev.bmg.vyasmahabharat.data.model.Chapter
import dev.bmg.vyasmahabharat.data.repository.MahabharataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MahabharataViewModel(private val repository: MahabharataRepository) : ViewModel() {
    
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books.asStateFlow()
    
    private val _selectedBook = MutableStateFlow<Book?>(null)
    val selectedBook: StateFlow<Book?> = _selectedBook.asStateFlow()
    
    private val _selectedChapter = MutableStateFlow<Chapter?>(null)
    val selectedChapter: StateFlow<Chapter?> = _selectedChapter.asStateFlow()

    // Optional: State for UI elements managed centrally (e.g., Top Bar Title)
    private val _screenTitle = MutableStateFlow("Vyas Mahabharata")
    val screenTitle: StateFlow<String> = _screenTitle.asStateFlow()

    private val _showBackButton = MutableStateFlow(false)
    val showBackButton: StateFlow<Boolean> = _showBackButton.asStateFlow()

    init {
        loadBooks()
    }
    
    private fun loadBooks() {
        viewModelScope.launch {
            _books.value = repository.getAllBooks()
        }
    }
    
    fun selectBook(bookNumber: Int) {
        val book = repository.getBookByNumber(bookNumber)
        _selectedBook.value = book
        _screenTitle.value = book?.bookName ?: "Chapters" // Update title
        _showBackButton.value = true // Show back button when viewing chapters
        _selectedChapter.value = null
    }
    
    fun selectChapter(chapterId: Int) {
        // Assumes _selectedBook is already set correctly by selectBook
        val chapter = _selectedBook.value?.chapters?.find { it.chapterId == chapterId }
        _selectedChapter.value = chapter
        // Update title to Canto name - requires Chapter model to have a name/title
        _screenTitle.value = chapter?.chapterName ?: "Reading" // Assuming Chapter has cantoName
        _showBackButton.value = true // Keep back button visible
    }

    // Called when navigating back to the main Book List within the nested graph
    fun deselectBook() {
        // _selectedBook.value = null // Optional: Clear selected book state if needed
        _screenTitle.value = "Books" // Reset title for the book list
        _showBackButton.value = false // Hide back button for the main book list
        _selectedChapter.value = null
    }

    // Called when navigating back to a main bottom nav destination
    fun setMainScreen(route: String) {
        _screenTitle.value = when (route) {
            "home" -> "Vyas Mahabharat"
            "books" -> "Books" // Initial title for books section
            "bookmarks" -> "My Bookmarks"
            "settings" -> "Settings"
            else -> "Vyas Mahabharat"
        }
        _showBackButton.value = route != "home" && route != "bookmarks" && route != "settings" && (_selectedBook.value != null || _selectedChapter.value != null)
    }

}