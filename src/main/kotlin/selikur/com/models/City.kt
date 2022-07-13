package selikur.com.models

import kotlinx.serialization.Serializable

interface City {
    val id: String
    val name: String
}

@Serializable
data class CityEntry(
    override val id: String,
    override val name: String
) : City

@Serializable
data class CityDetail(
    override val id: String,
    override val name: String,

    val xxi: List<TheaterEntry>,
    val premiere: List<TheaterEntry>,
    val imax: List<TheaterEntry>
) : City

