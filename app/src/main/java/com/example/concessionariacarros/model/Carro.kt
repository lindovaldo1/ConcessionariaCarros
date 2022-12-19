package com.example.concessionariacarros.model

import com.example.concessionariacarros.model.enum.TipoVeiculo

data class Carro (
    var modelo: String ="",
    var tipo:TipoVeiculo,
    var placa: Int = 0,
    var status: Boolean,
    var id: Long = 0L,
)