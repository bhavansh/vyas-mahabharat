package dev.bmg.vyasmahabharat.ui.reading

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bmg.vyasmahabharat.data.model.Verse
import dev.bmg.vyasmahabharat.ui.reading.components.TextToSpeechLanguage
import dev.bmg.vyasmahabharat.ui.reading.components.TextToSpeechManager
import dev.bmg.vyasmahabharat.ui.reading.components.VerseItem
import dev.bmg.vyasmahabharat.viewmodel.MahabharataViewModel

@Composable
fun ChapterReadingScreen(
    viewModel: MahabharataViewModel,
    textToSpeech: TextToSpeechManager
) {
    val selectedChapter by viewModel.selectedChapter.collectAsState()
    val selectedVerse = remember { mutableStateOf<Verse?>(null) }
    val languageSelection = remember { mutableStateOf(TextToSpeechLanguage.ENGLISH) }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Chapter title
        selectedChapter?.let { chapter ->
            Text(
                text = chapter.chapter_name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
            
            // TTS Language selection
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("TTS Language:", modifier = Modifier.padding(end = 8.dp))
                
                TextButton(
                    onClick = { languageSelection.value = TextToSpeechLanguage.ENGLISH },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (languageSelection.value == TextToSpeechLanguage.ENGLISH)
                            MaterialTheme.colorScheme.primary else Color.Gray
                    )
                ) {
                    Text("English")
                }
                
                TextButton(
                    onClick = { languageSelection.value = TextToSpeechLanguage.SANSKRIT },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (languageSelection.value == TextToSpeechLanguage.SANSKRIT)
                            MaterialTheme.colorScheme.primary else Color.Gray
                    )
                ) {
                    Text("Sanskrit")
                }
            }
            
            // Verses list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(chapter.verses) { verse ->
                    VerseItem(
                        verse = verse,
                        isSelected = verse == selectedVerse.value,
                        onVerseSelected = { selectedVerse.value = verse },
                        onTextToSpeech = {
                            val textToSpeak = when (languageSelection.value) {
                                TextToSpeechLanguage.ENGLISH -> verse.verse_translation.debroy_trans
                                TextToSpeechLanguage.SANSKRIT -> verse.verse_data.joinToString("\n")
                            }
                            textToSpeech.speak(textToSpeak, languageSelection.value)
                        }
                    )
                }
            }
        }
    }
}