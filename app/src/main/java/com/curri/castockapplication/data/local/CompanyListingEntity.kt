package com.curri.castockapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CompanyListingEntity(
    val name: String,
    val symbols: String,
    val exchange: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)
