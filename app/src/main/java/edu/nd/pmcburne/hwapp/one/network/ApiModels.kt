package edu.nd.pmcburne.hwapp.one.network

data class ScoreboardResponse(val games: List<GameWrapper>?)
data class GameWrapper(val game: GameData?)
data class GameData(
    val gameID: String,
    val home: TeamData?,
    val away: TeamData?,
    val gameState: String?,   // "final", "live", "pre"
    val currentPeriod: String?,
    val contestClock: String?,
    val startTime: String?
)
data class TeamData(
    val names: TeamNames?,
    val score: String?,
    val winner: Boolean?
)
data class TeamNames(
    val short: String?
)