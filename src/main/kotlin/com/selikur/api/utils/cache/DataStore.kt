package com.selikur.api.utils.cache

sealed interface DataStore {
    interface NoKey<T : Any> {
        fun exist(): Boolean
        fun fetch(): T?
        fun store(value: T): T
        fun clear()
    }

    interface Keyed<T : Any, U : Any> {
        fun exist(key: T): Boolean
        fun fetch(key: T): U?
        fun store(key: T, value: U): U
        fun store(key: T): (U) -> U
        fun clear()
    }
}
