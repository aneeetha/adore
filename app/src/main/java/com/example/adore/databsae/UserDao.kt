package com.example.adore.databsae

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.adore.models.entities.Address
import com.example.adore.models.entities.AddressDetailUpdate
import com.example.adore.models.entities.User
import com.example.adore.models.entities.UserDetailUpdate
import com.example.adore.models.relations.UserWithAddresses

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User):Long

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM user")
    suspend fun deleteAll()

    @Update(entity = User::class)
    suspend fun updateUserDetails(obj: UserDetailUpdate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: Address)

    @Delete
    suspend fun deleteAddress(address: Address)

    @Query("SELECT * FROM address ORDER BY address_id DESC LIMIT 1")
    fun getLastInsertedAddress(): LiveData<Address>

    @Update(entity = Address::class)
    suspend fun updateAddress(obj: AddressDetailUpdate)

    @Query("Select * from user where user_id = :userId")
    fun getUser(userId: Int): LiveData<User>

    @Transaction
    @Query("Select * from user where mobile_no = :mobileNo limit 1")
    suspend fun getUserWithMobileNo(mobileNo: String): User?

    @Query("Select * from user")
    fun getAllUsers(): LiveData<List<User>>

    @Query("Select * from user where user_id = :userId")
    fun getUserWithAddresses(userId: Int): LiveData<UserWithAddresses>

}