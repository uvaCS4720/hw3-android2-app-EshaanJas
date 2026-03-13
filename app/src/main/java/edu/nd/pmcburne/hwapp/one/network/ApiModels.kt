package edu.nd.pmcburne.hwapp.one.network

data class ScoreboardResponse(val events: List<Event>?)
data class Event(val id: String, val date: String, val competitions: List<Competition>?)
data class Competition(
    val competitors: List<Competitor>?,
    val status: CompetitionStatus?
)
data class Competitor(val homeAway: String, val team: Team, val score: String?)
data class Team(val displayName: String)
data class CompetitionStatus(
    val type: StatusType?,
    val displayClock: String?,
    val period: Int?
)
data class StatusType(val name: String, val shortDetail: String?)