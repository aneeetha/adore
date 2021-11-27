package com.example.adore.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.adore.models.enums.Gender
import java.util.*

@Entity(
    tableName = "user"
)
data class User(
    @ColumnInfo(name = "user_name")
    var userName: String,

    @ColumnInfo(name = "mobile_no")
    var mobileNo: String,

    @ColumnInfo(name = "password")
    var password: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var userId: Int? = null,

    var email: String? = null,
    var dob: Date? = null,
    var gender: Gender? = null
)

data class UserDetailUpdate(
    var user_id: Int?,
    var email: String? = null,
    var dob: Date? = null,
    var gender: Gender? = null
)
