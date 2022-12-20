package com.example.concessionariacarros.model

import com.example.concessionariacarros.model.enum.TipoVeiculo

data class Carro (
    var modelo: String ="",
    var tipo:TipoVeiculo,
    var preco: Double = 0.0,
    var vendido: Boolean,
    var id: Long = 0L,
)