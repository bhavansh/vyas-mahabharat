package dev.bmg.vyasmahabharat.ui.books

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import dev.bmg.vyasmahabharat.ui.books.components.BookCard
import dev.bmg.vyasmahabharat.viewmodel.MahabharataViewModel

@Composable
fun BookScreen(viewModel: MahabharataViewModel, onBookSelected: (Int) -> Unit) {
    val books by viewModel.books.collectAsState()
    
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(books) { book ->
            BookCard(
                bookName = book.bookName,
                bookNumber = book.bookNumber,
                onBookSelected = { onBookSelected(book.bookNumber) }
            )
        }
    }
}