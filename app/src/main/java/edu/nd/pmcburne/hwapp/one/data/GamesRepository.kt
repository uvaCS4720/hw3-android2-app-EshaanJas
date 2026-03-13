package edu.nd.pmcburne.hwapp.one.data


import edu.nd.pmcburne.hwapp.one.network.ApiService
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import kotlin.collections.emptyList

class GamesRepository(private val dao: GameDao, private val api: ApiService) {

    fun getGames(date: String, gender: String): Flow<List<Game>> =
        dao.getGames(date, gender)

    suspend fun refresh(date: LocalDate, gender: String) {
        val y = date.year.toString()
        val m = date.monthValue.toString().padStart(2, '0')
        val d = date.dayOfMonth.toString().padStart(2, '0')
        val dateKey = "$y/$m/$d"

        val response = api.getScores(gender, y, m, d)
        val games = response.events?.mapNotNull { event ->
            val comp = event.competitions?.firstOrNull() ?: return@mapNotNull null
            val home = comp.competitors?.find { it.homeAway == "home" } ?: return@mapNotNull null
            val away = comp.competitors?.find { it.homeAway == "away" } ?: return@mapNotNull null
            val status = comp.status
            val statusName = status?.type?.name ?: "STATUS_SCHEDULED"
            Game(
                gameId = event.id,
                gender = gender,
                homeTeam = home.team.displayName,
                awayTeam = away.team.displayName,
                homeScore = home.score?.toIntOrNull(),
                awayScore = away.score?.toIntOrNull(),
                status = when (statusName) {
                    "STATUS_IN_PROGRESS" -> "in"
                    "STATUS_FINAL" -> "post"
                    else -> "pre"
                },
                startTime = status?.type?.shortDetail,
                period = status?.period,
                clock = status?.displayClock,
                date = dateKey
            )
        } ?: emptyList()

        dao.upsertGames(games)
    }
}