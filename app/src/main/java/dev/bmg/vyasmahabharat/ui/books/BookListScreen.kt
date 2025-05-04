package dev.bmg.vyasmahabharat.ui.books

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bmg.vyasmahabharat.data.model.Book
import dev.bmg.vyasmahabharat.ui.books.components.BookListItem
import dev.bmg.vyasmahabharat.ui.theme.MahabharataTheme

// --- Book List Screen (Container for BookListItems) ---
@Composable
fun BookListScreen(
    books: List<Book>, // Accept the list of books directly
    onBookSelected: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        itemsIndexed(books) {_,  book ->
            BookListItem(
                bookName = book.bookName,
                bookNumber = book.bookNumber,
                onBookSelected = { onBookSelected(book.bookNumber) }
            )
        }
    }
}

// --- Preview for BookListScreen ---
@Preview(showBackground = true, widthDp = 360)
@Composable
fun BookListScreenPreview() {
    // Create some dummy data for the preview
    val previewBooks = List(5) { index ->
        Book(
            bookNumber = index + 1,
            bookName = when (index) {
                0 -> "Adiparvan"
                1 -> "Sabhaparvan"
                2 -> "Aranyakaparvan"
                3 -> "Virataparvan"
                4 -> "Udyogaparvan"
                else -> "Book ${index + 1}"
            },
            numChapters = 100,
            chapters = emptyList() // Chapters not needed for this preview
        )
    }
    MahabharataTheme {
        BookListScreen(books = previewBooks, onBookSelected = {})
    }
}
