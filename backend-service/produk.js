const express = require("express");
const router = express.Router();
const db = require("./db");

router.get("/", (req, res) => {
    const sql = "SELECT * FROM produk";

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

    const sql = "SELECT * FROM produk WHERE id = ?";

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