package com.grupo10.levelupgamer.data.mapper

import com.grupo10.levelupgamer.data.remote.dto.StoreDto
import com.grupo10.levelupgamer.model.Store

fun StoreDto.toDomain(): Store {
    return Store(
        id = this.id,
        name = this.name,
        address = this.address,
        city = this.city,
        latitude = this.latitude,
        longitude = this.longitude,
        phone = this.phone,
        hours = this.hours
    )
}

fun List<StoreDto>.toDomain(): List<Store> {
    return this.map { it.toDomain() }
}

