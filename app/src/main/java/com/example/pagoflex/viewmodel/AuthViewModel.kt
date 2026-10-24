package com.example.pagoflex.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.pagoflex.data.PagoFlexDbHelper
import com.example.pagoflex.data.SesionPreferencias
import com.example.pagoflex.data.repository.UsuarioRepository
import com.example.pagoflex.model.RolUsuario
import com.example.pagoflex.ui.state.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper = PagoFlexDbHelper(application)
    private val usuarioRepository = UsuarioRepository(dbHelper)
    private val sesion = SesionPreferencias(application)

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onCorreoLoginCambia(v: String) = _uiState.update { it.copy(correoLogin = v, errorCorreoLogin = null) }
    fun onContrasenaLoginCambia(v: String) = _uiState.update { it.copy(contrasenaLogin = v, errorContrasenaLogin = null) }

    fun onIntentarLogin(): Boolean {
        val e = _uiState.value
        val errCorreo = if (e.correoLogin.isBlank() || !e.correoLogin.contains("@")) "Ingresa un correo válido" else null
        val errPass = if (e.contrasenaLogin.isBlank()) "La contraseña es obligatoria" else null
        if (errCorreo != null || errPass != null) {
            _uiState.update { it.copy(errorCorreoLogin = errCorreo, errorContrasenaLogin = errPass) }
            return false
        }
        val u = usuarioRepository.login(e.correoLogin, e.contrasenaLogin)
        if (u == null) {
            _uiState.update { it.copy(errorContrasenaLogin = "Correo o contraseña incorrectos") }
            return false
        }
        val rol = when (u.idRol) { 2 -> RolUsuario.AGENTE; 3 -> RolUsuario.EJECUTIVO; else -> RolUsuario.USUARIO_FINAL }
        sesion.guardar(u.id.toLong())
        _uiState.update {
            it.copy(
                sesionIniciada = true,
                idUsuarioLogueado = u.id,
                nombreLogin = u.nombre,
                perfilNombre = u.nombre,
                perfilCorreo = u.correo,
                perfilRut = u.rut ?: "",
                perfilComuna = u.comuna ?: "",
                rolSeleccionado = rol
            )
        }
        return true
    }

    // Registro
    fun onNombreRegistroCambia(v: String) = _uiState.update { it.copy(nombreRegistro = v, errorNombreRegistro = null) }
    fun onRutRegistroCambia(v: String) = _uiState.update { it.copy(rutRegistro = v, errorRutRegistro = null) }
    fun onComunaRegistroCambia(v: String) = _uiState.update { it.copy(comunaRegistro = v, errorComunaRegistro = null) }
    fun onCorreoRegistroCambia(v: String) = _uiState.update { it.copy(correoRegistro = v, errorCorreoRegistro = null) }
    fun onContrasenaRegistroCambia(v: String) = _uiState.update { it.copy(contrasenaRegistro = v, errorContrasenaRegistro = null) }

    fun onRegistrarUsuario(): Boolean {
        val e = _uiState.value
        val errNombre = if (e.nombreRegistro.isBlank()) "El nombre es obligatorio" else null
        val errRut = if (e.rutRegistro.isBlank()) "El RUT es obligatorio" else null
        val errComuna = if (e.comunaRegistro.isBlank()) "La comuna es obligatoria" else null
        val errCorreo = if (e.correoRegistro.isBlank() || !e.correoRegistro.contains("@")) "Ingresa un correo válido" else null
        val errPass = if (e.contrasenaRegistro.length < 6) "Mínimo 6 caracteres" else null
        if (errNombre != null || errRut != null || errComuna != null || errCorreo != null || errPass != null) {
            _uiState.update { it.copy(errorNombreRegistro = errNombre, errorRutRegistro = errRut, errorComunaRegistro = errComuna, errorCorreoRegistro = errCorreo, errorContrasenaRegistro = errPass, registroUsuarioExitoso = false) }
            return false
        }
        usuarioRepository.registrar(e.nombreRegistro, e.correoRegistro, e.contrasenaRegistro, e.rutRegistro, e.comunaRegistro)
        _uiState.update { it.copy(nombreRegistro = "", rutRegistro = "", comunaRegistro = "", correoRegistro = "", contrasenaRegistro = "", registroUsuarioExitoso = true) }
        return true
    }

    fun cerrarSesion() {
        sesion.borrar()
        _uiState.value = AuthUiState()
    }
}
