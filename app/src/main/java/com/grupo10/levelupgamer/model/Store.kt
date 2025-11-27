package com.grupo10.levelupgamer.model

data class Store(
    val id: Int,
    val name: String,
    val address: String,
    val city: String,
    val latitude: Double,
    val longitude: Double,
    val phone: String = "",
    val hours: String = "Lun-Sab: 10:00 - 20:00"
)

object StoresData {
    val stores = listOf(
        Store(
            id = 1,
            name = "Level Up Gamer Viña del Mar Centro",
            address = "Alvarez 1199",
            city = "Viña del Mar",
            latitude = -33.0245,
            longitude = -71.5516,
            phone = "+56 32 268 5000"
        ),
        Store(
            id = 2,
            name = "Level Up Gamer Viña del Mar 15 Norte",
            address = "15 Norte 961",
            city = "Viña del Mar",
            latitude = -33.0180,
            longitude = -71.5470,
            phone = "+56 32 268 5001"
        ),
        Store(
            id = 3,
            name = "Level Up Gamer Valparaíso",
            address = "Avenida Brasil 2020",
            city = "Valparaíso",
            latitude = -33.0458,
            longitude = -71.6197,
            phone = "+56 32 259 4000"
        )
    )
}

