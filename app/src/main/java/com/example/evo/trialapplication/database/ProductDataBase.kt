package com.example.evo.trialapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.evo.trialapplication.interview.CartListModel

@Database(entities = [ProductData::class,CartListModel::class,AddNewItem::class], version = 4)
abstract class ProductDataBase:RoomDatabase() {

    abstract fun productDao():ProductDao

    companion object {

        @Volatile
        private var INSTANCE: ProductDataBase? = null

        fun getDatabase(context: Context): ProductDataBase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, ProductDataBase::class.java, "student_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance

            }
        }

    }

}