package com.ishakai.babusradio.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "stations")
data class Station(
    @PrimaryKey
    val stationuuid: String,
    val name: String,
    val url: String,
    val urlResolved: String,
    val homepage: String?,
    val favicon: String?,
    val tags: String?,
    val country: String?,
    val countryCode: String?,
    val state: String?,
    val language: String?,
    val votes: Int,
    val bitrate: Int,
    val codec: String?,
    val geoLat: Double?,
    val geoLong: Double?,
    val frequency: String?,
    val city: String?,
    val description: String?,
    val isFavorite: Boolean = false,
    val lastPlayedTime: Long = 0L
)
