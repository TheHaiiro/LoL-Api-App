package com.example.pracazaliczeniowa.ui.champion_detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pracazaliczeniowa.data.AppDatabase
import com.example.pracazaliczeniowa.data.ChampionEntity
import com.example.pracazaliczeniowa.data.ChampionsRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the [ChampionDetailFragment].
 * It is responsible for fetching and holding the details of a single, specific champion.
 */
class ChampionDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ChampionsRepository

    private val _champion = MutableLiveData<ChampionEntity>()
    val champion: LiveData<ChampionEntity> = _champion

    init {
        val championDao = AppDatabase.getDatabase(application).championDao()
        repository = ChampionsRepository(championDao)
    }

    /**
     * Fetches the details for a specific champion from the repository.
     */
    fun getChampionDetails(championId: String) {
        viewModelScope.launch {
            repository.getChampionDetails(championId).collect {
                _champion.value = it
            }
        }
    }
}
