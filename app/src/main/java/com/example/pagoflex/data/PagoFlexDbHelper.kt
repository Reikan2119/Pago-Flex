package com.example.pagoflex.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PagoFlexDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "pagoflex.db"
        const val DB_VERSION = 3
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE rol_usuario (
                id_rol INTEGER PRIMARY KEY,
                etiqueta TEXT NOT NULL
            )
        """.trimIndent())
        db.execSQL("""
            CREATE TABLE usuario (
                id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                correo TEXT,
                contrasena TEXT,
                rut TEXT,
                comuna TEXT,
                id_rol INTEGER,
                id_empresa INTEGER,
                FOREIGN KEY(id_rol) REFERENCES rol_usuario(id_rol)
            )
        """.trimIndent())
        db.execSQL("""
            CREATE TABLE estado_compromiso (
                id_estado INTEGER PRIMARY KEY,
                nombre TEXT NOT NULL
            )
        """.trimIndent())
        db.execSQL("""
            CREATE TABLE compromiso (
                id_compromiso INTEGER PRIMARY KEY AUTOINCREMENT,
                folio TEXT,
                id_usuario_final INTEGER,
                id_empresa INTEGER,
                concepto TEXT,
                cuota TEXT,
                monto INTEGER,
                fecha_vencimiento TEXT,
                id_estado_compromiso INTEGER,
                FOREIGN KEY(id_usuario_final) REFERENCES usuario(id_usuario),
                FOREIGN KEY(id_estado_compromiso) REFERENCES estado_compromiso(id_estado)
            )
        """.trimIndent())
        db.execSQL("""
            CREATE TABLE reporte_problema (
                id_reporte INTEGER PRIMARY KEY AUTOINCREMENT,
                id_usuario INTEGER,
                motivo TEXT,
                comentario TEXT,
                fecha TEXT,
                estado TEXT,
                respuesta TEXT,
                fecha_gestion TEXT,
                FOREIGN KEY(id_usuario) REFERENCES usuario(id_usuario)
            )
        """.trimIndent())
        db.execSQL("""
            CREATE TABLE recomendacion_entidad (
                id_recomendacion INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                rubro TEXT,
                comuna TEXT,
                fecha TEXT,
                estado TEXT
            )
        """.trimIndent())
        db.execSQL("""
            CREATE TABLE comprobante (
                id_comprobante INTEGER PRIMARY KEY AUTOINCREMENT,
                id_compromiso INTEGER,
                fecha_pago TEXT,
                monto INTEGER,
                folio TEXT,
                FOREIGN KEY(id_compromiso) REFERENCES compromiso(id_compromiso)
            )
        """.trimIndent())

        // Precarga
        db.execSQL("INSERT INTO rol_usuario (id_rol, etiqueta) VALUES (1,'Usuario Final')")
        db.execSQL("INSERT INTO rol_usuario (id_rol, etiqueta) VALUES (2,'Agente PagoFlex')")
        db.execSQL("INSERT INTO rol_usuario (id_rol, etiqueta) VALUES (3,'Ejecutivo de Empresa')")

        db.execSQL("INSERT INTO estado_compromiso (id_estado, nombre) VALUES (1,'Pendiente')")
        db.execSQL("INSERT INTO estado_compromiso (id_estado, nombre) VALUES (3,'Pagado')")

        db.execSQL("INSERT INTO usuario (id_usuario, nombre, correo, contrasena, rut, comuna, id_rol, id_empresa) VALUES (1,'Usuario Demo','usuario@demo.cl','123456','12.345.678-9','Santiago',1,NULL)")
        db.execSQL("INSERT INTO usuario (id_usuario, nombre, correo, contrasena, rut, comuna, id_rol, id_empresa) VALUES (2,'Agente Demo','agente@demo.cl','123456','98.765.432-1','Santiago',2,NULL)")
        db.execSQL("INSERT INTO usuario (id_usuario, nombre, correo, contrasena, rut, comuna, id_rol, id_empresa) VALUES (3,'Ejecutivo Demo','ejecutivo@demo.cl','123456','11.222.333-4','Santiago',3,1)")

        db.execSQL("INSERT INTO compromiso (id_compromiso, folio, id_usuario_final, id_empresa, concepto, cuota, monto, fecha_vencimiento, id_estado_compromiso) VALUES (1,'CP-2026-001',1,1,'Cuota GC - Altos del Maipo','1/12',85000,'15-11-2026',1)")
        db.execSQL("INSERT INTO compromiso (id_compromiso, folio, id_usuario_final, id_empresa, concepto, cuota, monto, fecha_vencimiento, id_estado_compromiso) VALUES (2,'CP-2026-002',1,6,'Mensualidad - Club Los Halcones','1/1',45000,'20-11-2026',1)")
        db.execSQL("INSERT INTO compromiso (id_compromiso, folio, id_usuario_final, id_empresa, concepto, cuota, monto, fecha_vencimiento, id_estado_compromiso) VALUES (3,'CP-2026-003',1,8,'Arriendo Depo - Plaza Oriente','3/12',120000,'05-12-2026',1)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS comprobante")
        db.execSQL("DROP TABLE IF EXISTS recomendacion_entidad")
        db.execSQL("DROP TABLE IF EXISTS reporte_problema")
        db.execSQL("DROP TABLE IF EXISTS compromiso")
        db.execSQL("DROP TABLE IF EXISTS estado_compromiso")
        db.execSQL("DROP TABLE IF EXISTS usuario")
        db.execSQL("DROP TABLE IF EXISTS rol_usuario")
        onCreate(db)
    }
}
