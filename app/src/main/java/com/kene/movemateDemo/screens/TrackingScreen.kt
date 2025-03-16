package com.kene.movemateDemo.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kene.movemateDemo.R
import com.kene.movemateDemo.models.Shipment
import com.kene.movemateDemo.models.VehicleType
import com.kene.movemateDemo.ui.theme.DividerColor
import com.kene.movemateDemo.ui.theme.PrimaryOrange
import com.kene.movemateDemo.ui.theme.PrimaryPurple
import com.kene.movemateDemo.ui.theme.TextSecondary
import com.kene.movemateDemo.utils.DummyData
import kotlinx.coroutines.delay

@Composable
fun TrackingScreen(
    searchQuery: String = "",
    isSearchFocused: Boolean = false
) {
    // Filter shipments based on search query
    val filteredShipments = remember(searchQuery) {
        if (searchQuery.isEmpty()) {
            DummyData.searchHistory
        } else {
            DummyData.searchHistory.filter { shipment ->
                shipment.name?.contains(searchQuery, ignoreCase = true) == true ||
                shipment.trackingNumber.contains(searchQuery, ignoreCase = true) ||
                shipment.from.contains(searchQuery, ignoreCase = true) ||
                shipment.to.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    
    // Content based on search focus state
    if (isSearchFocused) {
        // Search results layout
        SearchResultsLayout(
            searchQuery = searchQuery,
            filteredShipments = filteredShipments
        )
    } else {
        // Regular tracking screen content
        MainContent()
    }
}

@Composable
fun SearchResultsLayout(
    searchQuery: String,
    filteredShipments: List<Shipment>
) {
    val slideInAnimation = remember {
            Animatable(initialValue = 100f)
        }
        
        LaunchedEffect(Unit) {
            slideInAnimation.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            )
        }

    Box(Modifier.padding(horizontal = 16.dp, vertical = 16.dp).animateContentSize()) {
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .offset(y = slideInAnimation.value.dp)
                .alpha(1f - (slideInAnimation.value / 100f).coerceIn(0f, 1f)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(Color.White)
                    .padding(horizontal = 16.dp)
                    .animateContentSize(),
            ) {
                if (filteredShipments.isEmpty()) {
                    // No results found
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No shipments found for \"$searchQuery\"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    // Search results
                    items(filteredShipments) { shipment ->
                        ShipmentSearchResultItem(shipment = shipment)
                        if (shipment != filteredShipments.last()) {
                            HorizontalDivider(
                                color = DividerColor,
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun ShipmentSearchResultItem(shipment: Shipment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle click */ }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Package icon
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(PrimaryPurple),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.cardboard_box),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Shipment details
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = shipment.name ?: "Package",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "#${shipment.trackingNumber} • ${shipment.from} → ${shipment.to}",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun MainContent() {
    val animatedOffset = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(key1 = true) {
        animatedOffset.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp)
    ) {
        // Tracking section
        Text(
            text = "Tracking",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 8.dp)
                .padding(horizontal = 16.dp)
                .offset(x = animatedOffset.value.dp)
        )
        
        // Shipment details card

        
        Box(
            modifier = Modifier.offset(x = animatedOffset.value.dp)
        ) {
            ShipmentDetailsCard(
                shipment = DummyData.shipments[0],
                onAddStopClick = { /* Handle add stop */ }
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Available vehicles section
        Text(
            text = "Available vehicles",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .padding(horizontal = 16.dp)
                .offset(x = animatedOffset.value.dp)
        )
        
        // Vehicle types
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            itemsIndexed(DummyData.vehicleTypes) { index, vehicleType ->
                val animatedOffset = remember { Animatable(initialValue = 100f) }
                
                LaunchedEffect(key1 = true) {
                    delay(50L * index.coerceAtMost(1))
                    animatedOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
                
                Box(
                    modifier = Modifier.offset(x = animatedOffset.value.dp)
                ) {
                    VehicleTypeCard(vehicleType = vehicleType)
                }
            }
        }
    }
}

@Composable
fun ShipmentDetailsCard(
    shipment: Shipment,
    onAddStopClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.5.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(top = 16.dp)) {
            // Shipment number
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Shipment Number",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = TextSecondary
                    )
                    
                    Text(
                        text = shipment.trackingNumber,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                
                // Forklift icon (placeholder)
                Image(
                    painter = painterResource(id = R.drawable.tractor), // Replace with actual forklift icon
                    contentDescription = "Forklift",
                    modifier = Modifier.size(40.dp)
                )
            }

            HorizontalDivider(
                color = DividerColor,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
            )
            
            // Sender info with time
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sender with icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(0.6f)
                ) {
                    // Sender icon (orange circle with package)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFBE9E7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp, // Replace with package icon
                            contentDescription = "Sender",
                            tint = PrimaryOrange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = "Sender",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Text(
                            text = "${shipment.from}, 5243",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                // Time info
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.weight(0.4f)
                ) {
                    Text(
                        text = "Time",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        // Green dot
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4CAF50))
                        )
                        
                        Spacer(modifier = Modifier.width(4.dp))
                        
                        Text(
                            text = shipment.estimatedDelivery ?: "2 day -3 days",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis

                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(28.dp))
            
            // Receiver info with status
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Receiver with icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(0.6f)
                ) {
                    // Receiver icon (green circle with package)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2F1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown, // Replace with package icon
                            contentDescription = "Receiver",
                            tint = Color(0xFF80CBC4),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = "Receiver",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal),
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Text(
                            text = "${shipment.to}, 6342",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                // Status info
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.weight(0.4f)
                ) {
                    Text(
                        text = "Status",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Text(
                        text = "Waiting to collect",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(
                color = DividerColor,
                thickness = 1.dp
            )

            
            // Add stop button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAddStopClick() }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = PrimaryOrange,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Add Stop",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryOrange
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun VehicleTypeCard(
    vehicleType: VehicleType
) {
    Card(
        modifier = Modifier
            .width(160.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.5.dp
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .padding(start = 16.dp),
            horizontalAlignment = Alignment.End,

        ) {

            // Vehicle name
            Column(Modifier.fillMaxWidth()) {
                Text(
                    text = vehicleType.name,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                    )
                )

                Text(
                    text = vehicleType.description,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = TextSecondary.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Vehicle image
            Image(
                painter = painterResource(
                    id = when (vehicleType.name) {
                        "Ocean freight" -> R.drawable.ship_two
                        "Cargo freight" -> R.drawable.truck
                        "Air freight" -> R.drawable.plane
                        else -> R.drawable.ship_two
                    }
                ),
                contentDescription = "${vehicleType.name} icon",
                modifier = Modifier
                    .height(160.dp)
                    .align(Alignment.End)
            )
            

        }
    }
} 