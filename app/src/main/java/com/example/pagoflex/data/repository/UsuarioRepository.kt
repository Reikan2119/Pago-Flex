package com.example.pagoflex.data.repository

import android.content.ContentValues
import com.example.pagoflex.data.PagoFlexDbHelper

data class UsuarioDb(
    val id: Int,
    val nombre: String,
    val correo: String,
    val idRol: Int,
    val idEmpresa: Int?,
    val rut: String?,
    val comuna: String?
)

class UsuarioRepository(private val dbHelper: PagoFlexDbHelper) {
    fun login(correo: String, contrasena: String): UsuarioDb? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT id_usuario, nombre, correo, id_rol, id_empresa, rut, comuna FROM usuario WHERE correo=? AND contrasena=?",
            arrayOf(correo, contrasena)
        )
        var result: UsuarioDb? = null
        if (cursor.moveToFirst()) {
            result = UsuarioDb(
                id = cursor.getInt(0),
                nombre = cursor.getString(1) ?: "",
                correo = cursor.getString(2) ?: "",
                idRol = cursor.getInt(3),
                idEmpresa = if (cursor.isNull(4)) null else cursor.getInt(4),
                rut = cursor.getString(5),
                comuna = cursor.getString(6)
            )
        }
        cursor.close()
        return result
    }

    fun registrar(nombre: String, correo: String, contrasena: String, rut: String, comuna: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("correo", correo)
            put("contrasena", contrasena)
            put("rut", rut)
            put("comuna", comuna)
            put("id_rol", 1)
        }
        db.insert("usuario", null, values)
    }

    fun actualizarPerfil(id: Int, nombre: String, correo: String, comuna: String, rut: String? = null) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("correo", correo)
            put("comuna", comuna)
            if (rut != null) put("rut", rut)
        }
        db.update("usuario", values, "id_usuario=?", arrayOf(id.toString()))
    }
}
