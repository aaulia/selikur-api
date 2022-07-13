package com.selikur.api.models

import kotlinx.serialization.Serializable

interface City {
    val id: String
    val name: String

    @Serializable
    data class Entry(
        override val id: String,
        override val name: String
    ) : City

    @Serializable
    data class Detail(
        override val id: String,
        override val name: String,

        val xxi: List<Theater.Entry>,
        val premiere: List<Theater.Entry>,
        val imax: List<Theater.Entry>
    ) : City
}
