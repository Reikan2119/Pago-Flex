package com.example.pagoflex.data.repository

import android.content.ContentValues
import com.example.pagoflex.data.PagoFlexDbHelper
import com.example.pagoflex.model.Compromiso

class CompromisoRepository(private val dbHelper: PagoFlexDbHelper) : RepositorioBase<Compromiso> {

    override fun obtenerTodos(): List<Compromiso> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT id_compromiso, folio, id_usuario_final, id_empresa, concepto, cuota, monto, fecha_vencimiento, id_estado_compromiso FROM compromiso",
            null
        )
        val lista = mutableListOf<Compromiso>()
        while (cursor.moveToNext()) {
            val idEmpresa = cursor.getInt(3)
            val nombreEmpresa = when (idEmpresa) {
                1 -> "Altos del Maipo"
                6 -> "Club Los Halcones"
                8 -> "Plaza Oriente"
                else -> "Empresa #$idEmpresa"
            }
            lista.add(
                Compromiso(
                    idCompromiso = cursor.getInt(0),
                    folio = cursor.getString(1) ?: "",
                    idUsuarioFinal = cursor.getInt(2),
                    idEmpresa = idEmpresa,
                    nombreEmpresa = nombreEmpresa,
                    concepto = cursor.getString(4) ?: "",
                    cuota = cursor.getString(5) ?: "",
                    monto = cursor.getInt(6),
                    fechaVencimiento = cursor.getString(7) ?: "",
                    idEstadoCompromiso = cursor.getInt(8)
                )
            )
        }
        cursor.close()
        return lista
    }

    fun obtenerPorUsuario(idUsuario: Int): List<Compromiso> =
        obtenerTodos().filter { it.idUsuarioFinal == idUsuario }

    fun insertar(compromiso: Compromiso) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("folio", compromiso.folio)
            put("id_usuario_final", compromiso.idUsuarioFinal)
            put("id_empresa", compromiso.idEmpresa)
            put("concepto", compromiso.concepto)
            put("cuota", compromiso.cuota)
            put("monto", compromiso.monto)
            put("fecha_vencimiento", compromiso.fechaVencimiento)
            put("id_estado_compromiso", compromiso.idEstadoCompromiso)
        }
        db.insert("compromiso", null, values)
    }

    fun marcarPagado(idCompromiso: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply { put("id_estado_compromiso", 3) }
        db.update("compromiso", values, "id_compromiso=?", arrayOf(idCompromiso.toString()))
    }

    fun insertarComprobante(idCompromiso: Int, monto: Int, folio: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("id_compromiso", idCompromiso)
            put("fecha_pago", java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault()).format(java.util.Date()))
            put("monto", monto)
            put("folio", folio)
        }
        db.insert("comprobante", null, values)
    }

    fun obtenerComprobante(idCompromiso: Int): android.database.Cursor {
        val db = dbHelper.readableDatabase
        return db.rawQuery("SELECT fecha_pago, monto, folio FROM comprobante WHERE id_compromiso=? ORDER BY id_comprobante DESC LIMIT 1", arrayOf(idCompromiso.toString()))
    }
}

