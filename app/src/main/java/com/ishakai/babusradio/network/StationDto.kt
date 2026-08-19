package com.ishakai.babusradio.network

import com.google.gson.annotations.SerializedName
import com.ishakai.babusradio.data.Station
import java.util.UUID

data class StationDto(
    @SerializedName("stationuuid") val stationuuid: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("url_resolved") val urlResolved: String?,
    @SerializedName("homepage") val homepage: String?,
    @SerializedName("favicon") val favicon: String?,
    @SerializedName("tags") val tags: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("countrycode") val countrycode: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("language") val language: String?,
    @SerializedName("votes") val votes: Int?,
    @SerializedName("bitrate") val bitrate: Int?,
    @SerializedName("codec") val codec: String?,
    @SerializedName("geo_lat") val geoLat: Double?,
    @SerializedName("geo_long") val geoLong: Double?
)

fun StationDto.toStation(): Station {
    return Station(
        stationuuid = this.stationuuid ?: UUID.randomUUID().toString(),
        name = this.name?.trim() ?: "Unknown Station",
        url = this.url ?: "",
        urlResolved = this.urlResolved ?: this.url ?: "",
        homepage = this.homepage,
        favicon = this.favicon,
        tags = this.tags,
        country = this.country,
        countryCode = this.countrycode,
        state = this.state,
        language = this.language,
        votes = this.votes ?: 0,
        bitrate = this.bitrate ?: 0,
        codec = this.codec,
        geoLat = this.geoLat,
        geoLong = this.geoLong,
        frequency = null,
        city = null,
        description = null
    )
}
