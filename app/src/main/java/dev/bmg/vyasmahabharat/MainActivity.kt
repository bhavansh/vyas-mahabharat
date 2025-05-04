package dev.bmg.vyasmahabharat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.bmg.vyasmahabharat.data.model.BottomNavItem
import dev.bmg.vyasmahabharat.data.repository.MahabharataRepository
import dev.bmg.vyasmahabharat.ui.bookmarks.BookmarksScreen
import dev.bmg.vyasmahabharat.ui.books.BookListScreen
import dev.bmg.vyasmahabharat.ui.chapters.ChapterListScreen
import dev.bmg.vyasmahabharat.ui.home.HomeScreen
import dev.bmg.vyasmahabharat.ui.navbar.AppBottomNavigationBar
import dev.bmg.vyasmahabharat.ui.navbar.AppTopBar
import dev.bmg.vyasmahabharat.ui.reading.ChapterReadingScreen
import dev.bmg.vyasmahabharat.ui.reading.components.TextToSpeechManager
import dev.bmg.vyasmahabharat.ui.settings.SettingsScreen
import dev.bmg.vyasmahabharat.ui.theme.MahabharataTheme
import dev.bmg.vyasmahabharat.viewmodel.MahabharataViewModel


class MainActivity : ComponentActivity() {
    private val repository by lazy { MahabharataRepository(applicationContext) }
    private val viewModel by viewModels<MahabharataViewModel>(
        factoryProducer = { ViewModelFactory(repository) }
    )
    private lateinit var textToSpeechManager: TextToSpeechManager

    // Define Bottom Navigation Items
    private val bottomNavItems = listOf(
        BottomNavItem("Home", Icons.Default.Home, "home"),
        BottomNavItem("Books", Icons.Default.Book, "books"),
        BottomNavItem("Bookmarks", Icons.Default.Bookmark, "bookmarks"),
        BottomNavItem("Settings", Icons.Default.Settings, "settings")
    )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textToSpeechManager = TextToSpeechManager(this)

