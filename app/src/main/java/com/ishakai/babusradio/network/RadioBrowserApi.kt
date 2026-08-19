package com.ishakai.babusradio.network

import com.ishakai.babusradio.data.Station
import retrofit2.http.GET
import retrofit2.http.Query

interface RadioBrowserApi {
    @GET("json/stations/search")
    suspend fun searchStations(
        @Query("name") name: String? = null,
        @Query("countrycode") countryCode: String? = null,
        @Query("language") language: String? = null,
        @Query("tag") tag: String? = null,
        @Query("limit") limit: Int = 100,
        @Query("order") order: String = "votes",
        @Query("reverse") reverse: Boolean = true,
        @Query("hidebroken") hideBroken: Boolean = true
    ): List<StationDto>
    
    @GET("json/stations/topclick")
    suspend fun getTopStations(
        @Query("limit") limit: Int = 100,
        @Query("hidebroken") hideBroken: Boolean = true
    ): List<StationDto>
}
