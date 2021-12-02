package com.example.adore.databsae

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.adore.models.dataClasses.Favo

@Dao
interface FavoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favo: Favo):Long

    @Query("Update favo set favlist = :favlist where userId = :userId")
    suspend fun update(userId: String, favlist: List<String>)

    @Query("select favlist from favo where userId = :userId")
    suspend fun getFavlist(userId: String):List<String>
}