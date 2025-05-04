package dev.bmg.vyasmahabharat.ui.reading.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bmg.vyasmahabharat.data.model.Verse
import dev.bmg.vyasmahabharat.data.model.VerseTranslation
import dev.bmg.vyasmahabharat.ui.theme.MahabharataTheme

@Composable
fun VerseItem(
    verse: Verse,
    isSelected: Boolean,
    onVerseSelected: () -> Unit,
    onTextToSpeech: () -> Unit,
    onCopySanskrit: () -> Unit, // New lambda for Sanskrit copy
    onCopyEnglish: () -> Unit  // New lambda for English copy
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onVerseSelected() },
        elevation = CardDefaults.cardElevation(if (isSelected) 4.dp else 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.tertiaryContainer // Use a different highlight color maybe?
            else
                MaterialTheme.colorScheme.surfaceVariant // Use surfaceVariant for consistency
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // --- Verse Number Row ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Verse ${verse.verseNumber}",
                    style = MaterialTheme.typography.titleMedium,
                    // fontWeight removed - style defines it
                    modifier = Modifier.weight(1f) // Allow text to take space
                )
                // TTS Button moved next to Verse number for better grouping
                IconButton(onClick = onTextToSpeech, modifier = Modifier.size(32.dp)) { // Smaller IconButton
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Read aloud",
                        tint = MaterialTheme.colorScheme.primary // Make TTS icon stand out
                    )
                }
            }


            Spacer(modifier = Modifier.height(12.dp)) // Increased spacing

            // --- Sanskrit Text Row ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top // Align icon to top
            ) {
                Text(
                    text = verse.verseData.joinToString("\n"),
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = FontFamily.Serif, // Keep Serif for Sanskrit if desired
                    modifier = Modifier.weight(1f) // Take available space
                )
                IconButton(onClick = onCopySanskrit, modifier = Modifier.size(32.dp)) { // Copy Button
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Sanskrit",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant // Subtle tint
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp)) // Increased spacing

            // --- English Translation Row ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top // Align icon to top
            ) {
                Text(
                    text = verse.verseTranslation.debroyTrans,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f) // Take available space
                )
                IconButton(onClick = onCopyEnglish, modifier = Modifier.size(32.dp)) { // Copy Button
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy English Translation",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant // Subtle tint
                    )
                }
            }

            // TTS Button removed from here
        }
    }
}

// --- Preview for VerseItem ---
@Preview(showBackground = true)
@Composable
fun VerseItemPreview() {
    // Dummy verse data for preview
    val previewVerse = Verse(
        verseNumber = "001",
        verseData = listOf(
            "नारायणं नमस्कृत्य नरं चैव नरोत्तमम् ।",
            "देवीं सरस्वतीं चैव ततो जयमुदीरयेत् ॥"
        ),
        verseTranslation = VerseTranslation(debroyTrans = "\"Jaya\" must be recited after having bowed in obeisance before Nārāyaṇa and also Nara, the supreme human being, and also the goddess Sarasvatī."),
        verseId = 1
    )
    MahabharataTheme {
        Column(Modifier.padding(16.dp)) {
            VerseItem(
                verse = previewVerse,
                isSelected = false,
                onVerseSelected = {},
                onTextToSpeech = {},
                onCopySanskrit = {},
                onCopyEnglish = {}
            )
            VerseItem(
                verse = previewVerse.copy(verseNumber = "002"), // Example of another verse
                isSelected = true, // Example selected state
                onVerseSelected = {},
                onTextToSpeech = {},
                onCopySanskrit = {},
                onCopyEnglish = {}
            )
        }
    }
}