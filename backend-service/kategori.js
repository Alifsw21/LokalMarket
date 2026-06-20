const express = require("express");
const router = express.Router();
const db = require("./db");

router.get("/", (req, res) => {
    const sql = "SELECT * FROM kategori";

    db.query(sql, (err, result) => {
        if (err) {
            return res.status(500).json({
                success: false,
                message: "Gagal mengambil data kategori"
            });
        } else {
            return res.status(200).json({
                success: true,
                message: "Berhasil mengambil data kategori",
                data: result
            });
        }
    });
});

router.get("/:id", (req,res) => {
    const id = req.params.id;

    const sql = "SELECT * FROM kategori WHERE id = ?";

    db.query(sql, [id], (err, result) => {
        if (err) {
            return res.status(500).json({
                success: false,
                message: "Gagal mengambil data detail kategori"
            });
        }

        if (result.length === 0) {
            return res.status(404).json({
                success: false,
                message: "Kategori tidak ditemukan"
            });
        } else {
            return res.status(200).json({
                success: true,
                message: "Berhasil mengambil data kategori",
                data: result[0]
            });
        }
    });
});

module.exports = router;