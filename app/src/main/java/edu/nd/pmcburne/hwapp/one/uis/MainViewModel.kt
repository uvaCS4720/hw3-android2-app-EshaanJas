package edu.nd.pmcburne.hwapp.one.uis

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.hwapp.one.data.AppDatabase
import edu.nd.pmcburne.hwapp.one.data.Game
import edu.nd.pmcburne.hwapp.one.data.GamesRepository
import edu.nd.pmcburne.hwapp.one.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val repo = GamesRepository(db.gameDao(), ApiService.create())

    private val _date = MutableStateFlow(LocalDate.now())
    private val _gender = MutableStateFlow("men")
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val date: StateFlow<LocalDate> = _date
    val gender: StateFlow<String> = _gender
    val isLoading: StateFlow<Boolean> = _isLoading

    val games: StateFlow<List<Game>> = combine(_date, _gender) { date, gender ->
        val y = date.year.toString()
        val m = date.monthValue.toString().padStart(2, '0')
        val d = date.dayOfMonth.toString().padStart(2, '0')
        repo.getGames("$y/$m/$d", gender)
    }.flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init { refresh() }

    fun setDate(date: LocalDate) { _date.value = date; refresh() }
    fun setGender(g: String) { _gender.value = g; refresh() }

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
            try { repo.refresh(_date.value, _gender.value) }
            catch (e: Exception) { _error.value = e.message }
            finally { _isLoading.value = false }
        }
    }
}