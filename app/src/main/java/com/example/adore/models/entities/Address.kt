package com.example.adore.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.adore.models.enums.District
import java.io.Serializable

@Entity(
    tableName = "address"
)
data class Address(
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "address_type")
    var addressType: String,
    @ColumnInfo(name = "address_line")
    var addressLine: String?=null,
    var name: String?=null,
    var contact:String?=null,
    var locality: String?=null,
    var city: District?=null,
    var pincode: Int?=null,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "address_id")
    var addressId:Int? = null,
    var state: String = "TAMIL NADU"
):Serializable

data class AddressDetailUpdate(
    var address_id: Int?,
    var address_type: String,
    var address_line: String?,
    var name: String?,
    var contact:String?,
    var locality: String?,
    var city: District?,
    var pincode: Int?
)