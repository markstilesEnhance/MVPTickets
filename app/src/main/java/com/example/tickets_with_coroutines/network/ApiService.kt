package com.example.tickets_with_coroutines.network

import com.example.tickets_with_coroutines.network.model.Price
import com.example.tickets_with_coroutines.network.model.Ticket
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("airline-tickets.php")
    fun searchTickets(@Query("from") from: String, @Query("to") to: String):
            Deferred<Response<MutableList<Ticket>>>

    @GET("airline-tickets-price.php")
    fun getPrice(@Query("flight_number") flightNumber: String, @Query("from") from: String, @Query("to") to: String):
            Deferred<Response<Price>>
}