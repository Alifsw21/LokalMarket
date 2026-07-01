const express = require("express");
const router = express.Router();
const db = require("./db");

router.get("/", (req, res) => {
    const idPengguna = req.query.idPengguna;
    if (idPengguna) {
        const sql = "SELECT * FROM pesanan WHERE idPengguna = ? ORDER BY id DESC";
        db.query(sql, [idPengguna], (err, result) => {
            if (err) return res.status(500).json({ success: false, message: "Gagal mengambil data pesanan" });
            res.status(200).json({ success: true, data: result });
        });
    } else {
        const sql = "SELECT * FROM pesanan ORDER BY id DESC";
        db.query(sql, (err, result) => {
            if (err) return res.status(500).json({ success: false, message: "Gagal mengambil data pesanan" });
            res.status(200).json({ success: true, data: result });
        });
    }
});

router.get("/:id", (req, res) => {
    const id = req.params.id;
    const sql = `SELECT dp.*, IFNULL(p.namaProduk, CONCAT('Produk #', dp.idProduk)) as namaProduk
                 FROM detailpesanan dp
                 LEFT JOIN produk p ON dp.idProduk = p.id
                 WHERE dp.idPesanan = ?`;
    db.query(sql, [id], (err, result) => {
        if (err) return res.status(500).json({ success: false, message: "Gagal mengambil detail" });
        res.status(200).json({ success: true, data: result });
    });
});

router.post("/", (req, res) => {
    const { idPengguna, totalHarga, alamat, keranjang } = req.body;
    const sqlPesanan = "INSERT INTO pesanan (idPengguna, totalHarga, alamat) VALUES (?, ?, ?)";

    db.query(sqlPesanan, [idPengguna, totalHarga, alamat], (err, result) => {
        if (err) return res.status(500).json({ success: false, message: "Gagal simpan pesanan" });
        const idPesananBaru = result.insertId;

        let detailSelesai = 0;
        if (!keranjang || keranjang.length === 0) return res.status(201).json({ success: true });

        keranjang.forEach((item) => {
            const sqlDetail = "INSERT INTO detailpesanan (idPesanan, idProduk, jumlahBarang, hargaSatuan) VALUES (?, ?, ?, ?)";
            db.query(sqlDetail, [idPesananBaru, item.idProduk, item.jumlahBarang, item.hargaSatuan], (errDetail) => {
                if (errDetail) {
                    console.error("Gagal simpan detail pesanan:", errDetail);
                }
                detailSelesai++;
                if (detailSelesai === keranjang.length) {
                    res.status(201).json({ success: true, message: "Checkout berhasil!" });
                }
            });
        });
    });
});

router.delete("/:id", (req, res) => {
    const id = req.params.id;
    db.query("DELETE FROM pesanan WHERE id = ?", [id], (err) => {
        if (err) return res.status(500).json({ success: false });
        res.status(200).json({ success: true, message: "Terhapus" });
    });
});

module.exports = router;