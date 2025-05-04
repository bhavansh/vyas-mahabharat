package dev.bmg.vyasmahabharat.ui.chapters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bmg.vyasmahabharat.data.model.Chapter
import dev.bmg.vyasmahabharat.ui.chapters.components.ChapterListItem
import dev.bmg.vyasmahabharat.ui.theme.MahabharataTheme


@Composable
fun ChapterListScreen(
    // Accept the list of chapters directly
    chapters: List<Chapter>?, // Make it nullable to represent loading state
    onChapterSelected: (Int) -> Unit
) {

    Column(modifier = Modifier.fillMaxSize()) {
        when {
            // If chapters is null, assume it's loading (caller handles fetching)
            chapters == null -> {
                // Show a loading indicator centered
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Text("Loading chapters...", modifier = Modifier.padding(top = 8.dp))
                }
            }
            // If the list is not null but empty
            chapters.isEmpty() -> {
                Text(
                    "No chapters found for this book.",
                    modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
                )
            }
            // If we have chapters
            else -> {
                LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
                    itemsIndexed(chapters) { _, chapter ->
                        ChapterListItem( // Call ChapterListItem
                            // Use chapter.chapterName or similar field from your Chapter model
                            chapterName = chapter.chapterName, // MAKE SURE Chapter model HAS chapterName
                            // chapterId = chapter.chapterId, // ChapterListItem doesn't need ID
                            onChapterSelected = { onChapterSelected(chapter.chapterId) }
                        )
                    }
                }
            }
        }
    }
}

// --- Preview for ChapterScreen ---
@Preview(showBackground = true, widthDp = 360)
@Composable
fun ChapterScreenPreviewLoaded() {
    val previewChapters = List(10) { index ->
        // Assuming Chapter model structure
        Chapter(chapterId = index + 1, chapterName = "Canto ${"%03d".format(index + 1)}", verses = emptyList())
    }

    MahabharataTheme {
        // Pass the preview list directly
        ChapterListScreen(chapters = previewChapters, onChapterSelected = {})
    }
}

@Preview(showBackground = true, widthDp = 360, name = "ChapterScreen Empty")
@Composable
fun ChapterScreenPreviewEmpty() {
    MahabharataTheme {
        ChapterListScreen(chapters = emptyList(), onChapterSelected = {})
    }
}

@Preview(showBackground = true, widthDp = 360, name = "ChapterScreen Loading")
@Composable
fun ChapterScreenPreviewLoading() {
    MahabharataTheme {
        // Pass null to simulate loading state
        ChapterListScreen(chapters = null, onChapterSelected = {})
    }
}