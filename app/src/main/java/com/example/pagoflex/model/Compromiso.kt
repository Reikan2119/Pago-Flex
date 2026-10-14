package com.example.pagoflex.model
data class Compromiso(
    val idCompromiso: Int,
    val folio: String,
    val idUsuarioFinal: Int,
    val idEmpresa: Int,
    val concepto: String,
    val cuota: String,
    val monto: Int,
    val fechaVencimiento: String,
    val idEstadoCompromiso: Int
)