package selikur.com.models

import kotlinx.serialization.Serializable

interface Movie {
    val id: String
    val title: String
    val image: String
    val rating: String
    val type: String
}

@Serializable
data class MovieEntry(
    override val id: String,
    override val title: String,
    override val image: String,
    override val rating: String,
    override val type: String
) : Movie

@Serializable
data class MovieDetail(
    override val id: String,
    override val title: String,
    override val image: String,
    override val rating: String,
    override val type: String,

    val genre: List<String>,
    val duration: String,
    val trailer: String,
    val summary: String,
    val producers: List<String>,
    val directors: List<String>,
    val writers: List<String>,
    val casts: List<String>,
    val distributors: List<String>
) : Movie
