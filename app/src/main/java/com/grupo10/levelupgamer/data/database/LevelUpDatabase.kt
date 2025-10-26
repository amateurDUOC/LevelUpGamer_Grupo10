package com.grupo10.levelupgamer.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grupo10.levelupgamer.data.dao.CartDao
import com.grupo10.levelupgamer.data.dao.NotificationDao
import com.grupo10.levelupgamer.data.dao.ProductDao
import com.grupo10.levelupgamer.model.CartItem
import com.grupo10.levelupgamer.model.Notification
import com.grupo10.levelupgamer.model.Product

@Database(entities = [CartItem::class, Notification::class, Product::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LevelUpDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao
    abstract fun notificationDao(): NotificationDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: LevelUpDatabase? = null

        fun getDatabase(context: Context): LevelUpDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LevelUpDatabase::class.java,
                    "levelup_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

