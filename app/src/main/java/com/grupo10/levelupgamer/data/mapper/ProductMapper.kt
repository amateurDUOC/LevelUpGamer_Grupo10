package com.grupo10.levelupgamer.data.mapper

import com.grupo10.levelupgamer.data.remote.dto.ProductDto
import com.grupo10.levelupgamer.model.Product
import com.grupo10.levelupgamer.model.ProductCategory

fun ProductDto.toDomain(): Product {
    return Product(
        id = this.id,
        name = this.name,
        price = this.price,
        description = this.description,
        category = mapCategory(this.category.name),
        imageUrl = this.imageUrl,
        stock = this.stock,
        discount = this.discount,
        rating = this.rating
    )
}

private fun mapCategory(categoryName: String): ProductCategory {
    return when (categoryName.uppercase()) {
        "CONSOLAS" -> ProductCategory.CONSOLAS
        "JUEGOS" -> ProductCategory.JUEGOS
        "ACCESORIOS" -> ProductCategory.ACCESORIOS
        else -> ProductCategory.JUEGOS // Default fallback
    }
}

fun List<ProductDto>.toDomain(): List<Product> {
    return this.map { it.toDomain() }
}

