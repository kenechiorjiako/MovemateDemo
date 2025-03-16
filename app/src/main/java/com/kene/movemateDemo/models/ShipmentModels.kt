package com.kene.movemateDemo.models

import androidx.compose.ui.graphics.Color
import com.kene.movemateDemo.ui.theme.PrimaryOrange
import com.kene.movemateDemo.ui.theme.StatusBlue
import com.kene.movemateDemo.ui.theme.StatusGreen

// Enum for shipment status
enum class ShipmentStatus(val label: String, val color: Color) {
    IN_PROGRESS("in-progress", StatusGreen),
    PENDING("pending", PrimaryOrange),
    LOADING("loading", StatusBlue),
    COMPLETED("completed", StatusGreen)
}

// Data class for shipment details
data class Shipment(
    val id: String,
    val trackingNumber: String,
    val name: String? = null,
    val from: String,
    val to: String,
    val status: ShipmentStatus,
    val amount: Double? = null,
    val date: String? = null,
    val estimatedDelivery: String? = null
)

// Data class for vehicle types
data class VehicleType(
    val name: String,
    val description: String,
    val iconResId: Int
)