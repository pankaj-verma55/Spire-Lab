package com.example.spirelab_pankajverma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.spirelab_pankajverma.data.utility.Converters


@Database(entities = [ProductEntity::class,ProductCatalogEntity::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ProductLocalDataBase : RoomDatabase() {
    abstract fun cartDao(): ProductDao
}