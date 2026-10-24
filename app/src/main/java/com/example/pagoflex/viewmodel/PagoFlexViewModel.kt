package com.example.pagoflex.viewmodel

import androidx.lifecycle.ViewModel
import android.app.Application
import android.content.ContentValues
import androidx.lifecycle.AndroidViewModel
import com.example.pagoflex.data.PagoFlexDbHelper
import com.example.pagoflex.data.repository.ReporteGestionRepository
import com.example.pagoflex.data.repository.RecomendacionGestionRepository
import com.example.pagoflex.data.repository.CompromisoRepository
import com.example.pagoflex.data.repository.RecomendacionRepository
import com.example.pagoflex.data.repository.ReporteRepository
import com.example.pagoflex.data.repository.UsuarioRepository
import com.example.pagoflex.model.ReporteProblema
import com.example.pagoflex.model.Compromiso
import com.example.pagoflex.model.PagoFlexUiState
import com.example.pagoflex.model.RolUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PagoFlexViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = PagoFlexDbHelper(application)
    private val compromisoRepository = CompromisoRepository(dbHelper)
    private val reporteRepository = ReporteRepository(dbHelper)
    private val recomendacionRepository = RecomendacionRepository(dbHelper)
    private val usuarioRepository = UsuarioRepository(dbHelper)
    private val reporteGestionRepository = ReporteGestionRepository(dbHelper)
    private val recomendacionGestionRepository = RecomendacionGestionRepository(dbHelper)
    private val _uiState = MutableStateFlow(PagoFlexUiState())
    val uiState: StateFlow<PagoFlexUiState> = _uiState.asStateFlow()

    fun onNombreLoginCambia(nombre: String) {
        val error = if (nombre.any { it.isDigit() }) "El nombre no puede contener números." else null
        _uiState.update { it.copy(nombreLogin = nombre, errorNombreLogin = error) }
    }

    fun onCorreoLoginCambia(correo: String) {
        _uiState.update { it.copy(correoLogin = correo, errorCorreoLogin = null) }
    }

    fun onContrasenaLoginCambia(contrasena: String) {
        _uiState.update { it.copy(contrasenaLogin = contrasena, errorContrasenaLogin = null) }
    }

    fun onRolCambia(rol: RolUsuario) {
        _uiState.update { it.copy(rolSeleccionado = rol, errorRolLogin = null) }
    }

    fun onIntentarLogin(): Boolean {
        val estado = _uiState.value
        val errorCorreo = if (estado.correoLogin.isBlank() || !estado.correoLogin.contains("@")) "Ingresa un correo v�lido" else null
        val errorContrasena = if (estado.contrasenaLogin.isBlank()) "La contrase�a es obligatoria" else null

        if (errorCorreo != null || errorContrasena != null) {
            _uiState.update { it.copy(errorCorreoLogin = errorCorreo, errorContrasenaLogin = errorContrasena) }
            return false
        }

        val usuario = usuarioRepository.login(estado.correoLogin, estado.contrasenaLogin)
        if (usuario == null) {
            _uiState.update { it.copy(errorContrasenaLogin = "Correo o contrase�a incorrectos") }
            return false
        }

        val rol = when (usuario.idRol) {
            2 -> RolUsuario.AGENTE
            3 -> RolUsuario.EJECUTIVO
            else -> RolUsuario.USUARIO_FINAL
        }

        _uiState.update {
            it.copy(
                sesionIniciada = true,
                idUsuarioLogueado = usuario.id,
                nombreLogin = usuario.nombre,
                perfilNombre = usuario.nombre,
                perfilCorreo = usuario.correo,
                rolSeleccionado = rol,
                compromisos = compromisoRepository.obtenerTodos()
            )
        }
        return true
    }

    // --- REGISTRO DE USUARIO ---
    fun onNombreRegistroCambia(nombre: String) {
        val error = if (nombre.any { it.isDigit() }) "El nombre no puede contener números." else null
        _uiState.update { it.copy(nombreRegistro = nombre, errorNombreRegistro = error) }
    }

    fun onCorreoRegistroCambia(correo: String) {
        _uiState.update { it.copy(correoRegistro = correo, errorCorreoRegistro = null) }
    }

    fun onContrasenaRegistroCambia(contrasena: String) {
        _uiState.update { it.copy(contrasenaRegistro = contrasena, errorContrasenaRegistro = null) }
    }

    fun onRegistrarUsuario(): Boolean {
        val estado = _uiState.value
        val regexSoloLetras = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")
        val errorNombre = when {
            estado.nombreRegistro.isBlank() -> "El nombre es obligatorio"
            estado.nombreRegistro.trim().length < 2 -> "El nombre debe tener al menos 2 caracteres."
            estado.nombreRegistro.any { it.isDigit() } -> "El nombre no puede contener números."
            !estado.nombreRegistro.matches(regexSoloLetras) -> "El nombre solo puede contener letras."
            else -> null
        }
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
                compromisos = compromisoRepository.obtenerTodos(),
                registroUsuarioExitoso = true
            )
        }
        return true
    }

    fun cerrarSesion() {
        _uiState.update { PagoFlexUiState() }
    }

    fun onPagarCompromiso(compromiso: Compromiso) {
        compromisoRepository.marcarPagado(compromiso.idCompromiso)
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
            reporteRepository.insertar(estado.motivoReporteSeleccionado, estado.comentarioReporte)

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
            compromisoRepository.insertar(nuevoCompromiso)

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
        val error = if (valor.any { it.isDigit() }) "El nombre no puede contener números." else null
        _uiState.update { it.copy(nombreEntidadRecomendada = valor, errorNombreEntidad = error) }
    }

    fun onRubroEntidadCambia(valor: String) {
        _uiState.update { it.copy(rubroEntidadRecomendada = valor, errorRubroEntidad = null) }
    }

    fun onComunaEntidadCambia(valor: String) {
        _uiState.update { it.copy(comunaEntidadRecomendada = valor, errorComunaEntidad = null) }
    }

    fun onEnviarRecomendacion() {
        val estado = _uiState.value
        val regexSoloLetras = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")
        val errorNombre = when {
            estado.nombreEntidadRecomendada.isBlank() -> "El nombre es obligatorio"
            estado.nombreEntidadRecomendada.any { it.isDigit() } -> "El nombre no puede contener números."
            !estado.nombreEntidadRecomendada.matches(regexSoloLetras) -> "El nombre solo puede contener letras."
            else -> null
        }
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
            recomendacionRepository.insertar(
                estado.nombreEntidadRecomendada,
                estado.rubroEntidadRecomendada,
                estado.comunaEntidadRecomendada
            )

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

    fun onSeleccionarCompromisoParaPago(compromiso: Compromiso) {
        _uiState.update {
            it.copy(
                compromisoSeleccionadoParaPago = compromiso,
                numeroTarjeta = "",
                errorNumeroTarjeta = null,
                nombreTitularTarjeta = "",
                errorNombreTitularTarjeta = null,
                cvvTarjeta = "",
                errorCvvTarjeta = null,
                fechaExpiracionTarjeta = "",
                errorFechaExpiracionTarjeta = null,
                pagoTarjetaExitoso = false
            )
        }
    }

    fun onNumeroTarjetaCambia(valor: String) {
        val soloDigitos = valor.filter { it.isDigit() }.take(16)
        val error = if (soloDigitos.any { !it.isDigit() }) "El número de tarjeta solo puede contener dígitos." else null
        _uiState.update { it.copy(numeroTarjeta = soloDigitos, errorNumeroTarjeta = error) }
    }

    fun onNombreTitularTarjetaCambia(valor: String) {
        val error = if (valor.any { it.isDigit() }) "El nombre del titular no puede contener números." else null
        _uiState.update { it.copy(nombreTitularTarjeta = valor, errorNombreTitularTarjeta = error) }
    }

    fun onCvvTarjetaCambia(valor: String) {
        val soloDigitos = valor.filter { it.isDigit() }.take(4)
        _uiState.update { it.copy(cvvTarjeta = soloDigitos, errorCvvTarjeta = null) }
    }

    fun onFechaExpiracionTarjetaCambia(valor: String) {
        val soloDigitos = valor.filter { it.isDigit() }.take(4)
        val formateado = if (soloDigitos.length <= 2) {
            soloDigitos
        } else {
            "${soloDigitos.substring(0, 2)}/${soloDigitos.substring(2)}"
        }
        _uiState.update { it.copy(fechaExpiracionTarjeta = formateado, errorFechaExpiracionTarjeta = null) }
    }

    fun onRealizarPagoTarjeta(): Boolean {
        val estado = _uiState.value
        val compromiso = estado.compromisoSeleccionadoParaPago

        val errorNumero = when {
            estado.numeroTarjeta.isBlank() -> "El número de tarjeta es obligatorio."
            estado.numeroTarjeta.length != 16 -> "El número de tarjeta debe tener 16 dígitos."
            !estado.numeroTarjeta.all { it.isDigit() } -> "El número de tarjeta solo puede contener dígitos."
            else -> null
        }

        val errorNombre = when {
            estado.nombreTitularTarjeta.isBlank() -> "El nombre del titular es obligatorio."
            estado.nombreTitularTarjeta.any { it.isDigit() } -> "El nombre del titular no puede contener números."
            else -> null
        }

        val errorCvv = when {
            estado.cvvTarjeta.isBlank() -> "El CVV es obligatorio."
            estado.cvvTarjeta.length !in 3..4 -> "El CVV debe tener 3 o 4 dígitos."
            !estado.cvvTarjeta.all { it.isDigit() } -> "El CVV solo puede contener dígitos."
            else -> null
        }

        val errorFecha = when {
            !estado.fechaExpiracionTarjeta.matches(Regex("^\\d{2}/\\d{2}$")) ->
                "Ingresa una fecha de expiración válida (MM/AA)."
            else -> {
                val mes = estado.fechaExpiracionTarjeta.substring(0, 2).toIntOrNull()
                val anio = estado.fechaExpiracionTarjeta.substring(3, 5).toIntOrNull()
                when {
                    mes == null || anio == null || mes !in 1..12 ->
                        "Ingresa una fecha de expiración válida (MM/AA)."
                    else -> {
                        val calendario = java.util.Calendar.getInstance()
                        val anioActual = calendario.get(java.util.Calendar.YEAR) % 100
                        val mesActual = calendario.get(java.util.Calendar.MONTH) + 1
                        if (anio < anioActual || (anio == anioActual && mes < mesActual)) {
                            "La tarjeta está vencida."
                        } else null
                    }
                }
            }
        }

        if (errorNumero != null || errorNombre != null || errorCvv != null || errorFecha != null) {
            _uiState.update {
                it.copy(
                    errorNumeroTarjeta = errorNumero,
                    errorNombreTitularTarjeta = errorNombre,
                    errorCvvTarjeta = errorCvv,
                    errorFechaExpiracionTarjeta = errorFecha,
                    pagoTarjetaExitoso = false
                )
            }
            return false
        }

        if (compromiso != null) {
            onPagarCompromiso(compromiso)
            compromisoRepository.insertarComprobante(compromiso.idCompromiso, compromiso.monto, compromiso.folio)
        }

        _uiState.update {
            it.copy(
                errorNumeroTarjeta = null,
                errorNombreTitularTarjeta = null,
                errorCvvTarjeta = null,
                errorFechaExpiracionTarjeta = null,
                pagoTarjetaExitoso = true,
                comprobanteFecha = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault()).format(java.util.Date()),
                comprobanteMonto = compromiso?.monto ?: 0,
                comprobanteFolio = compromiso?.folio ?: "",
                mostrarComprobante = true,
                numeroTarjeta = "",
                nombreTitularTarjeta = "",
                cvvTarjeta = "",
                fechaExpiracionTarjeta = ""
            )
        }
        return true
    }

    fun limpiarPagoTarjeta() {
        _uiState.update {
            it.copy(
                compromisoSeleccionadoParaPago = null,
                numeroTarjeta = "",
                errorNumeroTarjeta = null,
                nombreTitularTarjeta = "",
                errorNombreTitularTarjeta = null,
                cvvTarjeta = "",
                errorCvvTarjeta = null,
                fechaExpiracionTarjeta = "",
                errorFechaExpiracionTarjeta = null,
                pagoTarjetaExitoso = false
            )
        }
    }


    fun onPerfilNombreCambia(valor: String) {
        _uiState.update { it.copy(perfilNombre = valor) }
    }

    fun onPerfilCorreoCambia(valor: String) {
        _uiState.update { it.copy(perfilCorreo = valor) }
    }

    fun onPerfilComunaCambia(valor: String) {
        _uiState.update { it.copy(perfilComuna = valor) }
    }

    fun onGuardarPerfil() {
        val estado = _uiState.value
        if (estado.idUsuarioLogueado != 0) {
            usuarioRepository.actualizarPerfil(estado.idUsuarioLogueado, estado.perfilNombre, estado.perfilCorreo, estado.perfilComuna)
        }
        _uiState.update { it.copy(nombreLogin = estado.perfilNombre, perfilCorreo = estado.perfilCorreo) }
    }
    fun cargarReportesAgente() {
        val reportes = reporteGestionRepository.obtenerTodos().map { r ->
            ReporteProblema(
                id = r.id,
                idUsuario = r.idUsuario,
                motivo = r.motivo,
                comentario = r.comentario,
                fecha = r.fecha,
                estado = r.estado,
                respuesta = r.respuesta,
                fechaGestion = r.fechaGestion
            )
        }
        _uiState.update { it.copy(reportesAgente = reportes) }
    }

    fun onRespuestaReporteCambia(valor: String) {
        _uiState.update { it.copy(respuestaReporte = valor) }
    }

    fun tomarReporte(id: Int) {
        reporteGestionRepository.tomar(id)
        cargarReportesAgente()
    }

    fun responderReporte(id: Int) {
        val respuesta = _uiState.value.respuestaReporte
        reporteGestionRepository.responder(id, respuesta)
        _uiState.update { it.copy(respuestaReporte = "") }
        cargarReportesAgente()
    }

    fun resolverReporte(id: Int) {
        val respuesta = _uiState.value.respuestaReporte
        reporteGestionRepository.resolver(id, respuesta.ifBlank { "Resuelto" })
        _uiState.update { it.copy(respuestaReporte = "") }
        cargarReportesAgente()
    }

    fun resumenCobranzaEmpresa(idEmpresa: Int): Triple<Int, Int, Int> {
        val compromisos = compromisoRepository.obtenerTodos().filter { it.idEmpresa == idEmpresa }
        val pagados = compromisos.count { it.idEstadoCompromiso == 3 }
        val pendientes = compromisos.count { it.idEstadoCompromiso != 3 }
        val vencidos = compromisos.count { it.idEstadoCompromiso != 3 && esVencido(it.fechaVencimiento) }
        return Triple(pagados, pendientes, vencidos)
    }

    private fun esVencido(fecha: String): Boolean {
        return try {
            val formato = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault())
            val venc = formato.parse(fecha)
            venc != null && venc.before(java.util.Date())
        } catch (e: Exception) {
            false
        }
    }}



