const mysql = require("mysql2")

const connection = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "",
    database: "db_lokalMarket"
})

connection.connect((err) => {
    if (err) {
        console.log("Koneksi database gagal")
    } else {
        console.log("Koneksi database berhasil")
    }
})

module.exports = connection