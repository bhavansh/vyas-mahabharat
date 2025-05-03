package dev.bmg.vyasmahabharat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.bmg.vyasmahabharat.data.repository.MahabharataRepository
import dev.bmg.vyasmahabharat.ui.books.BookScreen
import dev.bmg.vyasmahabharat.ui.chapters.ChapterScreen
import dev.bmg.vyasmahabharat.ui.reading.ChapterReadingScreen
import dev.bmg.vyasmahabharat.ui.reading.components.TextToSpeechManager
import dev.bmg.vyasmahabharat.ui.theme.MahabharataTheme
import dev.bmg.vyasmahabharat.viewmodel.MahabharataViewModel


class MainActivity : ComponentActivity() {
    private val repository by lazy { MahabharataRepository(applicationContext) }
    private val viewModel by viewModels<MahabharataViewModel>(
        factoryProducer = { ViewModelFactory(repository) }
    )
    private lateinit var textToSpeechManager: TextToSpeechManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textToSpeechManager = TextToSpeechManager(this)

        setContent {
            MahabharataTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "books"
                ) {
                    composable("books") {
                        BookScreen(
                            viewModel = viewModel,
                            onBookSelected = { bookNumber ->
                                viewModel.selectBook(bookNumber)
                                navController.navigate("chapters")
                            }
                        )
                    }

                    composable("chapters") {
                        ChapterScreen(
                            viewModel = viewModel,
                            onChapterSelected = { chapterId ->
                                viewModel.selectChapter(chapterId)
                                navController.navigate("reading")
                            }
                        )
                    }

                    composable("reading") {
                        ChapterReadingScreen(
                            viewModel = viewModel,
                            textToSpeech = textToSpeechManager
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeechManager.shutdown()
    }
}

class ViewModelFactory(private val repository: MahabharataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MahabharataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MahabharataViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
