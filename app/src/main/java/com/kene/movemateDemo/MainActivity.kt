package com.kene.movemateDemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.kene.movemateDemo.NavigationItem.*
import com.kene.movemateDemo.components.AnimatedBottomNavigation
import com.kene.movemateDemo.components.MoveMateHeader
import com.kene.movemateDemo.screens.CalculateScreen
import com.kene.movemateDemo.screens.ConfirmationScreen
import com.kene.movemateDemo.screens.ShipmentHistoryScreen
import com.kene.movemateDemo.screens.TrackingScreen
import com.kene.movemateDemo.ui.theme.BackgroundGrey
import com.kene.movemateDemo.ui.theme.MovemateDemoTheme
import com.kene.movemateDemo.ui.theme.NavigatinoGray
import com.kene.movemateDemo.ui.theme.PrimaryPurple
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.clickable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovemateDemoTheme {
                MoveMateApp()
            }
        }
    }
}

// Define navigation items
sealed class NavigationItem(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
) {
    object Home : NavigationItem( "Home", R.drawable.home, R.drawable.home)
    object Calculate : NavigationItem( "Calculate", R.drawable.calc,  R.drawable.calc)
    object Shipment : NavigationItem( "Shipment",  R.drawable.clockcounterclockwise,  R.drawable.clockcounterclockwise)
    object Profile : NavigationItem( "Profile",  R.drawable.user,  R.drawable.user)
}

// Define app screens
sealed class AppScreen {
    object Tracking : AppScreen()
    object ShipmentHistory : AppScreen()
    object Calculate : AppScreen()
    object Confirmation : AppScreen()
}


@Composable
fun MoveMateApp() {
    val density = LocalDensity.current
    val statusBarHeight = with(density) { WindowInsets.statusBars.getTop(density).toDp() }
    val view = LocalView.current
    val focusManager = LocalFocusManager.current

    // State for current screen
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Tracking) }
    
    // State for selected navigation item
    var selectedNavItem by remember { mutableStateOf<NavigationItem>(Home) }
    
    // Search state
    var searchQuery by remember { mutableStateOf("") }
    var isSearchFocused by remember { mutableStateOf(false) }
    
    // Determine if bottom navigation should be visible
    val showBottomNav = currentScreen is AppScreen.Tracking && !isSearchFocused
    
    // Set status bar appearance based on current screen
    val isConfirmationScreen = currentScreen is AppScreen.Confirmation
    
    // Function to handle back navigation
    val handleBackClick: () -> Unit = {
        if (isSearchFocused) {
            isSearchFocused = false
            searchQuery = ""
        } else {
            selectedNavItem = Home
            currentScreen = AppScreen.Tracking
        }
    }

    LaunchedEffect(isConfirmationScreen) {
        val window = (view.context as android.app.Activity).window
        window.statusBarColor = Color.Transparent.toArgb()
        val insetsController = WindowCompat.getInsetsController(window, view)

        if (isConfirmationScreen) {
            // White status bar with dark content for Confirmation screen
            insetsController.isAppearanceLightStatusBars = true
        } else {
            // Default purple status bar with light content for other screens
            insetsController.isAppearanceLightStatusBars = false
        }
    }
    
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(androidx.compose.foundation.layout.WindowInsetsSides.Horizontal))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            AnimatedBottomNavigation(visible = showBottomNav) {
                BottomNavigation(
                    selectedItem = selectedNavItem,
                    onItemSelected = { navItem ->
                        selectedNavItem = navItem
                        currentScreen = when (navItem) {
                            Home -> AppScreen.Tracking
                            Calculate -> AppScreen.Calculate
                            Shipment -> AppScreen.ShipmentHistory
                            else -> AppScreen.Tracking
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (showBottomNav) innerPadding.calculateBottomPadding() else 0.dp)
        ) {

            // Persistent header
            MoveMateHeader(
                currentScreen = currentScreen,
                statusBarHeight = statusBarHeight,
                onBackClick = handleBackClick,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                isSearchFocused = isSearchFocused,
                onSearchFocusChange = { isSearchFocused = it }
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundGrey)
            ) {
                Crossfade(
                    targetState = currentScreen,
                    animationSpec = tween(300),
                    label = "screen_transition"
                ) { screen ->
                    when (screen) {
                        is AppScreen.Tracking -> {
                            TrackingScreen(
                                searchQuery = searchQuery,
                                isSearchFocused = isSearchFocused
                            )
                        }
                        is AppScreen.ShipmentHistory -> {
                            ShipmentHistoryScreen()
                        }
                        is AppScreen.Calculate -> {
                            CalculateScreen(
                                onCalculateClick = {
                                    currentScreen = AppScreen.Confirmation
                                }
                            )
                        }
                        is AppScreen.Confirmation -> {
                            ConfirmationScreen(
                                onBackToHomeClick = {
                                    selectedNavItem = Home
                                    currentScreen = AppScreen.Tracking
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigation(
    selectedItem: NavigationItem,
    onItemSelected: (NavigationItem) -> Unit
) {
    val items = listOf(
        Home,
        Calculate,
        Shipment,
        Profile
    )
    
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        containerColor = Color.Transparent
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item,
                onClick = { onItemSelected(item) },
                icon = {
                    Icon(
                        painter = if (selectedItem == item) painterResource(id = item.selectedIcon) else painterResource(
                            id = item.unselectedIcon
                        ),
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryPurple,
                    selectedTextColor = PrimaryPurple,
                    unselectedIconColor = NavigatinoGray,
                    unselectedTextColor = NavigatinoGray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}