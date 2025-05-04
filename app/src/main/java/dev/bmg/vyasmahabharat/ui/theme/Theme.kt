package dev.bmg.vyasmahabharat.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Define our custom Light Color Scheme using the Vyas palette
private val VyasLightColorScheme = lightColorScheme(
    primary = VyasBrown,                 // Main interactive color
    onPrimary = VyasOnPrimary,             // Text/icon on primary
    primaryContainer = VyasAccentOrange,   // Used for prominent containers (like nav indicator)
    onPrimaryContainer = VyasOnPrimary,    // Text/icon on primary container
    secondary = VyasDarkBrown,           // Secondary actions/accents
    onSecondary = VyasOnPrimary,           // Text/icon on secondary
    tertiary = VyasAccentOrange,         // Tertiary accent (can be same as primaryContainer)
    onTertiary = VyasOnPrimary,          // Text/icon on tertiary
    background = VyasBeige,              // Overall screen background
    onBackground = VyasOnBackground,       // Text/icon on background
    surface = VyasLightBeige,          // Card backgrounds, bottom sheets
    onSurface = VyasOnSurface,           // Text/icon on surface
    surfaceVariant = VyasSurfaceVariant,   // Bottom nav background, subtle card backgrounds
    onSurfaceVariant = VyasOnSurfaceVariant, // Text/icon on surface variant
    outline = VyasSubtleText,            // Borders, dividers
    error = Color(0xFFB00020),             // Standard Material error color
    onError = Color.White
    // You can define others like inverseSurface, inverseOnSurface etc. if needed
)

// Optional: Define a custom Dark Color Scheme
private val VyasDarkColorScheme = darkColorScheme(
    primary = VyasDarkPrimary, // Orange in dark mode
    onPrimary = VyasDarkOnPrimary,
    primaryContainer = VyasBrown, // Use brown for containers in dark
    onPrimaryContainer = VyasOnPrimary,
    secondary = VyasAccentOrange, // Lighter orange for secondary
    onSecondary = VyasDarkOnPrimary,
    tertiary = VyasBrown,
    onTertiary = VyasOnPrimary,
    background = VyasDarkSurface,
    onBackground = VyasDarkOnSurface,
    surface = VyasDarkSurfaceVariant,
    onSurface = VyasDarkOnSurface,
    surfaceVariant = Color(0xFF4E342E), // Slightly different variant
    onSurfaceVariant = VyasDarkOnSurface,
    outline = VyasSubtleText,
    error = Color(0xFFCF6679), // Material standard dark error
    onError = Color(0xFF000000)
)


@Composable
fun MahabharataTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    // Set to false if you want to FORCE your custom theme always
    dynamicColor: Boolean = false, // Changed default to false to prioritize custom theme
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // If dynamic color is enabled AND available, use it
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Otherwise, use our custom dark or light scheme
        darkTheme -> VyasDarkColorScheme // Use custom dark scheme
        else -> VyasLightColorScheme      // Use custom light scheme
    }

    // This part handles system bars (status bar, navigation bar) styling
    val view = LocalView.current
    if (!view.isInEditMode) { // Don't run side effects in Preview
        SideEffect {
            val window = (view.context as Activity).window
            // Set status bar color and icon brightness
            window.statusBarColor = colorScheme.background.toArgb() // Match status bar to background
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme

            // Optional: Set navigation bar color (might clash with gesture nav)
            // window.navigationBarColor = colorScheme.surfaceVariant.toArgb()
            // WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme, // Apply the chosen color scheme
        typography = Typography,   // Apply our typography definitions
        // shapes = Shapes, // You can define custom shapes (rounded corners) in Shapes.kt too
        content = content
    )
}