const express = require("express");
const router = express.Router();
const db = require("./db");

router.get("/", (req, res) => {
    const sql = `SELECT
        produk.id,
        produk.nama,
        produk.harga,
        produk.diskon AS diskon,
        kategori.namaKategori AS kategori
        FROM produk
        LEFT JOIN promo ON produk.idPromo = promo.id
        LEFT JOIN kategori ON produk.idKategori = kategori.id`;

    db.query(sql, (err, result) => {
        if (err) {
            return res.status(500).json({
                success: false,
                message: "Gagal mengambil data produk"
            });
        } else {
            return res.status(200).json({
                success: true,
                message: "Berhasil mengambil data produk",
                data: result
            });
        }
    });
});

router.get("/:id", (req, res) => {
    const id = req.params.id;

    const sql = `SELECT
            produk.id,
            produk.nama,
            produk.harga,
            produk.diskon AS diskon,
            kategori.namaKategori AS kategori
            FROM produk
            LEFT JOIN promo ON produk.idPromo = promo.id
            LEFT JOIN kategori ON produk.idKategori = kategori.id
            WHERE produk.id = ?`;

    db.query(sql, [id], (err, result) => {
        if (err) {
            return res.status(500).json({
                success: false,
                message: "Gagal mengambil detail produk"
            });
        }

        if (result.length === 0) {
            return res.status(404).json({
                success: false,
                message: "Produk tidak tersedia"
            });
        } else {
            return res.status(200).json({
                success: true,
                message: "Berhasil mengambil detail produk",
                data: result[0]
            });
        }
    });
});

module.exports = router;