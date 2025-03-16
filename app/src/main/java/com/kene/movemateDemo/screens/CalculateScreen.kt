package com.kene.movemateDemo.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kene.movemateDemo.R
import com.kene.movemateDemo.components.OrangeButton
import com.kene.movemateDemo.models.ShipmentCategory
import com.kene.movemateDemo.ui.theme.BackgroundGrey
import com.kene.movemateDemo.ui.theme.BackgroundWhite
import com.kene.movemateDemo.ui.theme.PrimaryOrange
import com.kene.movemateDemo.ui.theme.PrimaryPurple
import com.kene.movemateDemo.ui.theme.TextSecondary
import com.kene.movemateDemo.ui.theme.TextTertiary
import com.kene.movemateDemo.utils.DummyData
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CalculateScreen(
    onCalculateClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Destination section
        DestinationSection()
        
        // Packaging section
        PackagingSection()
        
        // Categories section
        CategoriesSection(
        )
        
        // Calculate button
        OrangeButton(
            text = "Calculate",
            onClick = onCalculateClick,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}

@Composable
fun DestinationSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Destination",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 12.dp, top = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = BackgroundWhite
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.5.dp
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Sender location input
                LocationInputField(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.navigationarrow),
                            contentDescription = "Sender location",
                            tint = TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    placeholder = "Sender location",
                )

                
                // Receiver location input
                LocationInputField(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.navigationarrow),
                            contentDescription = "Receiver location",
                            tint = TextSecondary,
                            modifier = Modifier
                                .size(22.dp)
                                .rotate(180f)
                        )
                    },
                    placeholder = "Receiver location",
                )

                // Approx weight input
                LocationInputField(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.calc),
                            contentDescription = "Calculator",
                            tint = TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    placeholder = "Approx weight",
                )
            }
        }
    }
}

@Composable
private fun LocationInputField(
    icon: @Composable () -> Unit,
    placeholder: String,
) {
    // Search field
    val inputText = remember { mutableStateOf("") }
    TextField(
        value = inputText.value,
        onValueChange = { inputText.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .animateContentSize(),
        placeholder = {
            Text(
                text = placeholder,
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = {
            Row (verticalAlignment = Alignment.CenterVertically) {
                icon()
                VerticalDivider(
                    thickness = 0.5.dp,
                    modifier = Modifier.height(28.dp).padding(start = 8.dp),
                    color = TextTertiary.copy(alpha = 0.8f)
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = BackgroundGrey,
            unfocusedContainerColor = BackgroundGrey,
            disabledContainerColor = BackgroundGrey,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = PrimaryPurple
        ),
        textStyle = MaterialTheme.typography.labelLarge,
        singleLine = true
    )
}

@Composable
fun PackagingSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Title
        Text(
            text = "Packaging",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 4.dp, top = 12.dp)
        )
        
        // Subtitle
        Text(
            text = "What are you sending?",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        // Packaging selection card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = BackgroundWhite
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.5.dp
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left side with icon and text
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Box icon
                    Image(
                        painter = painterResource(id = R.drawable.box_package),
                        contentDescription = "Package",
                        modifier = Modifier.size(28.dp).alpha(0.8f)
                    )

                    VerticalDivider(
                        thickness = 1.dp,
                        modifier = Modifier.height(28.dp).padding(horizontal = 12.dp),
                        color = TextTertiary.copy(alpha = 0.4f)
                    )
                    
                    // Text
                    Text(
                        text = "Box",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    )
                }
                
                // Dropdown arrow - using KeyboardArrowDown from Icons.Default
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown",
                    tint = TextSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun PackagingOption(
    title: String,
    description: String,
    isSelected: Boolean
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.8f,
        label = "scale"
    )
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = if (isSelected) PrimaryOrange else Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .background(if (isSelected) PrimaryOrange.copy(alpha = 0.05f) else Color.Transparent)
            .clickable { /* Handle selection */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(if (isSelected) PrimaryOrange else Color.LightGray.copy(alpha = 0.3f))
                .padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier
                        .size(14.dp)
                        .scale(scale)
                )
            }
        }
        
        Spacer(modifier = Modifier.size(12.dp))
        
        // Text content
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun CategoriesSection(
) {
    var selectedCategory by remember { mutableStateOf("") }
    
    // List of all category names with their relative sizes
    val categoryItems = listOf(
        CategoryItem("Documents", 1.2f),
        CategoryItem("Glass", 0.8f),
        CategoryItem("Liquid", 0.8f),
        CategoryItem("Food", 0.8f),
        CategoryItem("Electronic", 1.2f),
        CategoryItem("Product", 1.0f),
        CategoryItem("Others", 1.0f)
    )
    
    Column(modifier = Modifier.fillMaxWidth()) {
        // Title
        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 4.dp, top = 12.dp)
        )
        
        // Subtitle
        Text(
            text = "What are you sending?",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        // Categories in a flow layout
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 4
        ) {
            categoryItems.forEach { item ->
                CategoryButton(
                    text = item.name,
                    isSelected = selectedCategory == item.name,
                    onClick = { selectedCategory = item.name }
                )
            }
        }
    }
}

