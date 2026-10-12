package com.example.pagoflex.model

data class Usuario(
    val idUsuario: Int,
    val idRol: Int,
    val rut: String,
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val idEmpresa: Int? = null,
    val avisosActivos: Boolean? = null
)

data class EmpresaCliente(
    val idEmpresa: Int,
    val codigo: String,
    val nombreFantasia: String,
    val recargoAtraso: Int
)

data class Compromiso(
    val idCompromiso: Int,
    val folio: String,
    val idUsuarioFinal: Int,
    val idEmpresa: Int,
    val concepto: String,
    val cuota: String?,
    val monto: Int,
    val fechaVencimiento: String,
    var idEstadoCompromiso: Int
)

data class Pago(
    val idPago: Int,
    val numeroComprobante: String,
    val idCompromiso: Int,
    val fechaHora: String,
    val montoPagado: Int,
    val medioPago: String
)