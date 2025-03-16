package com.kene.movemateDemo.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kene.movemateDemo.AppScreen
import com.kene.movemateDemo.R
import com.kene.movemateDemo.models.ShipmentStatus
import com.kene.movemateDemo.screens.ShipmentHistoryState
import com.kene.movemateDemo.ui.theme.BackgroundWhite
import com.kene.movemateDemo.ui.theme.PrimaryOrange
import com.kene.movemateDemo.ui.theme.PrimaryPurple
import com.kene.movemateDemo.ui.theme.TextSecondary
import com.kene.movemateDemo.utils.DummyData

/**
 * Custom header component with purple background
 */
@Composable
fun MoveMateHeader(
    currentScreen: AppScreen,
    statusBarHeight: Dp = Dp(0f),
    onBackClick: () -> Unit = {},
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    isSearchFocused: Boolean = false,
    onSearchFocusChange: (Boolean) -> Unit = {}
) {
    var isSearchFocusedInternal by remember { mutableStateOf(isSearchFocused) }

    if (isSearchFocusedInternal != isSearchFocused) {
        onSearchFocusChange(isSearchFocusedInternal)
    }

    val statusBarColor by animateColorAsState(
        targetValue = if (currentScreen == AppScreen.Confirmation) BackgroundWhite else PrimaryPurple,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "headerColorAnimation"
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryPurple)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(statusBarHeight)
                .background(statusBarColor)
        )

        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith
                        fadeOut(animationSpec = tween(300))
            }, label = "header_content"
        ) { currentScreen -> 
            when (currentScreen) {
                AppScreen.Tracking -> {
                    HomeHeader(
                        isSearchFocused = isSearchFocusedInternal,
                        onBackClick = { isSearchFocusedInternal = false },
                        onFocusChanged = { isSearchFocusedInternal = it },
                        query = searchQuery,
                        onQueryChanged = onSearchQueryChange
                    )
                }
                AppScreen.ShipmentHistory -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        DefaultHeader(onBackClick = onBackClick, title = "Shipment history")
                        ShipmentHistoryHeaderTabs()
                    }
                }
                AppScreen.Calculate -> {
                    DefaultHeader(onBackClick = onBackClick, title = "Calculate")
                }
                else -> {}
            }
        }
            
    }
}

@Composable
fun HomeHeader(
    isSearchFocused: Boolean,
    onBackClick: () -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    query: String,
    onQueryChanged: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(
            visible = !isSearchFocused,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile picture and location
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        // Profile image
                        Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "Profile",
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Location information
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.navigationarrow),
                                contentDescription = "Location",
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier
                                    .size(16.dp)
                                    .rotate(90f)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = "Your location",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = "Wertheimer, Illinois",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )

                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Change location",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Notification bell
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.Black
                    )
                }
            }
        }

        SearchBar(
            hint = "Enter the receipt number...",
            onSearch = {},
            onScanClick = {},
            onBackClick = onBackClick,
            isSearchFocused = isSearchFocused,
            onFocusChanged = onFocusChanged,
            query = query,
            onQueryChange = onQueryChanged
        )
    }
}

@Composable
fun DefaultHeader(onBackClick: () -> Unit, title: String) {
    return Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Color.White
        )

        Spacer(modifier = Modifier.weight(1f))

        Spacer(modifier = Modifier.width(48.dp))
    }
}

/**
 * Search bar component
 */
@Composable
fun SearchBar(
    hint: String = "Enter the receipt number...",
    onSearch: (String) -> Unit = {},
    onScanClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    isSearchFocused: Boolean = false,
    onFocusChanged: (Boolean) -> Unit = {},
    query: String = "",
    onQueryChange: (String) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button - only visible when search is focused
        AnimatedVisibility(
            visible = isSearchFocused,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {
            IconButton(
                onClick = { 
                    // Clear focus when back button is clicked
                    onBackClick()
                    focusManager.clearFocus()
                    onQueryChange("")
                },
                modifier = Modifier.animateContentSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        
        // Search field
        TextField(
            value = query,
            onValueChange = { 
                onQueryChange(it)
                onSearch(it) 
            },
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = if (isSearchFocused) 0.dp else 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
                .clip(RoundedCornerShape(50.dp))
                .focusRequester(focusRequester)
                .onFocusChanged { state ->
                    if (state.isFocused) {
                        onFocusChanged(state.isFocused)
                    }
                }
                .animateContentSize(),
            placeholder = {
                Text(
                    text = hint,
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelMedium
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.magnifyingglass),
                    contentDescription = "Search", 
                    tint = PrimaryPurple,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(20.dp)
                )
            },
            trailingIcon = {
                Box(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(PrimaryOrange)
                        .clickable { onScanClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.scan),
                        contentDescription = "Scan",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = BackgroundWhite,
                unfocusedContainerColor = BackgroundWhite,
                disabledContainerColor = BackgroundWhite,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = PrimaryPurple
            ),
            textStyle = MaterialTheme.typography.labelLarge,
            singleLine = true
        )
    }
}


/**
 * Orange button component
 */
@Composable
fun OrangeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val sizeScale by animateFloatAsState(if (isPressed) 0.95f else 1f)
    
    return Button(
        onClick = {
            onClick()
        },
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .graphicsLayer(
                scaleX = sizeScale,
                scaleY = sizeScale
            ),
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryOrange
        ),
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
    }
}

/**
 * Animated bottom navigation bar
 */
@Composable
fun AnimatedBottomNavigation(
    visible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }) + expandVertically() + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + shrinkVertically() + fadeOut()
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp
        ) {
            content()
        }
    }
}


/**
 * Tabs for shipment history header
 */
@Composable
fun ShipmentHistoryHeaderTabs() {
    val selectedTabIndex by ShipmentHistoryState.selectedTabIndex.collectAsState()

    val allCount = DummyData.shipments.size
    val completedCount = DummyData.shipments.count { it.status == ShipmentStatus.COMPLETED }
    val inProgressCount = DummyData.shipments.count { it.status == ShipmentStatus.IN_PROGRESS }
    val pendingCount = DummyData.shipments.count { it.status == ShipmentStatus.PENDING }
    
    val tabs = listOf(
        "All" to allCount,
        "Completed" to completedCount,
        "In progress" to inProgressCount,
        "Pending" to pendingCount
    )
    

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.Transparent,
        edgePadding = 16.dp,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                color = PrimaryOrange
            )
        },
        divider = {}
    ) {
        tabs.forEachIndexed { index, (title, count) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clickable { ShipmentHistoryState.updateSelectedTab(index) }
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 12.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                    ),
                    color = Color.White.copy(alpha = if (selectedTabIndex == index) 1f else 0.7f)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(200.dp))
                        .width(28.dp)
                        .background(
                            if (selectedTabIndex == index) PrimaryOrange else Color.White.copy(alpha = 0.2f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 3.dp),
                        text = count.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (selectedTabIndex == index) Color.White else Color.White.copy(alpha = 0.4f)
                    )
                }
            }
        }
    }
} 