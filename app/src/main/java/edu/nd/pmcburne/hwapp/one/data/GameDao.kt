package edu.nd.pmcburne.hwapp.one.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


// DAO interface for reading and writing games to the room database
@Dao
interface GameDao {

    // games for a given date and gender, auto-updates when DB changes
    @Query("SELECT * FROM games WHERE date = :date AND gender = :gender")
    fun getGames(date: String, gender: String): Flow<List<Game>>

    // keeps score up to date
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGames(games: List<Game>)
}