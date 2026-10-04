package com.example.spirelab_pankajverma.data.db

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    fun provideDataBase(context: Context): ProductLocalDataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            ProductLocalDataBase::class.java,
            "product_cart_item"
        )
            .addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .build()
    }
}