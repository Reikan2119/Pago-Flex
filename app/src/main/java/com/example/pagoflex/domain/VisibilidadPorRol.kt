package com.example.pagoflex.domain

import com.example.pagoflex.model.RolUsuario


object VisibilidadPorRol {

    fun puedeRegistrarCompromiso(rol: RolUsuario?): Boolean =
        rol == RolUsuario.AGENTE || rol == RolUsuario.EJECUTIVO

    fun puedePagar(rol: RolUsuario?): Boolean =
        rol == RolUsuario.USUARIO_FINAL

    fun esSupervisor(rol: RolUsuario?): Boolean =
        rol == RolUsuario.AGENTE || rol == RolUsuario.EJECUTIVO
}
