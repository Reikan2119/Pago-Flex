package com.example.pagoflex.navigation

sealed class Ruta(val ruta: String) {
    data object Bienvenida : Ruta("bienvenida")
    data object Login : Ruta("login")
    data object Registro : Ruta("registro")
    data object Dashboard : Ruta("dashboard")
    data object Reporte : Ruta("reporte")
    data object Recomendar : Ruta("recomendar")
    data object RegistrarCompromiso : Ruta("registrar_compromiso")
    data object Pago : Ruta("pago")
}