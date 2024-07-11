package com.example.coinswap.data.local.remote.repository.entity

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity
class CurrencyRateEntity (
    @PrimaryKey
    val code: String,
    val name: String,
    val rate: Double
){
}