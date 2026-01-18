package com.example.pracazaliczeniowa.ui.champions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pracazaliczeniowa.data.AppDatabase
import com.example.pracazaliczeniowa.data.ChampionEntity
import com.example.pracazaliczeniowa.data.ChampionsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel for the [ChampionsListFragment].
 * It is responsible for fetching and holding the list of all champions, as well as
 * handling search queries.
 */
class ChampionsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ChampionsRepository

    private val _champions = MutableLiveData<List<ChampionEntity>>()
    val champions: LiveData<List<ChampionEntity>> = _champions

    private var allChampions: List<ChampionEntity> = emptyList()

    init {
        val championDao = AppDatabase.getDatabase(application).championDao()
        repository = ChampionsRepository(championDao)
        getChampionsList()
    }

    /**
     * Fetches the list of champions from the repository and updates the LiveData.
     */
    private fun getChampionsList() {
        viewModelScope.launch {
            repository.getChampions().collect {
                allChampions = it
                _champions.value = allChampions
            }
        }
    }

    /**
     * Filters the champion list based on a search query.
     * If the query is null or empty, it displays the full list.
     */
    fun searchChampions(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            allChampions
        } else {
            allChampions.filter { it.name.contains(query, ignoreCase = true) }
        }
        _champions.value = filteredList
    }
}
