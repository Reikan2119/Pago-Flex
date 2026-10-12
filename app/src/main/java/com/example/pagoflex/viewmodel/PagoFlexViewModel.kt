package com.example.pagoflex.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pagoflex.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PagoFlexViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PagoFlexUiState())
    val uiState: StateFlow<PagoFlexUiState> = _uiState.asStateFlow()

    init {
        cargarDatosSimulados()
    }

    fun onNombreLoginCambia(nombre: String) {
        _uiState.update { it.copy(nombreLogin = nombre, errorNombreLogin = null) }
    }

    fun onRolCambia(rol: RolUsuario) {
        _uiState.update { it.copy(rolSeleccionado = rol, errorRolLogin = null) }
    }

    fun onIntentarLogin(): Boolean {
        val estado = _uiState.value
        val regexSoloLetras = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")

        val errorNombre = when {
            estado.nombreLogin.isBlank() -> "El nombre es obligatorio."
            estado.nombreLogin.trim().length < 2 -> "El nombre debe tener al menos 2 caracteres."
            !estado.nombreLogin.matches(regexSoloLetras) -> "El nombre solo puede contener letras."
            else -> null
        }

        val errorRol = if (estado.rolSeleccionado == null) "Debes seleccionar un perfil." else null

        if (errorNombre != null || errorRol != null) {
            _uiState.update {
                it.copy(errorNombreLogin = errorNombre, errorRolLogin = errorRol)
            }
            return false
        }

        _uiState.update {
            it.copy(
                sesionIniciada = true,
                compromisos = obtenerCompromisosIniciales()
            )
        }
        return true
    }

    private fun obtenerCompromisosIniciales(): List<Compromiso> {
        return listOf(
            Compromiso(1, "CP-2026-001", 1, 1, "Cuota GC - Altos del Maipo", "1/12", 85000, "15-11-2026", 1),
            Compromiso(2, "CP-2026-002", 1, 6, "Mensualidad - Club Los Halcones", "1/1", 45000, "20-11-2026", 1),
            Compromiso(3, "CP-2026-003", 1, 8, "Arriendo Depo - Plaza Oriente", "3/12", 120000, "05-12-2026", 1)
        )
    }

    fun onMedioPagoCambia(medio: MedioPago) {
        _uiState.update { it.copy(medioPagoSeleccionado = medio, errorPago = null, pagoExitoso = false) }
    }

    fun onPagarCompromiso(compromisoAPagar: Compromiso) {
        val estado = _uiState.value


        val compromisosMismaEmpresa = estado.compromisos.filter {
            it.idEmpresa == compromisoAPagar.idEmpresa && (it.idEstadoCompromiso == 1 || it.idEstadoCompromiso == 2)
        }

        val compromisoMasAntiguo = compromisosMismaEmpresa.minByOrNull { it.idCompromiso }

        val errorPago = if (compromisoAPagar.idEstadoCompromiso == 4 || compromisoAPagar.idEstadoCompromiso == 5) {
            "No puedes pagar un compromiso en revisión o anulado." // RN-08 y RN-09
        } else if (compromisoMasAntiguo != null && compromisoMasAntiguo.idCompromiso != compromisoAPagar.idCompromiso) {
            "Debes pagar tu compromiso más antiguo primero." // RN-04
        } else {
            null
        }

        if (errorPago != null) {
            _uiState.update { it.copy(errorPago = errorPago, pagoExitoso = false) }
        } else {
            val nuevaLista = estado.compromisos.map {
                if (it.idCompromiso == compromisoAPagar.idCompromiso) {
                    it.copy(idEstadoCompromiso = 3)
                } else {
                    it
                }
            }

            _uiState.update {
                it.copy(
                    compromisos = nuevaLista,
                    pagoExitoso = true,
                    errorPago = null
                )
            }
        }
    }

    fun onLimpiarPago() {
        _uiState.update { it.copy(errorPago = null, pagoExitoso = false, medioPagoSeleccionado = MedioPago.SIMULADO) }
    }

    fun onMotivoReporteCambia(motivo: String) {
        _uiState.update { it.copy(motivoReporteSeleccionado = motivo, errorComentarioReporte = null) }
    }

    fun onComentarioReporteCambia(comentario: String) {
        _uiState.update { it.copy(comentarioReporte = comentario, errorComentarioReporte = null) }
    }

    fun onEnviarReporte() {
        val estado = _uiState.value

        val error = if (estado.motivoReporteSeleccionado == "Otro" && estado.comentarioReporte.isBlank()) {
            "El comentario es obligatorio cuando el motivo es 'Otro'"
        } else {
            null
        }

        if (error != null) {
            _uiState.update { it.copy(errorComentarioReporte = error, reporteExitoso = false) }
        } else {
            _uiState.update {
                it.copy(
                    errorComentarioReporte = null,
                    reporteExitoso = true,
                    comentarioReporte = "",
                    motivoReporteSeleccionado = "Pago no reflejado"
                )
            }
        }
    }

    fun onLimpiarReporte() {
        _uiState.update {
            it.copy(
                comentarioReporte = "",
                errorComentarioReporte = null,
                reporteExitoso = false
            )
        }
    }

    fun onNombreEntidadCambia(valor: String) {
        _uiState.update { it.copy(nombreEntidadRecomendada = valor, errorNombreEntidad = null) }
    }

    fun onRubroEntidadCambia(valor: String) {
        _uiState.update { it.copy(rubroEntidadRecomendada = valor, errorRubroEntidad = null) }
    }

    fun onComunaEntidadCambia(valor: String) {
        _uiState.update { it.copy(comunaEntidadRecomendada = valor, errorComunaEntidad = null) }
    }

    fun onEnviarRecomendacion() {
        val estado = _uiState.value

        val errorNombre = if (estado.nombreEntidadRecomendada.isBlank()) "El nombre de la entidad es obligatorio" else null
        val errorRubro = if (estado.rubroEntidadRecomendada.isBlank()) "El rubro es obligatorio" else null
        val errorComuna = if (estado.comunaEntidadRecomendada.isBlank()) "La comuna es obligatoria" else null

        if (errorNombre != null || errorRubro != null || errorComuna != null) {
            _uiState.update {
                it.copy(
                    errorNombreEntidad = errorNombre,
                    errorRubroEntidad = errorRubro,
                    errorComunaEntidad = errorComuna,
                    recomendacionExitosa = false
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    nombreEntidadRecomendada = "",
                    rubroEntidadRecomendada = "",
                    comunaEntidadRecomendada = "",
                    errorNombreEntidad = null,
                    errorRubroEntidad = null,
                    errorComunaEntidad = null,
                    recomendacionExitosa = true
                )
            }
        }
    }

    fun onRutClienteCompromisoCambia(valor: String) {
        _uiState.update { it.copy(rutClienteCompromiso = valor, errorRutClienteCompromiso = null) }
    }

    fun onConceptoCompromisoCambia(valor: String) {
        _uiState.update { it.copy(conceptoCompromiso = valor, errorConceptoCompromiso = null) }
    }

    fun onMontoCompromisoCambia(valor: String) {
        _uiState.update { it.copy(montoCompromiso = valor, errorMontoCompromiso = null) }
    }

    fun onFechaVencimientoCompromisoCambia(valor: String) {
        _uiState.update { it.copy(fechaVencimientoCompromiso = valor, errorFechaVencimientoCompromiso = null) }
    }

    fun onRegistrarCompromiso() {
        val estado = _uiState.value

        val errorRut = if (estado.rutClienteCompromiso.isBlank()) "El RUT del cliente es obligatorio" else null
        val errorConcepto = if (estado.conceptoCompromiso.isBlank()) "El concepto es obligatorio" else null

        val montoEntero = estado.montoCompromiso.toIntOrNull()
        val errorMonto = when {
            estado.montoCompromiso.isBlank() -> "El monto es obligatorio"
            montoEntero == null || montoEntero <= 0 -> "El monto debe ser mayor a \$0 "
            else -> null
        }

        val errorFecha = if (estado.fechaVencimientoCompromiso.isBlank()) "La fecha de vencimiento es obligatoria" else null

        if (errorRut != null || errorConcepto != null || errorMonto != null || errorFecha != null) {
            _uiState.update {
                it.copy(
                    errorRutClienteCompromiso = errorRut,
                    errorConceptoCompromiso = errorConcepto,
                    errorMontoCompromiso = errorMonto,
                    errorFechaVencimientoCompromiso = errorFecha,
                    registroCompromisoExitoso = false
                )
            }
        } else {
            val nuevoCompromiso = Compromiso(
                idCompromiso = (100..999).random(),
                folio = "CP-2026-${(1000..9999).random()}",
                idUsuarioFinal = 1,
                idEmpresa = 1,
                concepto = estado.conceptoCompromiso,
                cuota = "1/1",
                monto = montoEntero!!,
                fechaVencimiento = estado.fechaVencimientoCompromiso,
                idEstadoCompromiso = 1
            )

            _uiState.update {
                it.copy(
                    compromisos = it.compromisos + nuevoCompromiso,
                    rutClienteCompromiso = "",
                    conceptoCompromiso = "",
                    montoCompromiso = "",
                    fechaVencimientoCompromiso = "",
                    errorRutClienteCompromiso = null,
                    errorConceptoCompromiso = null,
                    errorMontoCompromiso = null,
                    errorFechaVencimientoCompromiso = null,
                    registroCompromisoExitoso = true
                )
            }
        }
    }

    private fun cargarDatosSimulados() {
        val empresasFicticias = listOf(
            EmpresaCliente(1, "EC-01", "Altos del Maipo", 8500),
            EmpresaCliente(6, "EC-06", "Club Los Halcones", 2000),
            EmpresaCliente(8, "EC-08", "Plaza Oriente", 7500)
        )

        val compromisosFicticios = listOf(
            Compromiso(101, "CP-2026-0101", 1, 8, "Arriendo departamento octubre", "10/12", 520000, "10-10-2026", 1),
            Compromiso(102, "CP-2026-0102", 1, 6, "Cuota social octubre", "10/12", 15000, "05-10-2026", 2)
        )

        _uiState.update {
            it.copy(
                empresas = empresasFicticias,
                compromisos = compromisosFicticios,
                sesionIniciada = true
            )
        }
    }
}