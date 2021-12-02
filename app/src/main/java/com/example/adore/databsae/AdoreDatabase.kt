package com.example.adore.databsae

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.adore.models.entities.*


@Database(
    entities = [
        User::class,
        Address::class
    ],
    version = 1
)
@TypeConverters(ListOfStringTypeConverter::class,
    GenderTypeConverter::class,
    DateTypeConvertor::class, DistrictTypeConverter::class)
abstract class AdoreDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao

    companion object{

        private var instance: AdoreDatabase? = null

        operator fun invoke(context: Context) = instance?: synchronized(this){
            instance?:createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AdoreDatabase::class.java,
            "adore_database"
        )
            .build()

    }
}