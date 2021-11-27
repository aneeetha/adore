package com.example.adore.databsae

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.adore.models.entities.*


@Database(
    entities = [
        Product::class,
        User::class,
        Address::class
    ],
    version = 1
)
@TypeConverters(ListOfStringTypeConverter::class,
    ListOfStockTypeConverter::class,
    CategoryTypeConverter::class,
    ProductCategoryTypeConverter::class,
    ColorTypeConverter::class,
    GenderTypeConverter::class,
    DateTypeConvertor::class, DistrictTypeConverter::class)
abstract class AdoreDatabase: RoomDatabase() {

    abstract fun getProductDao():ProductDao

    abstract fun getUserDao(): UserDao

    companion object{

        private var instance: AdoreDatabase? = null

        operator fun invoke(context: Context) = instance?: synchronized(this){
            instance?:createDatabase(context).also { instance = it }
        }

//        private val MIGRATION_1_2 = object: Migration(1, 2){
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("CREATE TABLE users_new(user_id INTEGER, user_name TEXT NOT NULL, mobile_no TEXT NOT NULL, password TEXT NOT NULL,  email TEXT, dob BIGINT, gender TEXT, PRIMARY KEY(user_id)) ")
//                database.execSQL("INSERT INTO users_new(user_id, user_name, password, mobile_no) SELECT user_id, user_name, password, mobile_no FROM user")
//                database.execSQL("DROP TABLE user")
//                database.execSQL("ALTER TABLE users_new RENAME TO user")
//            }
//        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AdoreDatabase::class.java,
            "adore_database"
        )
            .build()

    }
}