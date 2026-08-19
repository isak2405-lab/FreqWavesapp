package com.ishakai.babusradio.data

import kotlinx.coroutines.flow.Flow

class StationRepository(private val stationDao: StationDao) {
    val favorites: Flow<List<Station>> = stationDao.getFavorites()
    val history: Flow<List<Station>> = stationDao.getHistory()

    suspend fun insertStation(station: Station) = stationDao.insertStation(station)
    
    suspend fun toggleFavorite(station: Station) {
        val existing = stationDao.getStationById(station.stationuuid)
        if (existing == null) {
            stationDao.insertStation(station.copy(isFavorite = true))
        } else {
            stationDao.updateFavorite(station.stationuuid, !existing.isFavorite)
        }
    }
    
    suspend fun addToHistory(station: Station) {
        val existing = stationDao.getStationById(station.stationuuid)
        if (existing == null) {
            stationDao.insertStation(station.copy(lastPlayedTime = System.currentTimeMillis()))
        } else {
            stationDao.updateLastPlayedTime(station.stationuuid, System.currentTimeMillis())
        }
    }
}
