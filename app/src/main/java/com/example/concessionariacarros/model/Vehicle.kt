package com.example.concessionariacarros.model

import com.example.concessionariacarros.model.enum.VehicleType

data class Vehicle (
    var model: String ="",
    var price: Double = 0.0,
    var type:VehicleType,
    var sold: Boolean,
    var id: Long = 0L,
)