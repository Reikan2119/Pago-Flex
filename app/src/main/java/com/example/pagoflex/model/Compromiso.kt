package com.example.pagoflex.model
data class Compromiso(
    val idCompromiso: Int,
    val folio: String,
    val idUsuarioFinal: Int,
    val idEmpresa: Int,
    val nombreEmpresa: String = "Empresa #$idEmpresa",
    val concepto: String,
    val cuota: String,
    val monto: Int,
    val fechaVencimiento: String,
    val idEstadoCompromiso: Int
) {
    fun estaVencido(): Boolean {
        return try {
            val formato = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault())
            val fecha = formato.parse(fechaVencimiento)
            fecha != null && fecha.before(java.util.Date()) && idEstadoCompromiso != 3
        } catch (e: Exception) { false }
    }

    fun recargoAtraso(): Int {
        return if (estaVencido()) (monto * 0.05).toInt().coerceAtLeast(500) else 0
    }

    fun totalAPagar(): Int = monto + recargoAtraso()
}