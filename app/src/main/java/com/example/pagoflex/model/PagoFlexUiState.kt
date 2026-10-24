package com.example.pagoflex.model
data class PagoFlexUiState(
    val correoLogin: String = "",
    val errorCorreoLogin: String? = null,
    val contrasenaLogin: String = "",
    val errorContrasenaLogin: String? = null,
    val nombreLogin: String = "",
    val errorNombreLogin: String? = null,
    val rolSeleccionado: RolUsuario? = null,
    val errorRolLogin: String? = null,
    val sesionIniciada: Boolean = false,

    val idUsuarioLogueado: Int = 0,
    val perfilNombre: String = "",
    val perfilCorreo: String = "",
    val perfilComuna: String = "",

    val nombreRegistro: String = "",
    val errorNombreRegistro: String? = null,
    val correoRegistro: String = "",
    val errorCorreoRegistro: String? = null,
    val contrasenaRegistro: String = "",
    val errorContrasenaRegistro: String? = null,
    val registroUsuarioExitoso: Boolean = false,

    val compromisos: List<Compromiso> = emptyList(),

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
    val recomendacionExitosa: Boolean = false,

    val compromisoSeleccionadoParaPago: Compromiso? = null,

    val numeroTarjeta: String = "",
    val errorNumeroTarjeta: String? = null,
    val nombreTitularTarjeta: String = "",
    val errorNombreTitularTarjeta: String? = null,
    val cvvTarjeta: String = "",
    val errorCvvTarjeta: String? = null,
    val fechaExpiracionTarjeta: String = "",
    val errorFechaExpiracionTarjeta: String? = null,
    val pagoTarjetaExitoso: Boolean = false,

    val comprobanteFecha: String = "",
    val comprobanteMonto: Int = 0,
    val comprobanteFolio: String = "",
    val mostrarComprobante: Boolean = false,

    val reportesAgente: List<ReporteProblema> = emptyList(),
    val respuestaReporte: String = ""
)
