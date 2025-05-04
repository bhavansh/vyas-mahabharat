package dev.bmg.vyasmahabharat.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bmg.vyasmahabharat.ui.books.components.BookListItem
import dev.bmg.vyasmahabharat.ui.theme.MahabharataTheme

// --- Book List Screen (Container for BookListItems) ---
@Composable
fun SettingsScreen() {
    // Assuming MahabharataBook has bookName: String and bookNumber: Int
    val exampleBooks = List(18) { index -> // Generate dummy data for preview
        object {
            val bookName = when (index) {
                0 -> "Adiparvan"
                1 -> "Sabhaparvan"
                2 -> "Aranyakaparvan"
                3 -> "Virataparvan"
                4 -> "Udyogaparvan"
                else -> "Book ${index + 1}"
            }
            val bookNumber = index + 1
        }
    }


    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        // Use actual books list in real implementation
        itemsIndexed(exampleBooks /* replace with books */) { _, book ->
            BookListItem(
                bookName = book.bookName,
                bookNumber = book.bookNumber,
                // In real use: onBookSelected = { onBookSelected(book.bookNumber) }
                onBookSelected = { }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun BookListScreenPreview() {
    MahabharataTheme {
        Box(modifier = Modifier.background(Color(0xFFFFFBEF))) {
            // Pass empty list or dummy data for preview
            SettingsScreen()
        }
    }
}
