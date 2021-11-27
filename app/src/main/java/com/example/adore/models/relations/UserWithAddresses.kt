package com.example.adore.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.adore.models.entities.Address
import com.example.adore.models.entities.User

data class UserWithAddresses(
    @Embedded val user: User,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "user_id"
    )
    val addresses: List<Address>
)
