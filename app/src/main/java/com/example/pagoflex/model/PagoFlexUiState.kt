package com.example.pagoflex.model

enum class RolUsuario(val etiqueta: String) {
    USUARIO_FINAL("Usuario Final"),
    AGENTE("Agente PagoFlex"),
    EJECUTIVO("Ejecutivo de Empresa")
}

enum class MedioPago(val etiqueta: String) {
    DEBITO("Tarjeta de Débito"),
    CREDITO("Tarjeta de Crédito"),
    TRANSFERENCIA("Transferencia"),
    SIMULADO("Pago Simulado")
}

data class PagoFlexUiState(
    val nombreLogin: String = "",
    val errorNombreLogin: String? = null,
    val rolSeleccionado: RolUsuario? = null,
    val errorRolLogin: String? = null,

    val sesionIniciada: Boolean = false,
    val usuarioActual: Usuario? = null,

    val compromisos: List<Compromiso> = emptyList(),
    val empresas: List<EmpresaCliente> = emptyList(),

    val medioPagoSeleccionado: MedioPago = MedioPago.SIMULADO,
    val errorPago: String? = null,
    val pagoExitoso: Boolean = false,

    val motivoReporteSeleccionado: String = "Pago no reflejado",
    val comentarioReporte: String = "",
    val errorComentarioReporte: String? = null,
    val reporteExitoso: Boolean = false,

    val rutClienteCompromiso: String = "",
    val errorRutClienteCompromiso: String? = null,
    val conceptoCompromiso: String = "",
    val errorConceptoCompromiso: String? = null,
    val montoCompromiso: String = "",
    val errorMontoCompromiso: String? = null,
    val fechaVencimientoCompromiso: String = "",
    val errorFechaVencimientoCompromiso: String? = null,
    val registroCompromisoExitoso: Boolean = false,

    val nombreEntidadRecomendada: String = "",
    val errorNombreEntidad: String? = null,
    val rubroEntidadRecomendada: String = "",
    val errorRubroEntidad: String? = null,
    val comunaEntidadRecomendada: String = "",
    val errorComunaEntidad: String? = null,
    val recomendacionExitosa: Boolean = false
)