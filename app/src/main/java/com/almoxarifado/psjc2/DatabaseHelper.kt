package com.almoxarifado.psjc2

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "patrimonio.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_PATRIMONIO = "patrimonio"
        const val COL_PLACA_PATRIMONIAL = "placa_patrimonial"
        const val COL_DESCRICAO = "descricao"
        const val COL_UO = "uo"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE " + TABLE_PATRIMONIO + "("
                + COL_PLACA_PATRIMONIAL + " TEXT PRIMARY KEY,"
                + COL_DESCRICAO + " TEXT,"
                + COL_UO + " TEXT" + ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PATRIMONIO")
        onCreate(db)
    }

    // Função para buscar patrimônio pelo código da placa patrimonial
    fun getPatrimonioByPlaca(placaPatrimonial: String): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT * FROM $TABLE_PATRIMONIO WHERE $COL_PLACA_PATRIMONIAL = ?",
            arrayOf(placaPatrimonial)
        )
    }
}
