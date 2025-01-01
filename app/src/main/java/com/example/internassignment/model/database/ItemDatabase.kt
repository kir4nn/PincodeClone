package com.example.internassignment.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.internassignment.model.ItemEntity

@Database(entities = [ItemEntity::class], version=1)
abstract class ItemDatabase:RoomDatabase() {
    abstract fun itemDao(): ItemDao
    companion object {
        @Volatile
        private var INSTANCE: ItemDatabase? = null

        fun getDatabase(context: Context): ItemDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    ItemDatabase::class.java,
                    "item_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}