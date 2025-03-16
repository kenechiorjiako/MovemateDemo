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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.kene.movemateDemo.NavigationItem.*
import com.kene.movemateDemo.components.AnimatedBottomNavigation
import com.kene.movemateDemo.components.MoveMateHeader
import com.kene.movemateDemo.components.SearchBar
import com.kene.movemateDemo.screens.CalculateScreen
import com.kene.movemateDemo.screens.ConfirmationScreen
import com.kene.movemateDemo.screens.ShipmentHistoryScreen
import com.kene.movemateDemo.screens.TrackingScreen
import com.kene.movemateDemo.ui.theme.BackgroundGrey
import com.kene.movemateDemo.ui.theme.BackgroundWhite
import com.kene.movemateDemo.ui.theme.MovemateDemoTheme
import com.kene.movemateDemo.ui.theme.NavigatinoGray
import com.kene.movemateDemo.ui.theme.PrimaryPurple
import com.kene.movemateDemo.ui.theme.TextSecondary

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
    val route: String,
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
) {
    object Home : NavigationItem("home", "Home", R.drawable.home, R.drawable.home)
    object Calculate : NavigationItem("calculate", "Calculate", R.drawable.calc,  R.drawable.calc)
    object Shipment : NavigationItem("shipment", "Shipment",  R.drawable.clockcounterclockwise,  R.drawable.clockcounterclockwise)
    object Profile : NavigationItem("profile", "Profile",  R.drawable.user,  R.drawable.user)
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

    // State for current screen
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Tracking) }
    
    // State for selected navigation item
    var selectedNavItem by remember { mutableStateOf<NavigationItem>(Home) }
    
    // State for header properties
    var headerTitle by remember { mutableStateOf("Tracking") }
    var showBackButton by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(true) }
    
    // Search state
    var searchQuery by remember { mutableStateOf("") }
    var isSearchFocused by remember { mutableStateOf(false) }
    
    // Determine if bottom navigation should be visible
    val showBottomNav = currentScreen is AppScreen.Tracking && !isSearchFocused
    
    // Set status bar appearance based on current screen
    val isConfirmationScreen = currentScreen is AppScreen.Confirmation
    
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
    
    // Function to handle back navigation
    val handleBackClick: () -> Unit = {
        if (isSearchFocused) {
            // If search is focused, just clear search and unfocus
            isSearchFocused = false
            searchQuery = ""
        } else {
            // Otherwise, navigate back to home
            selectedNavItem = Home
            currentScreen = AppScreen.Tracking
            headerTitle = "Tracking"
            showBackButton = false
            showSearchBar = true
        }
    }
    
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(androidx.compose.foundation.layout.WindowInsetsSides.Horizontal)),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            AnimatedBottomNavigation(visible = showBottomNav) {
                BottomNavigation(
                    selectedItem = selectedNavItem,
                    onItemSelected = { navItem ->
                        selectedNavItem = navItem
                        currentScreen = when (navItem) {
                            Home -> {
                                headerTitle = "Tracking"
                                showBackButton = false
                                showSearchBar = true
                                AppScreen.Tracking
                            }
                            Calculate -> {
                                headerTitle = "Calculate"
                                showBackButton = true
                                showSearchBar = false
                                AppScreen.Calculate
                            }
                            Shipment -> {
                                headerTitle = "Shipment history"
                                showBackButton = true
                                showSearchBar = false
                                AppScreen.ShipmentHistory
                            }
                            else -> {
                                headerTitle = "Profile"
                                showBackButton = true
                                showSearchBar = false
                                AppScreen.Tracking // Profile not implemented
                            }
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
//                .windowInsetsPadding(WindowInsets.safeDrawing.only(androidx.compose.foundation.layout.WindowInsetsSides.Top))
        ) {
            // Persistent header
            MoveMateHeader(
                title = headerTitle,
                currentScreen = currentScreen,
                statusBarHeight = statusBarHeight,
                onBackClick = handleBackClick,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                isSearchFocused = isSearchFocused,
                onSearchFocusChange = { isSearchFocused = it }
            )
            
            // Screen content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundGrey)
            ) {
                // Crossfade animation between screens
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
                                    headerTitle = "Confirmation"
                                    showBackButton = true
                                    showSearchBar = false
                                }
                            )
                        }
                        is AppScreen.Confirmation -> {
                            ConfirmationScreen(
                                onBackToHomeClick = {
                                    selectedNavItem = Home
                                    currentScreen = AppScreen.Tracking
                                    headerTitle = "Tracking"
                                    showBackButton = false
                                    showSearchBar = true
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