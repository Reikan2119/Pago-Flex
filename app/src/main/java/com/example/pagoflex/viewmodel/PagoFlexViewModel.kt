package com.example.pagoflex.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pagoflex.model.Compromiso
import com.example.pagoflex.model.PagoFlexUiState
import com.example.pagoflex.model.RolUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PagoFlexViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PagoFlexUiState())
    val uiState: StateFlow<PagoFlexUiState> = _uiState.asStateFlow()

    fun onNombreLoginCambia(nombre: String) {
        val regexSoloLetrasOVacio = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*$")
        if (nombre.matches(regexSoloLetrasOVacio)) {
            _uiState.update { it.copy(nombreLogin = nombre, errorNombreLogin = null) }
        }
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
            _uiState.update { it.copy(errorNombreLogin = errorNombre, errorRolLogin = errorRol) }
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

    // --- REGISTRO DE USUARIO ---
    fun onNombreRegistroCambia(nombre: String) {
        _uiState.update { it.copy(nombreRegistro = nombre, errorNombreRegistro = null) }
    }

    fun onCorreoRegistroCambia(correo: String) {
        _uiState.update { it.copy(correoRegistro = correo, errorCorreoRegistro = null) }
    }

    fun onContrasenaRegistroCambia(contrasena: String) {
        _uiState.update { it.copy(contrasenaRegistro = contrasena, errorContrasenaRegistro = null) }
    }

    fun onRegistrarUsuario(): Boolean {
        val estado = _uiState.value
        val errorNombre = if (estado.nombreRegistro.isBlank()) "El nombre es obligatorio" else null
        val errorCorreo = if (estado.correoRegistro.isBlank() || !estado.correoRegistro.contains("@")) "Ingresa un correo válido" else null
        val errorContrasena = if (estado.contrasenaRegistro.length < 6) "Mínimo 6 caracteres" else null

        if (errorNombre != null || errorCorreo != null || errorContrasena != null) {
            _uiState.update {
                it.copy(
                    errorNombreRegistro = errorNombre,
                    errorCorreoRegistro = errorCorreo,
                    errorContrasenaRegistro = errorContrasena,
                    registroUsuarioExitoso = false
                )
            }
            return false
        }

        _uiState.update {
            it.copy(
                nombreLogin = estado.nombreRegistro,
                rolSeleccionado = RolUsuario.USUARIO_FINAL,
                sesionIniciada = true,
                compromisos = obtenerCompromisosIniciales(),
                registroUsuarioExitoso = true
            )
        }
        return true
    }

    fun cerrarSesion() {
        _uiState.update { PagoFlexUiState() }
    }

    fun onPagarCompromiso(compromiso: Compromiso) {
        _uiState.update { estadoActual ->
            val listaActualizada = estadoActual.compromisos.map {
                if (it.idCompromiso == compromiso.idCompromiso) {
                    it.copy(idEstadoCompromiso = 3)
                } else {
                    it
                }
            }
            estadoActual.copy(compromisos = listaActualizada)
        }
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
            "El comentario es obligatorio cuando el motivo es 'Otro' "
        } else null

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
    fun onRutClienteCompromisoCambia(valor: String) {
        _uiState.update { it.copy(rutClienteCompromiso = valor, errorRutClienteCompromiso = null) }
    }

    fun onConceptoCompromisoCambia(valor: String) {
        _uiState.update { it.copy(conceptoCompromiso = valor, errorConceptoCompromiso = null) }
    }

    fun onMontoCompromisoCambia(valor: String) {
        if (valor.matches(Regex("^[0-9]*$"))) {
            _uiState.update { it.copy(montoCompromiso = valor, errorMontoCompromiso = null) }
        }
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
        val errorFecha = if (estado.fechaVencimientoCompromiso.isBlank()) "La fecha es obligatoria" else null

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
                    registroCompromisoExitoso = true
                )
            }
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
        val errorNombre = if (estado.nombreEntidadRecomendada.isBlank()) "El nombre es obligatorio" else null
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
                    recomendacionExitosa = true
                )
            }
        }
    }

    private fun obtenerCompromisosIniciales(): List<Compromiso> {
        return listOf(
            Compromiso(1, "CP-2026-001", 1, 1, "Cuota GC - Altos del Maipo", "1/12", 85000, "15-11-2026", 1),
            Compromiso(2, "CP-2026-002", 1, 6, "Mensualidad - Club Los Halcones", "1/1", 45000, "20-11-2026", 1),
            Compromiso(3, "CP-2026-003", 1, 8, "Arriendo Depo - Plaza Oriente", "3/12", 120000, "05-12-2026", 1)
        )
    }
}