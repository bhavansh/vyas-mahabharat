package dev.bmg.vyasmahabharat.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


// --- Custom Vyas Katha Palette (Light Theme) ---
// Define colors inspired by your screenshots
val VyasBeige = Color(0xFFFFFBEF)         // Light background
val VyasLightBeige = Color(0xFFFFFDF7)      // Card/Surface backgrounds (slightly lighter/different)
val VyasBrown = Color(0xFF8D6E63)         // Primary interactive elements, icons
val VyasDarkBrown = Color(0xFF5D4037)       // Text on light backgrounds, maybe secondary elements
val VyasAccentOrange = Color(0xFFFFA726)   // Accent color (e.g., for highlighted nav item indicator)
val VyasSubtleText = Color(0xFF795548)     // For less important text (like "Book 1")
val VyasSurfaceVariant = Color(0xFFF5F0E1) // For slightly differentiated surfaces like bottom nav bg

// --- Define Colors for Text/Icons ON TOP OF the palette colors ---
// Ensure good contrast! You might need to adjust these based on testing.
val VyasOnPrimary = Color.White             // Text/icon on top of VyasBrown/VyasAccentOrange
val VyasOnBackground = VyasDarkBrown        // Text/icon on top of VyasBeige
val VyasOnSurface = VyasDarkBrown           // Text/icon on top of VyasLightBeige
val VyasOnSurfaceVariant = VyasDarkBrown    // Text/icon on top of VyasSurfaceVariant

// --- Optional: Define Dark Theme Palette (Example using browns/oranges) ---
val VyasDarkSurface = Color(0xFF3E2723)     // Dark background
val VyasDarkSurfaceVariant = Color(0xFF4E342E) // Darker card background
val VyasDarkPrimary = VyasAccentOrange     // Use the orange as primary in dark mode
val VyasDarkOnSurface = Color(0xFFEDE0D4)    // Light text on dark background
val VyasDarkOnPrimary = Color(0xFF4E342E)    // Dark text on orange primary button