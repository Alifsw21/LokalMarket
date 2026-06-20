const express = require("express");
const router = express.Router();
const db = require("./db");

router.post("/register", (req, res) => {
    const { nama, email, password } = req.body;

    const sql = "INSERT INTO pengguna (nama, email, password) VALUES (?, ?, ?)";

    db.query(sql, [nama, email, password], (err, result) => {
        if (err) {
            return res.status(500).json({
                success: false,
                message: "Gagal membuat data pengguna"
            });
        } else {
            return res.status(201).json({
                success: true,
                message: "Pengguna berhasil ditambahkan"
            });
        }
    });
});

router.post("/login", (req, res) => {
    const { email, password } = req.body;

    const sql = "SELECT * FROM pengguna WHERE email = ? AND password = ?";

    db.query(sql, [email, password], (err, result) => {
        if (err) {
            return res.status(500).json({
                success: false,
                message: "Terjadi kesalahan pada server"
            });
        }

        if (result.length > 0) {
            return res.status(200).json({
                success: true,
                message: "Login berhasil",
                data: result[0]
            });
        } else {
            return res.status(401).json({
                success: false,
                message: "Email atau password salah, silahkan coba lagi"
            });
        }
    });
});

module.exports = router;