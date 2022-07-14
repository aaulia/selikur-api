package com.selikur.api.utils.cache

import com.selikur.api.utils.cache.Entry.Empty
import com.selikur.api.utils.cache.Entry.Exist
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.reflect.KClass

private sealed interface Entry {
    data class Exist<T : Any>(
        private val data: T,
        private val createdAt: Long = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
        private val maxAge: Long = 3600
    ) : Entry {
        fun get(): T? {
            val epoch = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            val delta = epoch - createdAt

            return if (delta <= maxAge) data else null
        }
    }

    object Empty : Entry
}

sealed interface Cache {
    class NoKey<T : Any> : Cache, DataStore.NoKey<T> {
        private var entry: Entry = Empty

        override fun exist(): Boolean = entry !is Empty

        @Suppress("UNCHECKED_CAST")
        override fun fetch(): T? = when (entry) {
            is Exist<*> -> (entry as Exist<T>).get()
            else -> null
        }

        override fun clear() {
            entry = Empty
        }

        override fun store(value: T): T = value.also { entry = Exist(value) }
    }

    class Keyed<T : Any, U : Any> : Cache, DataStore.Keyed<T, U> {
        private val cache = mutableMapOf<T, Entry>()

        override fun exist(key: T): Boolean {
            return cache[key]
                ?.let { entry -> entry !is Empty }
                ?: false
        }

        @Suppress("UNCHECKED_CAST")
        override fun fetch(key: T): U? {
            return cache[key]?.let { entry ->
                when (entry) {
                    is Exist<*> -> (entry as Exist<U>).get()
                    else -> null
                }
            }
        }

        override fun store(key: T, value: U): U {
            return value.also { cache[key] = Exist(value) }
        }

        override fun store(key: T): (U) -> U {
            return { value -> store(key, value) }
        }

        fun store(key: T, getter: (T) -> U): U {
            return store(key, getter(key))
        }

        override fun clear() {
            cache.clear()
        }
    }
}

typealias CacheMap = Map<KClass<*>, Cache>

inline operator fun <reified T> CacheMap.invoke(): Cache {
    return getValue(T::class)
}
