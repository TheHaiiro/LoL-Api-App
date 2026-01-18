package com.example.pracazaliczeniowa.data

import com.example.pracazaliczeniowa.network.LolApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

/**
 * Repository module for handling data operations.
 * This repository is the single source of truth for all champion data. It is responsible
 * for fetching data from the network and storing it in a local database, providing a
 * consistent API for the rest of the app.
 */
class ChampionsRepository(private val championDao: ChampionDao) {

    /**
     * Gets a flow of all champions. It first emits the cached data from the local
     * database, then attempts to fetch fresh data from the network. If the network
     * fetch is successful, it updates the database and emits the new list.
     */
    fun getChampions(): Flow<List<ChampionEntity>> = flow {
        // 1. Emit the data from the local database first.
        emit(championDao.getAllChampions().first())

        // 2. Try to fetch fresh data from the network.
        try {
            val networkResponse = LolApi.retrofitService.getChampions()
            val championEntities = networkResponse.data.values.map { dto ->
                ChampionEntity(
                    id = dto.id,
                    name = dto.name,
                    title = dto.title,
                    blurb = dto.blurb,
                    imageUrl = "https://ddragon.leagueoflegends.com/cdn/14.12.1/img/champion/${dto.image.full}"
                )
            }
            // 3. Save the fresh data to the local database.
            championEntities.forEach { championDao.insertChampion(it) }

            // 4. Emit the fresh data to update the UI.
            emit(championEntities)
        } catch (e: Exception) {
            // If the network fails, the flow simply completes, and the UI
            // continues to display the old data from the database.
        }
    }

    /**
     * Gets a flow for a single champion's details. It follows the same "single source
     * of truth" pattern as getChampions().
     */
    fun getChampionDetails(championId: String): Flow<ChampionEntity?> = flow {
        // 1. Emit the cached data from the local database first.
        emit(championDao.getChampionById(championId).first())

        // 2. Try to fetch fresh data from the network.
        try {
            val networkResponse = LolApi.retrofitService.getChampion(championId)
            val dto = networkResponse.data.values.first()
            val championEntity = ChampionEntity(
                id = dto.id,
                name = dto.name,
                title = dto.title,
                blurb = dto.blurb,
                imageUrl = "https://ddragon.leagueoflegends.com/cdn/14.12.1/img/champion/${dto.image.full}"
            )
            // 3. Save the fresh data to the local database.
            championDao.insertChampion(championEntity)

            // 4. Emit the fresh data to update the UI.
            emit(championEntity)
        } catch (e: Exception) {
            // If the network fails, the flow simply completes.
        }
    }
}
