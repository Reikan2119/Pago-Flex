package com.example.pagoflex.model

data class ReporteProblema(
    val id: Int,
    val idUsuario: Int,
    val motivo: String,
    val comentario: String,
    val fecha: String,
    val estado: String,
    val respuesta: String?,
    val fechaGestion: String?
)
