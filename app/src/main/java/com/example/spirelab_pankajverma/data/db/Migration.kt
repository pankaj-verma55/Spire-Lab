package com.example.spirelab_pankajverma.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE product_cart_item ADD COLUMN count INTEGER NOT NULL DEFAULT 1")
    }
}