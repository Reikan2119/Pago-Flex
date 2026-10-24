package com.example.pagoflex.ui.state

import com.example.pagoflex.model.RolUsuario

data class AuthUiState(
    val correoLogin: String = "",
    val errorCorreoLogin: String? = null,
    val contrasenaLogin: String = "",
    val errorContrasenaLogin: String? = null,
    val nombreLogin: String = "",
    val rolSeleccionado: RolUsuario? = null,
    val sesionIniciada: Boolean = false,
    val idUsuarioLogueado: Int = 0,
    val perfilNombre: String = "",
    val perfilCorreo: String = "",
    val perfilRut: String = "",
    val perfilComuna: String = "",

    val nombreRegistro: String = "",
    val errorNombreRegistro: String? = null,
    val rutRegistro: String = "",
    val errorRutRegistro: String? = null,
    val comunaRegistro: String = "",
    val errorComunaRegistro: String? = null,
    val correoRegistro: String = "",
    val errorCorreoRegistro: String? = null,
    val contrasenaRegistro: String = "",
    val errorContrasenaRegistro: String? = null,
    val registroUsuarioExitoso: Boolean = false
)
