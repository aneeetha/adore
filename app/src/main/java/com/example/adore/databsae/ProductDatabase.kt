package com.example.adore.databsae

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.adore.models.Product

@Database(
    entities = [Product::class],
    version = 1
)
abstract class ProductDatabase: RoomDatabase() {

    abstract fun getProductDatabaseDao():ProductDatabaseDao

    companion object{

        private var instance: ProductDatabase? = null

        operator fun invoke(context: Context) = instance?: synchronized(this){
            instance?:createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ProductDatabase::class.java,
            "product_database"
        ).build()
    }
}