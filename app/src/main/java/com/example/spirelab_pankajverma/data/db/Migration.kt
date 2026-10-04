package com.example.spirelab_pankajverma.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {

//        db.execSQL(
//            """
//            CREATE TABLE IF NOT EXISTS product_cart_item (
//                productId INTEGER NOT NULL,
//                images TEXT NOT NULL,
//                title TEXT NOT NULL,
//                description TEXT NOT NULL,
//                price REAL NOT NULL,
//                rating REAL NOT NULL,
//                category TEXT NOT NULL,
//                brand TEXT NOT NULL,
//                stock INTEGER NOT NULL,
//                thumbnail TEXT NOT NULL,
//                quantity INTEGER NOT NULL,
//                PRIMARY KEY(productId)
//            )
//            """.trimIndent()
//        )
        db.execSQL("ALTER TABLE product_cart_item ADD COLUMN count INTEGER NOT NULL DEFAULT 1")
    }
}