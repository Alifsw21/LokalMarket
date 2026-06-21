package com.app.lokalmarket.v1.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.app.lokalmarket.v1.data.model.Keranjang

class KeranjangDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "keranjang_belanja.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_KERANJANG = "keranjang"

        private const val COL_ID = "id"
        private const val COL_IDPRODUK = "idProduk"
        private const val COL_NAMA = "namaProduk"
        private const val COL_GAMBAR = "gambarProduk"
        private const val COL_HARGA = "hargaSatuan"
        private const val COL_JUMLAH = "jumlahBarang"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_KERANJANG (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_IDPRODUK INTEGER NOT NULL,
                $COL_NAMA TEXT NOT NULL,
                $COL_GAMBAR TEXT NOT NULL,
                $COL_HARGA INTEGER NOT NULL,
                $COL_JUMLAH INTEGER NOT NULL
            )
        """.trimIndent()

        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_KERANJANG")
        onCreate(db)
    }

    fun insertKeranjang(keranjang: Keranjang): Boolean {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COL_IDPRODUK, keranjang.idProduk)
            put(COL_NAMA, keranjang.namaProduk)
            put(COL_GAMBAR, keranjang.gambarProduk)
            put(COL_HARGA, keranjang.hargaSatuan)
            put(COL_JUMLAH, keranjang.jumlahBarang)
        }

        val result = db.insert(TABLE_KERANJANG, null, values)
        db.close()

        return result != -1L
    }

    fun getAllKeranjang(): List<Keranjang> {
        val keranjangList = mutableListOf<Keranjang>()
        val db = this.readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_KERANJANG ORDER BY $COL_ID DESC",
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val keranjang = Keranjang(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    idProduk = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IDPRODUK)),
                    namaProduk = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAMA)),
                    gambarProduk = cursor.getString(cursor.getColumnIndexOrThrow(COL_GAMBAR)),
                    hargaSatuan = cursor.getInt(cursor.getColumnIndexOrThrow(COL_HARGA)),
                    jumlahBarang = cursor.getInt(cursor.getColumnIndexOrThrow(COL_JUMLAH))
                )
                keranjangList.add(keranjang)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return keranjangList
    }

    fun updateJumlahBarang(id: Int, jumlahBaru: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_JUMLAH, jumlahBaru)
        }

        val result = db.update(
            TABLE_KERANJANG,
            values,
            "$COL_ID = ?",
            arrayOf(id.toString())
        )
        db.close()
        return result > 0
    }

    fun deleteKeranjang(id: Int) : Boolean {
        val db = writableDatabase
        val result = db.delete(
            TABLE_KERANJANG,
            "$COL_ID = ?",
            arrayOf(id.toString())
        )
        db.close()
        return result > 0
    }

    fun clearKeranjang() {
        val db = this.writableDatabase
        db.delete(TABLE_KERANJANG, null, null)
        db.close()
    }
}