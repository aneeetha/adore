package com.example.adore.databsae

import android.content.Context
import androidx.room.*
import com.example.adore.models.Product

@Database(
    entities = [Product::class],
    version = 1
)
@TypeConverters(ColorTypeConverter::class, InStockCountTypeConverter::class, CategoryTypeConverter::class)
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