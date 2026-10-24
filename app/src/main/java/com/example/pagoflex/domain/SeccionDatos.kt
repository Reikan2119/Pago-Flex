package com.example.pagoflex.domain

data class CampoVisible(val etiqueta: String, val valor: String)
data class SeccionDatos(val titulo: String, val campos: List<CampoVisible>)
