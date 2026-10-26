package com.example.pagoflex.navigation

sealed class Ruta(val ruta: String) {
    data object Bienvenida : Ruta("bienvenida")
    data object HomePerfil : Ruta("home_perfil")
    data object AgenteHome : Ruta("agente_home")
    data object Pagar : Ruta("pagar")
    data object HistorialPagos : Ruta("historial_pagos")
    data object MisReportes : Ruta("mis_reportes")
    data object Login : Ruta("login")
    data object Registro : Ruta("registro")
    data object Dashboard : Ruta("dashboard")
    data object Reporte : Ruta("reporte")
    data object Recomendar : Ruta("recomendar")
    data object RegistrarCompromiso : Ruta("registrar_compromiso")
    data object Pago : Ruta("pago")
    data object Perfil : Ruta("perfil")
    data object DetalleCompromiso : Ruta("detalle_compromiso")
    data object Comprobante : Ruta("comprobante")
    data object ReportesAgente : Ruta("reportes_agente")
    data object RecomendacionesAgente : Ruta("recomendaciones_agente")
    data object ResumenCobranza : Ruta("resumen_cobranza")
}

