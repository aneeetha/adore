package com.example.adore.databsae

import android.content.Context
import androidx.room.*
import com.example.adore.models.Favo
import com.example.adore.models.Product

@Database(
    entities = [Product::class, Favo::class],
    version = 2
)
@TypeConverters(ListOfStringTypeConverter::class, InStockCountTypeConverter::class, CategoryTypeConverter::class)
abstract class AdoreDatabase: RoomDatabase() {

    abstract fun getProductDao():ProductDao
    abstract fun getFavoDao():FavoDao

    companion object{

        private var instance: AdoreDatabase? = null

        operator fun invoke(context: Context) = instance?: synchronized(this){
            instance?:createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AdoreDatabase::class.java,
            "adore_database"
        ).build()
    }
}