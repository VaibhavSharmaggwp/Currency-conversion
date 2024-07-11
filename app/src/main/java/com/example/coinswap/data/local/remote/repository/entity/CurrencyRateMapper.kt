package com.example.coinswap.data.local.remote.repository.entity


// This is mapper user to convert CurrencyRateEntity to Currency Rate
import com.example.coinswap.domain.model.repository.CurrencyRate

fun CurrencyRateEntity.toCurrencyRate(): CurrencyRate{
    return CurrencyRate(
        code = code,
        name  = name,
        rate = rate
    )
}

fun CurrencyRate.toCurrencyRateEntity(): CurrencyRateEntity{
    return CurrencyRateEntity(
        code = code,
        name  = name,
        rate = rate
    )
}