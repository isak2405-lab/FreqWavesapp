package com.ishakai.babusradio.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StationDao {
    @Query("SELECT * FROM stations WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavorites(): Flow<List<Station>>

    @Query("SELECT * FROM stations WHERE lastPlayedTime > 0 ORDER BY lastPlayedTime DESC LIMIT 20")
    fun getHistory(): Flow<List<Station>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStation(station: Station)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<Station>)

    @Query("UPDATE stations SET isFavorite = :isFavorite WHERE stationuuid = :id")
    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    @Query("UPDATE stations SET lastPlayedTime = :time WHERE stationuuid = :id")
    suspend fun updateLastPlayedTime(id: String, time: Long)
    
    @Query("SELECT * FROM stations WHERE stationuuid = :id")
    suspend fun getStationById(id: String): Station?
}
