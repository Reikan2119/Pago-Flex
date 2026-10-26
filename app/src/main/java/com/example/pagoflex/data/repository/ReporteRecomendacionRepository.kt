package com.example.pagoflex.data.repository

import android.content.ContentValues
import com.example.pagoflex.data.PagoFlexDbHelper
import com.example.pagoflex.model.ReporteProblema
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

    fun obtenerPorUsuario(idUsuario: Int): List<ReporteProblema> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT id_reporte, id_usuario, motivo, comentario, fecha, estado, respuesta, fecha_gestion FROM reporte_problema WHERE id_usuario=?", arrayOf(idUsuario.toString()))
        val lista = mutableListOf<ReporteProblema>()
        while (cursor.moveToNext()) {
            lista.add(ReporteProblema(
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

