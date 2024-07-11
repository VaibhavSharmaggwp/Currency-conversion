package com.example.coinswap.data.local.remote.repository

import com.example.coinswap.data.local.remote.repository.Dto.toCurrencyRte
import com.example.coinswap.data.local.remote.repository.entity.toCurrencyRate
import com.example.coinswap.data.local.remote.repository.entity.toCurrencyRateEntity
import com.example.coinswap.domain.model.repository.CurrencyRate
import com.example.coinswap.domain.model.repository.CurrencyRepository
import com.example.coinswap.domain.model.repository.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class RepositoryImpe(
    private val api: CurrencyApi,
    private val dao: CurrencyRateDao
): CurrencyRepository {

    override fun getCurrencyRatesList(): Flow<Resource<List<CurrencyRate>>> = flow {
        val localCurrencyRates = getLocalCurrencyRates()
        emit(Resource.Success(localCurrencyRates))

        // using try catch to get data from remote API
        try{
            val newRates = getRemoteCurrencyRates()
            updateLocalCurrencyRates(newRates)

            emit(Resource.Success(newRates))
        }catch (e: IOException){
            emit(
                Resource.Error(
                    message = "Couldn't reach server, check your Internet Connection",
                    data = localCurrencyRates
                )
            )
        } catch (e: Exception){
            emit(
                Resource.Error(
                    message = "OOPS something went wrong!! ${e.message}",
                    data = localCurrencyRates
                )
            )
        }

    }
    private suspend fun getLocalCurrencyRates(): List<CurrencyRate>{
        return dao.getAllCurrencyRates().map { it.toCurrencyRate() }
    }

    private suspend fun getRemoteCurrencyRates(): List<CurrencyRate>{
        val response = api.getLatestRates()
        return response.toCurrencyRte()
    }

    private suspend fun updateLocalCurrencyRates(currencyRate: List<CurrencyRate>){
        dao.upsertAll(currencyRate.map { it.toCurrencyRateEntity()})
    }
}