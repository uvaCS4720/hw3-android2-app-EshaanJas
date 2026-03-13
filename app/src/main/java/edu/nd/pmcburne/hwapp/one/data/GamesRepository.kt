package edu.nd.pmcburne.hwapp.one.data

import android.util.Log
import edu.nd.pmcburne.hwapp.one.network.ApiService
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GamesRepository(private val dao: GameDao, private val api: ApiService) {

    fun getGames(date: String, gender: String): Flow<List<Game>> =
        dao.getGames(date, gender)

    suspend fun refresh(date: LocalDate, gender: String) {
        val y = date.year.toString()
        val m = date.monthValue.toString().padStart(2, '0')
        val d = date.dayOfMonth.toString().padStart(2, '0')
        val dateKey = "$y/$m/$d"

        Log.d("GamesRepo", "Fetching: basketball-$gender/d1/$y/$m/$d")

        try {
            val response = api.getScores(gender, y, m, d)
            Log.d("GamesRepo", "Response games count: ${response.games?.size}")

            val games = response.games?.mapNotNull { wrapper ->
                val g = wrapper.game ?: return@mapNotNull null
                val status = when (g.gameState?.lowercase()) {
                    "live", "in_progress" -> "in"
                    "final" -> "post"
                    else -> "pre"
                }
                Game(
                    gameId = g.gameID,
                    gender = gender,
                    homeTeam = g.home?.names?.short ?: "Home",
                    awayTeam = g.away?.names?.short ?: "Away",
                    homeScore = g.home?.score?.toIntOrNull(),
                    awayScore = g.away?.score?.toIntOrNull(),
                    status = status,
                    startTime = g.startTime,
                    period = null,
                    clock = g.contestClock,
                    date = dateKey
                )
            } ?: emptyList()

            Log.d("GamesRepo", "Parsed ${games.size} games, saving to DB")
            dao.upsertGames(games)

        } catch (e: Exception) {
            Log.e("GamesRepo", "Error fetching games: ${e.message}", e)
        }
    }
}