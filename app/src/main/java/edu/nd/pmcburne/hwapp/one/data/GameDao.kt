package edu.nd.pmcburne.hwapp.one.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM games WHERE date = :date AND gender = :gender")
    fun getGames(date: String, gender: String): Flow<List<Game>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGames(games: List<Game>)
}