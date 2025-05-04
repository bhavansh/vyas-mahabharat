package dev.bmg.vyasmahabharat.ui.navbar

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bmg.vyasmahabharat.ui.theme.MahabharataTheme

// --- Top App Bar ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    canNavigateBack: Boolean,
    onNavigateUp: () -> Unit = {},
    onRefresh: (() -> Unit)? = null, // Optional refresh action
    onSearch: (() -> Unit)? = null  // Optional search action
) {
    TopAppBar(
        title = { Text(text = title, fontWeight = FontWeight.Medium) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateUp) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            } else {
                // Optional: Show app icon or hamburger menu if needed
                Icon(
                    Icons.Default.Book, // App icon placeholder
                    contentDescription = "App Icon",
                    modifier = Modifier.padding(start = 16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        actions = {
            if (onRefresh != null) {
                IconButton(onClick = onRefresh) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                }
            }
            if (onSearch != null) {
                IconButton(onClick = onSearch) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface, // Match background
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Preview(showBackground = true, )
@Composable
fun AppTopBarPreviewHome() {
    MahabharataTheme {
        AppTopBar(title = "Vyas Katha", canNavigateBack = false)
    }
}

@Preview(showBackground = true)
@Composable
fun AppTopBarPreviewBack() {
    MahabharataTheme {
        AppTopBar(title = "Adiparvan", canNavigateBack = true, onRefresh = {}, onSearch = {})
    }
}
