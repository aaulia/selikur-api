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