        setContent {
            MahabharataTheme {
                val mainNavController = rememberNavController()
                // Observe title and back button state from ViewModel
                val screenTitle = viewModel.screenTitle.collectAsState("")
                val showBackButton = viewModel.showBackButton.collectAsState(false)

                Scaffold(
                    topBar = {
                        // Use AppTopBar, controlling back navigation based on ViewModel state
                        AppTopBar(
                            title = screenTitle.value,
                            canNavigateBack = showBackButton.value,
                            // Navigate up using the MAIN controller for now.
                            // We might need to conditionally use a nested controller later if needed.
                            onNavigateUp = { mainNavController.navigateUp() }
                            // Add refresh/search actions if needed
                        )
                    },
                    bottomBar = {
                        AppBottomNavigationBar(
                            items = bottomNavItems,
                            currentRoute = currentRoute(mainNavController),
                            onItemClick = { item ->
                                mainNavController.navigate(item.route) {
                                    // Pop up to the start destination of the graph to avoid building up a large stack
                                    popUpTo(mainNavController.graph.findStartDestination().id) {
                                        saveState = true // Save state of the screen being left
                                    }
                                    // Avoid multiple copies of the same destination when reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when navigating to a previously visited destination
                                    restoreState = true
                                }
                                // Update VM state when main tab changes
                                viewModel.setMainScreen(item.route)
                            }
                        )
                    }
                ) { innerPadding ->
                    // Main Navigation Host
                    NavHost(
                        navController = mainNavController,
                        startDestination = "home", // Start at home
                        modifier = Modifier.padding(innerPadding) // Apply padding from Scaffold
                    ) {
                        composable("home") {
                            // Ensure VM state is correct when navigating here
                            LaunchedEffect(Unit) { viewModel.setMainScreen("home") }
                            HomeScreen(Modifier.padding())
                        }

                        // Nested Navigation for Books Section
                        navigation(startDestination = "bookList", route = "books") {
                            composable("bookList") {
                                // Set title/back button when entering book list
                                LaunchedEffect(Unit) { viewModel.deselectBook() }

                                val booksState = viewModel.books.collectAsState() // Collect the state directly
                                BookListScreen(
                                    booksState.value,
                                    onBookSelected = { bookNumber ->
                                        // ViewModel handles title/back button update in selectBook
                                        // viewModel.selectBook(bookNumber) // Moved to ChapterListScreen's LaunchedEffect
                                        mainNavController.navigate("chapterList/$bookNumber")
                                    }
                                )
                            }

                            composable(
                                route = "chapterList/{bookNumber}",
                                arguments = listOf(navArgument("bookNumber") {
                                    type = NavType.IntType
                                })
                            ) { backStackEntry ->
                                val bookNumber = backStackEntry.arguments?.getInt("bookNumber")
                                if (bookNumber != null) {
                                    // Load book/chapter data when this screen becomes visible
                                    LaunchedEffect(bookNumber) {
                                        viewModel.selectBook(bookNumber)
                                    }
                                    val selectedBookState = viewModel.selectedBook.collectAsState()
                                    val currentBook = selectedBookState.value // Get the Book object or null

                                    ChapterListScreen(
                                        chapters = currentBook?.chapters,
                                        onChapterSelected = { chapterId ->
                                            // Navigate to the reading screen
                                            mainNavController.navigate("reading/$chapterId")
                                        }
                                    )
                                } else {
                                    // Handle error: bookNumber not found
                                    Text("Error: Book number not found.")
                                    // Optionally navigate back or show error message
                                }
                            }

                            composable(
                                route = "reading/{chapterId}",
                                arguments = listOf(navArgument("chapterId") {
                                    type = NavType.IntType
                                })
                            ) { backStackEntry ->
                                val chapterId = backStackEntry.arguments?.getInt("chapterId")
                                if (chapterId != null) {
                                    // Load chapter details when this screen is visible
                                    LaunchedEffect(chapterId) {
                                        viewModel.selectChapter(chapterId)
                                    }

                                    val selectedChapterState = viewModel.selectedChapter.collectAsState()
                                    val currentChapter = selectedChapterState.value // Get the Chapter object or null

                                    ChapterReadingScreen(
                                        chapter = currentChapter, // Pass the whole VM
                                        textToSpeech = textToSpeechManager
                                        // No explicit onNavigateBack needed if using topBar's back
                                    )
                                } else {
                                    // Handle error: chapterId not found
                                    Text("Error: Chapter ID not found.")
                                    // Optionally navigate back
                                }
                            }
                        } // End of "books" nested navigation

                        composable("bookmarks") {
                            LaunchedEffect(Unit) { viewModel.setMainScreen("bookmarks") }
                            BookmarksScreen()
                        }

                        composable("settings") {
                            LaunchedEffect(Unit) { viewModel.setMainScreen("settings") }
                            SettingsScreen()
                        }
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

// Helper function to get the current route from the NavController
@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    // Check hierarchy for nested routes
    return navBackStackEntry.value?.destination?.route
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

// --- Preview (Optional - Shows Scaffold Structure) ---
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    // Dummy NavController and items for preview
    val previewNavController = rememberNavController()
    val previewItems = listOf(
        BottomNavItem("Home", Icons.Default.Home, "home"),
        BottomNavItem("Books", Icons.Default.Book, "books"),
        BottomNavItem("Bookmarks", Icons.Default.Bookmark, "bookmarks"),
        BottomNavItem("Settings", Icons.Default.Settings, "settings")
    )
    MahabharataTheme {
        Scaffold(
            topBar = { AppTopBar(title = "Vyas Mahabharata", canNavigateBack = false) },
            bottomBar = {
                AppBottomNavigationBar(
                    items = previewItems,
                    currentRoute = "home",
                    onItemClick = {})
            }
        ) { padding ->
            // Simple placeholder for preview content area
            HomeScreen(modifier = Modifier.padding(padding))
        }
    }
}


