package edu.nd.pmcburne.hwapp.one.uis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.nd.pmcburne.hwapp.one.data.Game


// composable that displays a single game card with teams, scores, and status
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

    val statusColor = when (game.status) {
        "in" -> Color(0xFFD32F2F)
        "post" -> Color(0xFF555555)
        else -> Color(0xFF1565C0)
    }

    val statusText = when (game.status) {
        "in" -> "● LIVE"
        "post" -> "FINAL"
        else -> game.startTime ?: "Upcoming"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(Modifier.padding(12.dp)) {
            // Status row
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(statusColor.copy(alpha = 0.1f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = statusText,
                        color = statusColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
                if (game.status == "in" && game.clock != null) {
                    Text(
                        text = "${periodLabel ?: ""} · ${game.clock}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Away team
            TeamRow(
                teamName = game.awayTeam,
                label = "AWY",
                score = game.awayScore,
                isWinner = isAwayWinner,
                showScore = game.status != "pre"
            )

            Spacer(Modifier.height(4.dp))
            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(Modifier.height(4.dp))

            // Home team
            TeamRow(
                teamName = game.homeTeam,
                label = "HME",
                score = game.homeScore,
                isWinner = isHomeWinner,
                showScore = game.status != "pre"
            )

            // Start time for upcoming games
            if (game.status == "pre" && game.startTime != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = game.startTime,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

// composable that displays one team row with name, label, and score
@Composable
fun TeamRow(
    teamName: String,
    label: String,
    score: Int?,
    isWinner: Boolean,
    showScore: Boolean
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 9.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(28.dp)
        )
        Text(
            text = teamName,
            fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f),
            color = if (isWinner)
                MaterialTheme.colorScheme.onSurface
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (showScore && score != null) {
            Text(
                text = score.toString(),
                fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Normal,
                fontSize = if (isWinner) 16.sp else 14.sp,
                color = if (isWinner)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}