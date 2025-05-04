package dev.bmg.vyasmahabharat.ui.reading

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bmg.vyasmahabharat.data.model.Chapter
import dev.bmg.vyasmahabharat.data.model.Verse
import dev.bmg.vyasmahabharat.data.model.VerseTranslation
import dev.bmg.vyasmahabharat.ui.reading.components.TextToSpeechLanguage
import dev.bmg.vyasmahabharat.ui.reading.components.TextToSpeechManager
import dev.bmg.vyasmahabharat.ui.reading.components.VerseItem
import dev.bmg.vyasmahabharat.ui.theme.MahabharataTheme

@Composable
fun ChapterReadingScreen(
    chapter: Chapter?,
    textToSpeech: TextToSpeechManager
) {
    // Use rememberSaveable for state that should survive simple navigation/config changes
    val selectedVerseNumberState = rememberSaveable { mutableStateOf<String?>(null) } // Store number (String)
    val languageSelection = rememberSaveable { mutableStateOf(TextToSpeechLanguage.ENGLISH) }
    val searchQuery = rememberSaveable { mutableStateOf("") } // Use rememberSaveable
    val listState = rememberLazyListState() // Already uses rememberSaveable internally
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current // Get focus manager

    val filteredVerses = remember(chapter?.verses, searchQuery.value) {
        // ... (filtering logic remains the same) ...
        val query = searchQuery.value
        if (query.isBlank()) {
            chapter?.verses ?: emptyList()
        } else {
            chapter?.verses?.filter { verse ->
                verse.verseData.joinToString(" ").contains(query, ignoreCase = true) ||
                        verse.verseTranslation.debroyTrans.contains(query, ignoreCase = true) ||
                        verse.verseNumber.contains(query)
            } ?: emptyList()
        }
    }

    // Clear focus when tapping outside the TextField (e.g., on the main column background)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus() // Clear focus when tapping the background
                })
            }
    ) {

        // --- Search Bar ---
        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            label = { Text("Search Verses...") }, // Shorter label
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                if (searchQuery.value.isNotEmpty()) {
                    IconButton(onClick = {
                        searchQuery.value = ""
                        focusManager.clearFocus() // Also clear focus when clearing text
                    }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear Search")
                    }
                }
            },
            singleLine = true,
            // Use outlined text field colors for consistency with OutlinedTextField
            colors = TextFieldDefaults.colors()
        )

        // --- TTS Language Selection ---
        Row(
            // ... (TTS selection Row remains the same) ...
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                "TTS:",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(end = 8.dp)
            )
            TextButton( // English
                onClick = { languageSelection.value = TextToSpeechLanguage.ENGLISH },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (languageSelection.value == TextToSpeechLanguage.ENGLISH)
                        MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) { Text("English", style = MaterialTheme.typography.labelMedium) }

            TextButton( // Sanskrit
                onClick = { languageSelection.value = TextToSpeechLanguage.SANSKRIT },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (languageSelection.value == TextToSpeechLanguage.SANSKRIT)
                        MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) { Text("Sanskrit", style = MaterialTheme.typography.labelMedium) }
        }


        // --- Verses List ---
        when {
            chapter == null -> { // Loading state
                // ... (loading indicator remains the same) ...
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Text("Loading chapter...", modifier = Modifier.padding(top = 8.dp))
                }
            }
            // Check filtered list specifically for "no results" message
            filteredVerses.isEmpty() && chapter.verses.isNotEmpty() -> { // Adjusted condition slightly
                Text(
                    "No verses found matching your search.",
                    modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
                )
            }
            filteredVerses.isEmpty() && chapter.verses.isNullOrEmpty() -> {
                Text(
                    "This chapter has no verses.",
                    modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
                )
            }
            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(filteredVerses, key = { it.verseNumber }) { verse ->
                        VerseItem(
                            verse = verse,
                            // Check against saved verse NUMBER
                            isSelected = verse.verseNumber == selectedVerseNumberState.value,
                            onVerseSelected = {
                                // Save the verse NUMBER
                                selectedVerseNumberState.value = verse.verseNumber
                                focusManager.clearFocus() // Clear focus when selecting a verse
                            },
                            onTextToSpeech = {
                                // ... (TTS logic remains the same) ...
                                val textToSpeak = when (languageSelection.value) {
                                    TextToSpeechLanguage.ENGLISH -> verse.verseTranslation.debroyTrans
                                    TextToSpeechLanguage.SANSKRIT -> verse.verseData.joinToString(" ") // Join with space for TTS
                                }
                                textToSpeech.speak(textToSpeak, languageSelection.value)
                                focusManager.clearFocus() // Clear focus when TTS starts
                            },
                            onCopySanskrit = {
                                copyToClipboard(context, "Sanskrit V${verse.verseNumber}", verse.verseData.joinToString("\n"))
                                focusManager.clearFocus() // Clear focus after copy
                            },
                            onCopyEnglish = {
                                copyToClipboard(context, "English V${verse.verseNumber}", verse.verseTranslation.debroyTrans)
                                focusManager.clearFocus() // Clear focus after copy
                            }
                        )
                    }
                }
            }
        }
    }
}

// Helper function for clipboard
private fun copyToClipboard(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
    // Optional: Show feedback
    Toast.makeText(context, "$label copied", Toast.LENGTH_SHORT).show()
}


// --- Previews ---
@Preview(showBackground = true, name = "Reading Screen Loaded")
@Composable
fun ChapterReadingScreenPreviewLoaded() {
    val previewVerses = List(20) { index ->
        Verse(
            verseNumber = "%03d".format(index + 1),
            verseData = listOf("सञ्जय उवाच ।", "एवमुक्त्वा हृषीकेशं गुडाकेशः परन्तपः ।"),
            verseTranslation = VerseTranslation("Sanjaya said: Having spoken thus to Hrishikesha, Gudakesha Parantapa said..."),
            verseId = 1
        )
    }
    val previewChapter = Chapter(
        chapterId = 1,
        chapterName = "Arjuna Viṣhāda Yoga (Preview)",
        verses = previewVerses
    )
    val dummyTts = TextToSpeechManager(LocalContext.current) // Dummy for preview

    MahabharataTheme {
        ChapterReadingScreen(chapter = previewChapter, textToSpeech = dummyTts )
    }
}

@Preview(showBackground = true, name = "Reading Screen Loading")
@Composable
fun ChapterReadingScreenPreviewLoading() {
    val dummyTts = TextToSpeechManager(LocalContext.current)
    MahabharataTheme {
        ChapterReadingScreen(chapter = null, textToSpeech = dummyTts) // Pass null for loading
    }
}

@Preview(showBackground = true, name = "Reading Screen No Verses")
@Composable
fun ChapterReadingScreenPreviewNoVerses() {
    val previewChapter = Chapter(chapterId = 1, chapterName = "Empty Chapter (Preview)", verses = emptyList())
    val dummyTts = TextToSpeechManager(LocalContext.current)
    MahabharataTheme {
        ChapterReadingScreen(chapter = previewChapter, textToSpeech = dummyTts)
    }
}