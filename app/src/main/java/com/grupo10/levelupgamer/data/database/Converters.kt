package com.grupo10.levelupgamer.data.database

import androidx.room.TypeConverter
import com.grupo10.levelupgamer.model.ProductCategory
import com.grupo10.levelupgamer.model.NotificationType

class Converters {

    @TypeConverter
    fun fromProductCategory(value: ProductCategory): String {
        return value.name
    }

    @TypeConverter
    fun toProductCategory(value: String): ProductCategory {
        return ProductCategory.valueOf(value)
    }

    @TypeConverter
    fun fromNotificationType(value: NotificationType): String {
        return value.name
    }

    @TypeConverter
    fun toNotificationType(value: String): NotificationType {
        return NotificationType.valueOf(value)
    }
}

