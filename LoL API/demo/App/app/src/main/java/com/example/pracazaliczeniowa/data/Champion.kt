package com.example.pracazaliczeniowa.data

data class ChampionResponse(
    val data: Map<String, ChampionDto>
)

data class ChampionDto(
    val id: String,
    val name: String,
    val title: String,
    val blurb: String,
    val image: ChampionImageDto
)

data class ChampionImageDto(
    val full: String,
    val sprite: String,
    val group: String
)
