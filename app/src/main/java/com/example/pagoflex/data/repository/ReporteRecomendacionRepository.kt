package com.example.pagoflex.data.repository

import android.content.ContentValues
import com.example.pagoflex.data.PagoFlexDbHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReporteRepository(private val dbHelper: PagoFlexDbHelper) {
    fun insertar(motivo: String, comentario: String, idUsuario: Int = 1) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("id_usuario", idUsuario)
            put("motivo", motivo)
            put("comentario", comentario)
            put("fecha", SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date()))
        }
        db.insert("reporte_problema", null, values)
    }
}

class RecomendacionRepository(private val dbHelper: PagoFlexDbHelper) {
    fun insertar(nombre: String, rubro: String, comuna: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("rubro", rubro)
            put("comuna", comuna)
            put("fecha", SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date()))
        }
        db.insert("recomendacion_entidad", null, values)
    }
}
