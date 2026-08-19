package com.ishakai.babusradio.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ishakai.babusradio.data.AppDatabase
import com.ishakai.babusradio.data.Station
import com.ishakai.babusradio.data.StationRepository
import com.ishakai.babusradio.network.RadioApiClient
import com.ishakai.babusradio.network.toStation
import com.ishakai.babusradio.player.RadioPlayerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RadioViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = StationRepository(database.stationDao())
    
    val playerManager = RadioPlayerManager(application)

    private val _topStations = MutableStateFlow<List<Station>>(emptyList())
    val topStations: StateFlow<List<Station>> = _topStations.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Station>>(emptyList())
    val searchResults: StateFlow<List<Station>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val favorites: StateFlow<List<Station>> = repository.favorites.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val history: StateFlow<List<Station>> = repository.history.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        loadTopStations()
    }

    fun loadTopStations() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val results = RadioApiClient.api.getTopStations(limit = 50)
                _topStations.value = results.map { it.toStation() }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchStations(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val results = RadioApiClient.api.searchStations(name = query)
                _searchResults.value = results.map { it.toStation() }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun playStation(station: Station) {
        playerManager.playStation(station)
        viewModelScope.launch {
            repository.addToHistory(station)
        }
    }

    fun toggleFavorite(station: Station) {
        viewModelScope.launch {
            repository.toggleFavorite(station)
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }
}
