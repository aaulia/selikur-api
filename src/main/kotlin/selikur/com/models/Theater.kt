package selikur.com.models

import kotlinx.serialization.Serializable

interface Theater {
    val id: String
    val name: String
}

@Serializable
data class TheaterEntry(
    override val id: String,
    override val name: String
) : Theater

@Serializable
data class TheaterDetail(
    override val id: String,
    override val name: String,

    val address: String,
    val phoneNo: String,
    val longlat: List<String>,
    val schedules: List<Schedule>
) : Theater

@Serializable
data class Schedule(
    val movie: MovieEntry,
    val duration: String,
    val price: String,
    val date: String,
    val time: List<String>
)