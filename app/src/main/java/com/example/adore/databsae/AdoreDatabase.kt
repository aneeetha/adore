package com.example.adore.databsae

import android.content.Context
import androidx.room.*
import com.example.adore.models.Favo
import com.example.adore.models.Product

@Database(
    entities = [Product::class],
    version = 2
)
@TypeConverters(ListOfStringTypeConverter::class,
    ListOfStockTypeConverter::class,
    CategoryTypeConverter::class,
    ProductCategoryTypeConverter::class,
    ColorTypeConverter::class,
    GenderTypeConverter::class)
abstract class AdoreDatabase: RoomDatabase() {

    abstract fun getProductDao():ProductDao

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