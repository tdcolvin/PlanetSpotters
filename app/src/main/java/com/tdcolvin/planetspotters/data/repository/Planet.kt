package com.tdcolvin.planetspotters.data.repository

import java.util.Date

data class Planet(
    val planetId: String? = null,
    val name: String,
    val distanceLy: Float,
    val discovered: Date
)
