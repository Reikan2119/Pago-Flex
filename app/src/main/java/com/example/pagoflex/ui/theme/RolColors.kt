package com.example.pagoflex.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.pagoflex.model.RolUsuario

fun colorDeRol(rol: RolUsuario): Color = when (rol) {
    RolUsuario.USUARIO_FINAL -> Color(0xFF2E7D32)
    RolUsuario.AGENTE -> Color(0xFFEF6C00)
    RolUsuario.EJECUTIVO -> Color(0xFF1565C0)
}

