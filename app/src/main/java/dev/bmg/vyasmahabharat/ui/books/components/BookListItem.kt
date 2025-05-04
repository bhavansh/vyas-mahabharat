package dev.bmg.vyasmahabharat.ui.books.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun BookListItem(
    bookName: String,
    bookNumber: Int,
    onBookSelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onBookSelected() },
        shape = RoundedCornerShape(12.dp),
        // Use theme color directly - surfaceVariant often looks good for list items
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        // If you want the main surface color (like cards on home screen):
        // colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = bookName,
                    style = MaterialTheme.typography.titleMedium
                    // fontWeight removed - style defines it
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Book $bookNumber",
                    style = MaterialTheme.typography.labelMedium, // Using labelMedium for consistency
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Subtle color is good
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Go to book",
                tint = MaterialTheme.colorScheme.onSurfaceVariant // Subtle icon tint is good
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun BookListItemPreview() {
    MahabharataTheme {
        Column(Modifier.padding(8.dp)) {
            BookListItem(bookName = "Adiparvan", bookNumber = 1, onBookSelected = {})
            BookListItem(bookName = "Sabha Parvan", bookNumber = 2, onBookSelected = {})
        }
    }
}
