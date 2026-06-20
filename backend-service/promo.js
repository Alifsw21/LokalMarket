const express = require("express");
const router = express.Router();
const db = require("./db");

router.get("/", (req, res) => {
    const sql = "SELECT * FROM promo";

    db.query(sql, (err, result) => {
        if (err) {
            return res.status(500).json({
                success: false,
                message: "Gagal mengambil data promo"
            });
        } else {
            return res.status(200).json({
                success: true,
                message: "Berhasil mengambil data promo",
                data: result
            });
        }
    });
});

router.get("/:id", (req, res) => {
    const id = req.params.id;

    const sql = "SELECT * FROM promo WHERE id = ?";

    db.query(sql, [id], (err, result) => {
        if (err) {
            return res.status(500).json({
                success: false,
                message: "Gagal mengambil detail promo"
            });
        }

        if (result.length === 0) {
            return res.status(404).json({
                success: false,
                message: "Data promo tidak ditemukan"
            });
        } else {
            return res.status(200).json({
                success: true,
                message: "Berhasil mengambil detail promo",
                data: result[0]
            });
        }
    });
});

module.exports = router;