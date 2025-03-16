package com.kene.movemateDemo.utils

import com.kene.movemateDemo.models.Shipment
import com.kene.movemateDemo.models.ShipmentCategory
import com.kene.movemateDemo.models.ShipmentStatus
import com.kene.movemateDemo.models.VehicleType

/**
 * Utility class to provide dummy data for the app
 */
object DummyData {
    
    // Dummy shipments for the shipment history screen
    val shipments = listOf(
        Shipment(
            id = "1",
            trackingNumber = "NEJ20089934122231",
            name = "Summer linen jacket",
            from = "Atlanta", 
            to = "Chicago",
            status = ShipmentStatus.IN_PROGRESS,
            amount = 1400.0,
            date = "Sep 20, 2023",
            estimatedDelivery = "2 day -3 days"
        ),
        Shipment(
            id = "2", 
            trackingNumber = "NEJ20089934122232",
            name = "Winter boots",
            from = "Boston",
            to = "New York",
            status = ShipmentStatus.PENDING,
            amount = 650.0,
            date = "Sep 21, 2023"
        ),
        Shipment(
            id = "3",
            trackingNumber = "NEJ20089934122233", 
            name = "Leather bag",
            from = "San Francisco",
            to = "Los Angeles",
            status = ShipmentStatus.COMPLETED,
            amount = 850.0,
            date = "Sep 18, 2023"
        ),
        Shipment(
            id = "4",
            trackingNumber = "NEJ20089934122234",
            name = "Fitness equipment",
            from = "Denver", 
            to = "Phoenix",
            status = ShipmentStatus.LOADING,
            amount = 1200.0,
            date = "Sep 22, 2023"
        ),
        Shipment(
            id = "5",
            trackingNumber = "NEJ20089934122235",
            name = "Winter coat",
            from = "Boston",
            to = "Seattle",
            status = ShipmentStatus.COMPLETED,
            amount = 890.0,
            date = "Sep 17, 2023",
            estimatedDelivery = "1 day -2 days"
        ),
        Shipment(
            id = "6",
            trackingNumber = "NEJ20089934122236", 
            name = "Gaming laptop",
            from = "Miami",
            to = "Las Vegas", 
            status = ShipmentStatus.IN_PROGRESS,
            amount = 2100.0,
            date = "Sep 23, 2023",
            estimatedDelivery = "3 days -4 days"
        ),
        Shipment(
            id = "7",
            trackingNumber = "NEJ20089934122237",
            name = "Camera equipment",
            from = "New York",
            to = "Los Angeles",
            status = ShipmentStatus.LOADING,
            amount = 3500.0,
            date = "Sep 24, 2023"
        ),
        Shipment(
            id = "8",
            trackingNumber = "NEJ20089934122238",
            name = "Office furniture",
            from = "Houston",
            to = "Phoenix",
            status = ShipmentStatus.PENDING,
            amount = 1800.0,
            date = "Sep 25, 2023",
            estimatedDelivery = "4 days -5 days"
        ),
        Shipment(
            id = "9",
            trackingNumber = "NEJ20089934122239",
            name = "Smartphone",
            from = "Seattle",
            to = "Portland",
            status = ShipmentStatus.COMPLETED,
            amount = 950.0,
            date = "Sep 15, 2023"
        ),
        Shipment(
            id = "10",
            trackingNumber = "NEJ20089934122240",
            name = "Tablet",
            from = "Chicago",
            to = "Detroit",
            status = ShipmentStatus.COMPLETED,
            amount = 750.0,
            date = "Sep 16, 2023"
        ),
        Shipment(
            id = "11",
            trackingNumber = "NEJ20089934122241",
            name = "Headphones",
            from = "Austin",
            to = "Dallas",
            status = ShipmentStatus.PENDING,
            amount = 350.0,
            date = "Sep 26, 2023"
        ),
        Shipment(
            id = "12",
            trackingNumber = "NEJ20089934122242",
            name = "Smart watch",
            from = "San Diego",
            to = "Las Vegas",
            status = ShipmentStatus.IN_PROGRESS,
            amount = 450.0,
            date = "Sep 24, 2023"
        )
    )
    
    // Dummy shipments for the search history screen
    val searchHistory = listOf(
        Shipment(
            id = "1",
            trackingNumber = "NE4385734085790",
            name = "Macbook pro M2",
            from = "Paris",
            to = "Morocco",
            status = ShipmentStatus.IN_PROGRESS
        ),
        Shipment(
            id = "2",
            trackingNumber = "NEJ20089934122231",
            name = "Summer linen jacket",
            from = "Barcelona",
            to = "Paris",
            status = ShipmentStatus.PENDING
        ),
        Shipment(
            id = "3",
            trackingNumber = "NEJ3587026497865",
            name = "Tapered-fit jeans AW",
            from = "Colombia",
            to = "Paris",
            status = ShipmentStatus.COMPLETED
        ),
        Shipment(
            id = "4",
            trackingNumber = "NEJ3587026497865",
            name = "Slim fit jeans AW",
            from = "Bogota",
            to = "Dhaka",
            status = ShipmentStatus.IN_PROGRESS
        ),
        Shipment(
            id = "5",
            trackingNumber = "NEJ2348157075496",
            name = "Office setup desk",
            from = "France",
            to = "German",
            status = ShipmentStatus.PENDING
        )
    )
    
    // Recent searches for the search functionality
    val recentSearches = listOf(
        "NE4385734085790",
        "NEJ20089934122231",
        "NEJ3587026497865",
        "NEJ2348157075496",
        "Macbook pro M2"
    )
    
    // Dummy vehicle types
    val vehicleTypes = listOf(
        VehicleType(
            name = "Cargo freight",
            description = "Reliable",
            iconResId = 0
        ),
        VehicleType(
            name = "Air freight",
            description = "International",
            iconResId = 0
        ),
        VehicleType(
            name = "Ocean freight",
            description = "International",
            iconResId = 0
        )
    )
    
    // Dummy shipment categories
    val shipmentCategories = listOf(
        ShipmentCategory("Documents"),
        ShipmentCategory("Glass"),
        ShipmentCategory("Liquid"),
        ShipmentCategory("Food"),
        ShipmentCategory("Electronic", true),
        ShipmentCategory("Product"),
        ShipmentCategory("Others")
    )
} 