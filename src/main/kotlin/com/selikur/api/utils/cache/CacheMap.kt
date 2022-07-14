package com.selikur.api.utils.cache

import kotlin.reflect.KClass

typealias CacheMap = Map<KClass<*>, Cache>

inline operator fun <reified T> CacheMap.invoke(): Cache {
    return getValue(T::class)
}
