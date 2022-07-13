package com.selikur.api.models

import kotlinx.serialization.Serializable

interface Theater {
    val id: String
    val name: String

    @Serializable
    data class Entry(
        override val id: String,
        override val name: String
    ) : Theater

    @Serializable
    data class Detail(
        override val id: String,
        override val name: String,

        val address: String,
        val phoneNo: String,
        val longlat: List<String>,
        val schedules: List<Schedule>
    ) : Theater {
        @Serializable
        data class Schedule(
            val movie: Movie.Entry,
            val duration: String,
            val price: String,
            val date: String,
            val time: List<String>
        )
    }
}
