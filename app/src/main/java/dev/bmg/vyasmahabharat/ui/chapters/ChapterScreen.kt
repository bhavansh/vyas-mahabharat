package dev.bmg.vyasmahabharat.ui.chapters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bmg.vyasmahabharat.ui.chapters.components.ChapterItem
import dev.bmg.vyasmahabharat.viewmodel.MahabharataViewModel

@Composable
fun ChapterScreen(viewModel: MahabharataViewModel, onChapterSelected: (Int) -> Unit) {
    val selectedBook by viewModel.selectedBook.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize()) {
        selectedBook?.let { book ->
            Text(
                text = book.bookName,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
            
            LazyColumn(contentPadding = PaddingValues(8.dp)) {
                items(book.chapters) { chapter ->
                    ChapterItem(
                        chapterName = chapter.chapterName,
                        chapterId = chapter.chapterId,
                        onChapterSelected = { onChapterSelected(chapter.chapterId) }
                    )
                }
            }
        }
    }
}