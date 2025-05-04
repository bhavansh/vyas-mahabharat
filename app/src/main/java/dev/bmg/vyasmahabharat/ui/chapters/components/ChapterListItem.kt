package dev.bmg.vyasmahabharat.ui.chapters.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bmg.vyasmahabharat.ui.theme.MahabharataTheme

@Composable
fun ChapterListItem(
    chapterName: String,
    onChapterSelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp) // Consistent padding
            .clickable { onChapterSelected() },
        shape = RoundedCornerShape(12.dp), // Consistent shape
        // elevation removed - rely on theme/surface colors
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) // Consistent color
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp) // Adjust padding as needed
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Ensures icon is at the end
        ) {
            Text(
                text = chapterName,
                style = MaterialTheme.typography.titleMedium, // Use titleMedium like BookListItem? Or bodyLarge is fine too.
                modifier = Modifier.weight(1f).padding(end = 8.dp) // Add padding between text and icon
            )
            Icon(
                imageVector = Icons.Default.ChevronRight, // Changed icon
                contentDescription = "Open chapter", // More specific description
                tint = MaterialTheme.colorScheme.onSurfaceVariant // Consistent tint
            )
        }
    }
}

// --- Preview for ChapterItem ---
@Preview(showBackground = true, widthDp = 360)
@Composable
fun ChapterItemPreview() {
    MahabharataTheme {
        Column(Modifier.padding(8.dp)) {
            ChapterListItem(chapterName = "Canto 001", onChapterSelected = {})
            ChapterListItem(chapterName = "Canto 002 - A Longer Chapter Title Example", onChapterSelected = {})
        }
    }
}