// Simple data class to hold category information
data class CategoryItem(val name: String, val weight: Float)

// Custom FlowRow implementation
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val rows = mutableListOf<List<Placeable>>()
        val itemConstraints = constraints.copy(minWidth = 0)
        
        // Measure and arrange items into rows
        val placeables = measurables.map { it.measure(itemConstraints) }
        var currentRow = mutableListOf<Placeable>()
        var currentRowWidth = 0
        var currentRowCount = 0
        
        placeables.forEach { placeable ->
            if (currentRowWidth + placeable.width > constraints.maxWidth || currentRowCount >= maxItemsInEachRow) {
                rows.add(currentRow)
                currentRow = mutableListOf()
                currentRowWidth = 0
                currentRowCount = 0
            }
            
            currentRow.add(placeable)
            currentRowWidth += placeable.width + if (currentRowWidth > 0) 8.dp.roundToPx() else 0
            currentRowCount++
        }
        
        if (currentRow.isNotEmpty()) {
            rows.add(currentRow)
        }
        
        // Calculate layout height
        val rowGapInPx = verticalArrangement.spacing.roundToPx()
        val rowHeights = rows.map { row -> row.maxOf { it.height } }
        val layoutHeight = rowHeights.sum() + (rows.size - 1) * rowGapInPx
        
        // Set layout size
        layout(constraints.maxWidth, layoutHeight) {
            var yPosition = 0
            
            rows.forEachIndexed { rowIndex, row ->
                val rowHeight = rowHeights[rowIndex]
                var xPosition = 0
                
                row.forEach { placeable ->
                    placeable.place(xPosition, yPosition)
                    xPosition += placeable.width + 8.dp.roundToPx()
                }
                
                yPosition += rowHeight + rowGapInPx
            }
        }
    }
}

@Composable
fun CategoryButton(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF0E1B2B) else BackgroundWhite,
        animationSpec = tween(durationMillis = 300),
        label = "backgroundColor"
    )
    
    val animatedTextColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color(0xFF0E1B2B),
        animationSpec = tween(durationMillis = 300),
        label = "textColor"
    )
    
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF0E1B2B) else Color(0xFF0E1B2B).copy(alpha = 0.5f),
        animationSpec = tween(durationMillis = 300),
        label = "borderColor"
    )
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = animatedBorderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .background(animatedBackgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(end = 4.dp)
                )
            }
            
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                ),
                maxLines = 1,
                color = animatedTextColor
            )
        }
    }
}

@Composable
fun ConfirmationScreen(
    amount: Double = 1460.0,
    onBackToHomeClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                    modifier = Modifier.fillMaxWidth(0.6f).aspectRatio(227/39f)
                )
                
                Spacer(modifier = Modifier.height(52.dp))
                
                // Package icon (simplified)
                Image(
                    painter = painterResource(id = R.drawable.box_package),
                    contentDescription = "Package",
                    modifier = Modifier.size(120.dp).alpha(0.8f)
                )
                
                Spacer(modifier = Modifier.height(36.dp))
                
                // Total amount
                Text(
                    text = "Total Estimated Amount",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "$${amount.toInt()} USD",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color(0xFF4CAF50),
                    )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "This amount is estimated this will vary\nif you change your location or weight",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Back to home button
                OrangeButton(
                    text = "Back to home",
                    onClick = onBackToHomeClick
                )
            }
        }
    }
} 