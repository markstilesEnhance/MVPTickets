package com.example.tickets_with_coroutines.network

import com.example.tickets_with_coroutines.network.model.Price
import com.example.tickets_with_coroutines.network.model.Ticket

class Repository(private val apiService: ApiService): BaseRepository() {

    suspend fun tickets(from: String, to: String): MutableList<Ticket>? {
        return safeApiCall(
            call = {apiService.searchTickets(from, to).await()},
            error = "Error fetching tickets"
        )
    }

    suspend fun prices(flightNumber: String, from: String, to: String): Price? {
        return safeApiCall(
            call = {apiService.getPrice(flightNumber, from, to).await()},
            error = "Error fetching price"
        )
    }
}