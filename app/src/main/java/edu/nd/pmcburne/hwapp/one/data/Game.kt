package edu.nd.pmcburne.hwapp.one.data

import androidx.room.Entity


// room database entity which for a single basketball game
@Entity(tableName = "games", primaryKeys = ["gameId", "gender"])
data class Game(
    val gameId: String,
    val gender: String,
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: Int?,
    val awayScore: Int?,
    val status: String,
    val startTime: String?,
    val period: Int?,
    val clock: String?,
    val date: String
)