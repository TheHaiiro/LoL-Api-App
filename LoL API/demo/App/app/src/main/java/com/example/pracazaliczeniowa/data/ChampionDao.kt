package com.example.pracazaliczeniowa.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChampionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChampion(champion: ChampionEntity)

    @Query("SELECT * FROM champions WHERE id = :championId")
    fun getChampionById(championId: String): Flow<ChampionEntity?>

    @Query("SELECT * FROM champions ORDER BY name ASC")
    fun getAllChampions(): Flow<List<ChampionEntity>>
}
