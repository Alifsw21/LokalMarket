const express = require("express");
const router = express.Router();
const db = require("./db");

router.get("/", (req, res) => {
    const sql = "SELECT * FROM pesanan ORDER BY id DESC";

    db.query(sql, (err, result) => {
        if (err) {
            return res.status(500).json({
                success: false,
                message: "Gagal mengambil data pesanan"
            });
        } else {
            return res.status(200).json({
                success: true,
                data: result
            });
        }
    });
});

router.post("/", (req, res) => {
    const { idPengguna, totalHarga, alamat, keranjang } = req.body;

    const sqlPesanan = "INSERT INTO pesanan (idPengguna, totalHarga, alamat) VALUES (?, ?, ?)";

    db.query(sqlPesanan, [idPengguna, totalHarga, alamat], (err, result) => {
        if (err) {
            return res.status(500).json({
                success: false,
                message: "Gagal membuat pesanan utama"
            });
        }

        const idPesananBaru = result.insertId;

        if (!keranjang || keranjang.length === 0) {
             return res.status(201).json({
                success: true,
                message: "Berhasil menambahkan pesanan (tanpa barang)"
             });
        }

        let detailSelesai = 0;
        keranjang.forEach((item) => {
            const sqlDetail = "INSERT INTO detail_pesanan (idPesanan, idProduk, jumlahBarang, hargaSatuan) VALUES (?, ?, ?, ?)";

            db.query(sqlDetail, [idPesananBaru, item.idProduk, item.jumlahBarang, item.hargaSatuan], (errDetail) => {
                detailSelesai++;
                if (detailSelesai === keranjang.length) {
                    return res.status(201).json({
                        success: true,
                        message: "Checkout berhasil! Pesanan tersimpan."
                    });
                }
            });
        });
    });
});

router.put("/:id/batal", (req, res) => {
    const id = req.params.id;
    const sql = "UPDATE pesanan SET statusPesanan = 'Dibatalkan' WHERE id = ?";

    db.query(sql, [id], (err, result) => {
        if (err) {
            return res.status(500).json({
                success: false,
                message: "Gagal membatalkan pesanan"
            });
        } else {
            return res.status(200).json({
                success: true,
                message: "Pesanan berhasil dibatalkan"
            });
        }
    });
});

router.delete("/:id", (req, res) => {
    const id = req.params.id;

    const sql = "DELETE FROM pesanan WHERE id = ?";

    db.query(sql, [id], (err, result) => {
        if (err) {
            return res.status(500).json({
                success: false,
                message: "Gagal menghapus pesanan"
            });
        }
        if (result.affectedRows === 0) {
            return res.status(404).json({
                success: false,
                message: "Pesanan tidak ditemukan"
            });
        } else {
            return res.status(200).json({
                success: true,
                message: "Berhasil menghapus pesanan"
            });
        }
    });
});

module.exports = router;