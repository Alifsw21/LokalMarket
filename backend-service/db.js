const mysql = require("mysql2");

const pool = mysql.createPool({
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    port: process.env.DB_PORT,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_NAME
});

pool.getConnection((err, connection) => {
    if (err) {
        console.error("Koneksi database gagal:", err.message);
    } else {
        console.log("Koneksi database berhasil");
        connection.release();
    }
})

module.exports = pool;