package com.example.pagoflex.data.repository

import android.content.ContentValues
import com.example.pagoflex.data.PagoFlexDbHelper

data class ReporteDb(
    val id: Int,
    val idUsuario: Int,
    val motivo: String,
    val comentario: String,
    val fecha: String,
    val estado: String,
    val respuesta: String?,
    val fechaGestion: String?
)

class ReporteGestionRepository(private val dbHelper: PagoFlexDbHelper) {
    fun obtenerTodos(): List<ReporteDb> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT id_reporte, id_usuario, motivo, comentario, fecha, estado, respuesta, fecha_gestion FROM reporte_problema", null)
        val lista = mutableListOf<ReporteDb>()
        while (cursor.moveToNext()) {
            lista.add(ReporteDb(
                id = cursor.getInt(0),
                idUsuario = cursor.getInt(1),
                motivo = cursor.getString(2) ?: "",
                comentario = cursor.getString(3) ?: "",
                fecha = cursor.getString(4) ?: "",
                estado = cursor.getString(5) ?: "Pendiente",
                respuesta = cursor.getString(6),
                fechaGestion = cursor.getString(7)
            ))
        }
        cursor.close()
        return lista
    }

    fun tomar(id: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("estado", "En gestión")
            put("fecha_gestion", java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault()).format(java.util.Date()))
        }
        db.update("reporte_problema", values, "id_reporte=?", arrayOf(id.toString()))
    }

    fun responder(id: Int, respuesta: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply { put("respuesta", respuesta) }
        db.update("reporte_problema", values, "id_reporte=?", arrayOf(id.toString()))
    }

    fun resolver(id: Int, resultado: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("estado", "Resuelto")
            put("respuesta", resultado)
            put("fecha_gestion", java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault()).format(java.util.Date()))
        }
        db.update("reporte_problema", values, "id_reporte=?", arrayOf(id.toString()))
    }
}

class RecomendacionGestionRepository(private val dbHelper: PagoFlexDbHelper) {
    fun obtenerTodas(): List<Pair<Int, String>> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT id_recomendacion, nombre, estado FROM recomendacion_entidad", null)
        val lista = mutableListOf<Pair<Int, String>>()
        while (cursor.moveToNext()) {
            lista.add(Pair(cursor.getInt(0), "${cursor.getString(1)} - ${cursor.getString(2) ?: "Pendiente"}"))
        }
        cursor.close()
        return lista
    }

    fun cambiarEstado(id: Int, estado: String, motivo: String? = null) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("estado", estado)
        }
        db.update("recomendacion_entidad", values, "id_recomendacion=?", arrayOf(id.toString()))
    }
}
