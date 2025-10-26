package com.grupo10.levelupgamer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val price: Double,
    val description: String,
    val category: ProductCategory,
    val imageUrl: String = "",
    val stock: Int = 0,
    val discount: Int = 0, // Porcentaje de descuento
    val rating: Double = 0.0 // Rating del producto (e.g., de 0 a 5)
) {
    val finalPrice: Double
        get() = if (discount > 0) {
            price - (price * discount / 100)
        } else {
            price
        }

    val hasDiscount: Boolean
        get() = discount > 0
}

// Datos de ejemplo
object ProductsData {
    val sampleProducts = listOf(
        Product(
            id = 1,
            name = "PlayStation 5",
            price = 499990.0,
            description = "Consola de última generación",
            category = ProductCategory.CONSOLAS,
            stock = 15,
            discount = 10,
            rating = 4.8
        ),
        Product(
            id = 2,
            name = "Xbox Series X",
            price = 479990.0,
            description = "Consola de nueva generación",
            category = ProductCategory.CONSOLAS,
            stock = 20,
            rating = 4.7
        ),
        Product(
            id = 3,
            name = "Nintendo Switch OLED",
            price = 349990.0,
            description = "Consola híbrida con pantalla OLED",
            category = ProductCategory.CONSOLAS,
            stock = 30,
            discount = 5,
            rating = 4.6
        ),
        Product(
            id = 4,
            name = "The Last of Us Part II",
            price = 39990.0,
            description = "Juego de acción y aventura",
            category = ProductCategory.JUEGOS,
            stock = 50,
            discount = 20,
            rating = 4.9
        ),
        Product(
            id = 5,
            name = "God of War Ragnarök",
            price = 59990.0,
            description = "Épica aventura nórdica",
            category = ProductCategory.JUEGOS,
            stock = 40,
            rating = 4.9
        ),
        Product(
            id = 6,
            name = "DualSense Controller",
            price = 69990.0,
            description = "Control inalámbrico PS5",
            category = ProductCategory.ACCESORIOS,
            stock = 100,
            discount = 15,
            rating = 4.5
        ),
        Product(
            id = 7,
            name = "Zelda: Tears of Kingdom",
            price = 54990.0,
            description = "Aventura épica de mundo abierto",
            category = ProductCategory.JUEGOS,
            stock = 35,
            rating = 5.0
        ),
        Product(
            id = 8,
            name = "Auriculares Gaming RGB",
            price = 89990.0,
            description = "Audio 7.1 surround",
            category = ProductCategory.ACCESORIOS,
            stock = 25,
            discount = 25,
            rating = 4.3
        )
    )
}
