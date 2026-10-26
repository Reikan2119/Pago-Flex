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
import com.example.pagoflex.model.Recomendacion
import com.example.pagoflex.model.ReporteProblema
import com.example.pagoflex.model.Cliente
import com.example.pagoflex.model.Compromiso
import com.example.pagoflex.model.EntidadSugerida
import com.example.pagoflex.model.PagoFlexUiState
import com.example.pagoflex.model.RolUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PagoFlexViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(PagoFlexUiState())
    init {
        _uiState.update {
            it.copy(
                entidadesDisponibles = listOf(
                    EntidadSugerida("Municipalidad de Santiago", "Municipalidad", "Santiago"),
                    EntidadSugerida("Club Deportivo Universidad", "Club", "Providencia"),
                    EntidadSugerida("Colegio San Ignacio", "Educaci�n", "Las Condes"),
                    EntidadSugerida("Inmobiliaria Altos del Maipo", "Inmobiliaria", "Puente Alto")
                )
            )
        }
    }

    private val dbHelper = PagoFlexDbHelper(application)
    private val usuarioRepository = UsuarioRepository(dbHelper)
    private val compromisoRepository = CompromisoRepository(dbHelper)
    private val reporteRepository = ReporteRepository(dbHelper)
    private val recomendacionRepository = RecomendacionRepository(dbHelper)
    private val reporteGestionRepository = ReporteGestionRepository(dbHelper)
    private val recomendacionGestionRepository = RecomendacionGestionRepository(dbHelper)
    val uiState: StateFlow<PagoFlexUiState> = _uiState.asStateFlow()

    init { cargarClientes() }

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
            val idCliente = estado.clienteSeleccionado?.id
                ?: usuarioRepository.obtenerUsuariosFinales().firstOrNull { it.rut == estado.rutClienteCompromiso }?.id
                ?: 1

            val nuevoCompromiso = Compromiso(
                idCompromiso = (100..999).random(),
                folio = "CP-2026-${(1000..9999).random()}",
                idUsuarioFinal = idCliente,
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
            compromisoRepository.insertarComprobante(compromiso.idCompromiso, compromiso.totalAPagar(), compromiso.folio)
        }

        _uiState.update {
            it.copy(
                errorNumeroTarjeta = null,
                errorNombreTitularTarjeta = null,
                errorCvvTarjeta = null,
                errorFechaExpiracionTarjeta = null,
                pagoTarjetaExitoso = true,
                comprobanteFecha = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault()).format(java.util.Date()),
                comprobanteMonto = compromiso?.totalAPagar() ?: 0,
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


    fun onPerfilRutCambia(valor: String) {
        _uiState.update { it.copy(perfilRut = valor) }
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
            usuarioRepository.actualizarPerfil(estado.idUsuarioLogueado, estado.perfilNombre, estado.perfilCorreo, estado.perfilComuna, estado.perfilRut)
        }
        _uiState.update { it.copy(nombreLogin = estado.perfilNombre, perfilCorreo = estado.perfilCorreo, perfilRut = estado.perfilRut, perfilComuna = estado.perfilComuna, perfilGuardadoExitoso = true) }
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
    }
    fun cargarRecomendacionesAgente() {
        val recs = recomendacionGestionRepository.obtenerTodas().map { (id, texto) ->
            val partes = texto.split(" - ")
            Recomendacion(id = id, nombre = partes.getOrElse(0) { "" }, estado = partes.getOrElse(1) { "Pendiente" })
        }
        _uiState.update { it.copy(recomendacionesAgente = recs) }
    }

    fun cambiarEstadoRecomendacion(id: Int, estado: String) {
        recomendacionGestionRepository.cambiarEstado(id, estado)
        cargarRecomendacionesAgente()
    }
    fun onMostrarListaEntidades(mostrar: Boolean) {
        _uiState.update { it.copy(mostrarListaEntidades = mostrar) }
    }

    fun onSeleccionarEntidad(entidad: EntidadSugerida) {
        _uiState.update {
            it.copy(
                nombreEntidadRecomendada = entidad.nombre,
                rubroEntidadRecomendada = entidad.rubro,
                comunaEntidadRecomendada = entidad.comuna,
                mostrarListaEntidades = false
            )
        }
}

    fun onPagarCompromiso(compromiso: Compromiso) {
        compromisoRepository.marcarPagado(compromiso.idCompromiso)
        _uiState.update { estadoActual ->
            val listaActualizada = estadoActual.compromisos.map {
                if (it.idCompromiso == compromiso.idCompromiso) it.copy(idEstadoCompromiso = 3) else it
            }
            estadoActual.copy(compromisos = listaActualizada)
        }
    }
    fun iniciarSesion(id: Int, nombre: String, correo: String, rut: String?, comuna: String?, rol: RolUsuario) {
        _uiState.update {
            it.copy(
                sesionIniciada = true,
                idUsuarioLogueado = id,
                nombreLogin = nombre,
                perfilNombre = nombre,
                perfilCorreo = correo,
                perfilRut = rut ?: "",
                perfilComuna = comuna ?: "",
                rolSeleccionado = rol,
                compromisos = compromisoRepository.obtenerPorUsuario(id)
            )
        }
    }
    fun cerrarSesion() {
        _uiState.value = PagoFlexUiState()
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
            reporteRepository.insertar(estado.motivoReporteSeleccionado, estado.comentarioReporte, if (estado.idUsuarioLogueado != 0) estado.idUsuarioLogueado else 1)
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
    fun cargarClientes() {
        val clientes = usuarioRepository.obtenerUsuariosFinales().map { u ->
            Cliente(id = u.id, nombre = u.nombre, rut = u.rut ?: "")
        }
        _uiState.update { it.copy(clientesDisponibles = clientes) }
    }

    fun onMostrarListaClientes(mostrar: Boolean) {
        _uiState.update { it.copy(mostrarListaClientes = mostrar) }
    }

    fun onSeleccionarCliente(cliente: Cliente) {
        _uiState.update { it.copy(clienteSeleccionado = cliente, mostrarListaClientes = false, confirmarCliente = true) }
    }

    fun onConfirmarCliente() {
        val c = _uiState.value.clienteSeleccionado
        if (c != null) {
            _uiState.update { it.copy(rutClienteCompromiso = c.rut, confirmarCliente = false) }
        }
    }

    fun onCancelarCliente() {
        _uiState.update { it.copy(clienteSeleccionado = null, confirmarCliente = false) }
    }
    fun limpiarMensajePerfil() {
        _uiState.update { it.copy(perfilGuardadoExitoso = false) }
    }
    fun cargarReportesUsuario() {
        val id = _uiState.value.idUsuarioLogueado
        if (id != 0) {
            _uiState.update { it.copy(reportesUsuario = reporteRepository.obtenerPorUsuario(id)) }
        }
    }}
