package edu.nd.pmcburne.hwapp.one.uis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.nd.pmcburne.hwapp.one.data.Game

@Composable
fun GameCard(game: Game, gender: String) {
    val periodLabel = when {
        game.status == "post" -> "Final"
        game.status == "in" -> {
            val p = game.period ?: 1
            if (gender == "men") {
                if (p == 1) "1st Half" else "2nd Half"
            } else {
                listOf("1st", "2nd", "3rd", "4th").getOrNull(p - 1)?.plus(" Qtr") ?: "${p}th Qtr"
            }
        }
        else -> null
    }

    val isHomeWinner = game.status == "post" &&
            (game.homeScore ?: 0) > (game.awayScore ?: 0)
    val isAwayWinner = game.status == "post" &&
            (game.awayScore ?: 0) > (game.homeScore ?: 0)

    Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = when (game.status) {
                        "in" -> "LIVE"
                        "post" -> "FINAL"
                        else -> game.startTime ?: "Upcoming"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = when (game.status) {
                        "in" -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.weight(1f)
                )
                if (game.status == "in" && periodLabel != null) {
                    Text(
                        "$periodLabel · ${game.clock}",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            TeamRow(
                teamName = "${game.awayTeam} (Away)",
                score = game.awayScore,
                isWinner = isAwayWinner,
                showScore = game.status != "pre"
            )
            Spacer(Modifier.height(4.dp))
            TeamRow(
                teamName = "${game.homeTeam} (Home)",
                score = game.homeScore,
                isWinner = isHomeWinner,
                showScore = game.status != "pre"
            )
        }
    }
}

@Composable
fun TeamRow(teamName: String, score: Int?, isWinner: Boolean, showScore: Boolean) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = teamName,
            fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        if (showScore && score != null) {
            Text(
                text = score.toString(),
                fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Normal,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}