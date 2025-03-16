package com.kene.movemateDemo.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kene.movemateDemo.R
import com.kene.movemateDemo.components.StatusIndicator
import com.kene.movemateDemo.models.Shipment
import com.kene.movemateDemo.models.ShipmentStatus
import com.kene.movemateDemo.ui.theme.BackgroundGrey
import com.kene.movemateDemo.ui.theme.BackgroundWhite
import com.kene.movemateDemo.ui.theme.DividerColor
import com.kene.movemateDemo.ui.theme.PrimaryPurple
import com.kene.movemateDemo.ui.theme.TextSecondary
import com.kene.movemateDemo.ui.theme.TextTertiary
import com.kene.movemateDemo.utils.DummyData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Create a singleton object to manage the selected tab state
object ShipmentHistoryState {
    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex
    
    fun updateSelectedTab(index: Int) {
        _selectedTabIndex.value = index
    }
}

@Composable
fun ShipmentHistoryScreen() {
    // Get the current selected tab
    val selectedTabIndex by ShipmentHistoryState.selectedTabIndex.collectAsState()
    
    // Filter shipments based on selected tab
    val filteredShipments = remember(selectedTabIndex) {
        when (selectedTabIndex) {
            0 -> DummyData.shipments // All
            1 -> DummyData.shipments.filter { it.status == ShipmentStatus.COMPLETED } // Completed
            2 -> DummyData.shipments.filter { it.status == ShipmentStatus.IN_PROGRESS } // In progress
            3 -> DummyData.shipments.filter { it.status == ShipmentStatus.PENDING || it.status == ShipmentStatus.LOADING } // Pending
            else -> DummyData.shipments
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Make the entire content scrollable
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Text(
                    text = "Shipments",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp, top = 24.dp)
                )
            }

            // Empty state
            if (filteredShipments.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No shipments found",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            } 
            // Shipment items
            else {
                items(filteredShipments) { shipment ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(durationMillis = 300)) +
                                slideInVertically(
                                    animationSpec = tween(durationMillis = 300),
                                    initialOffsetY = { it / 2 }
                                )
                    ) {
                        ShipmentHistoryItem(shipment = shipment)
                    }
                }
            }
            
            // Add some bottom padding
            item {
                Spacer(modifier = Modifier.height(80.dp)) // Increased height to account for gradient overlay
            }
        }
        
        // Gradient overlay at the bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White
                        ),
                        startY = 0f,
                        endY = 100f
                    )
                )
        )
    }
}

@Composable
fun ShipmentHistoryItem(shipment: Shipment) {

    val statusIcon = when(shipment.status) {
        ShipmentStatus.PENDING -> R.drawable.clockcounterclockwise
        ShipmentStatus.LOADING -> R.drawable.clockcountdown
        ShipmentStatus.COMPLETED -> R.drawable.check_circle
        else -> R.drawable.arrowsclockwise
    }
    
    return Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.5.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left column with text content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                // Status indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(200.dp))
                        .background(BackgroundGrey)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    // Status icon
                    Icon(
                        painter = painterResource(id = statusIcon),
                        contentDescription = shipment.status.label,
                        tint = shipment.status.color,
                        modifier = Modifier.size(16.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    // Status text
                    Text(
                        text = shipment.status.label.lowercase(),
                        style = MaterialTheme.typography.labelMedium,
                        color = shipment.status.color
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Shipment title
                Text(
                    text = shipment.name ?: "Arriving today!",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Shipment details
                val detailsText = if (shipment.to.isNotEmpty()) {
                    "Your delivery, #${shipment.trackingNumber}\nfrom ${shipment.from}, is arriving today!"
                } else {
                    "Your delivery, #${shipment.trackingNumber}\nfrom ${shipment.from}"
                }
                
                Text(
                    text = detailsText,
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary.copy(alpha = 0.8f)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Price and date
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    shipment.amount?.let {
                        Text(
                            text = "$${it.toInt()} USD",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryPurple
                            ),
                            maxLines = 1
                        )
                    }
                    Box(
                        Modifier
                            .clip(CircleShape)
                            .background(TextTertiary.copy(alpha = 0.5f))
                            .size(5.dp)
                    )
                    shipment.date?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1
                        )
                    }
                }
            }
            
            // Right side with package image
            Box(
                modifier = Modifier
                    .size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.box_package),
                    contentDescription = "Package",
                    modifier = Modifier.size(60.dp).alpha(0.8f)
                )
            }
        }
    }
} 