package com.example.pracazaliczeniowa.network

import com.example.pracazaliczeniowa.data.ChampionResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://ddragon.leagueoflegends.com/"

/**
 * A public object that exposes the lazily-initialized Retrofit service.
 */
object LolApi {
    val retrofitService: LolApiService by lazy {
        retrofit.create(LolApiService::class.java)
    }
}

/**
 * A Retrofit service to fetch champion data from the LoL Data Dragon API.
 */
interface LolApiService {
    @GET("cdn/14.12.1/data/en_US/champion.json")
    suspend fun getChampions(): ChampionResponse

    @GET("cdn/14.12.1/data/en_US/champion/{championId}.json")
    suspend fun getChampion(@Path("championId") championId: String): ChampionResponse
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()
