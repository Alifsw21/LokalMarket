CREATE DATABASE IF NOT EXISTS db_lokalmarket;

USE db_lokalmarket;

CREATE TABLE pengguna (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nama VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE kategori (
    id INT PRIMARY KEY AUTO_INCREMENT,
    namaKategori VARCHAR(100) NOT NULL
);

CREATE TABLE promo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    judulPromo VARCHAR(100) NOT NULL,
    statusAktif ENUM('Aktif', 'Tidak Aktif') DEFAULT 'Aktif',
    diskon INT
);

CREATE TABLE produk (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idKategori INT NOT NULL,
    namaProduk VARCHAR(150) NOT NULL,
    deskripsi TEXT NOT NULL,
    harga INT NOT NULL,
    idPromo INT NOT NULL,
    FOREIGN KEY (idKategori) REFERENCES kategori(id),
    FOREIGN KEY (idPromo) REFERENCES promo(id)
);

CREATE TABLE pesanan (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idPengguna INT NOT NULL,
    tanggalPesanan DATETIME DEFAULT CURRENT_TIMESTAMP,
    totalHarga INT NOT NULL,
    alamat TEXT NOT NULL,
    statusPesanan ENUM('Menunggu Pembayaran', 'Diproses', 'Dikirim', 'Selesai', 'Dibatalkan') DEFAULT 'Menunggu Pembayaran',
    FOREIGN KEY (idPengguna) REFERENCES pengguna(id)
);

CREATE TABLE detailpesanan (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idPesanan INT NOT NULL,
    idProduk INT NOT NULL,
    jumlahBarang INT NOT NULL,
    hargaSatuan INT NOT NULL,
    FOREIGN KEY (idPesanan) REFERENCES pesanan(id),
    FOREIGN KEY (idProduk) REFERENCES produk(id)
);

-- Seed Data Awal
INSERT INTO pengguna (id, nama, email, password) VALUES 
(1, 'doni', 'doni@gmail.com', 'doni123')
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO kategori (id, namaKategori) VALUES 
(1, 'Umum')
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO promo (id, judulPromo, statusAktif, diskon) VALUES 
(1, 'No Promo', 'Aktif', 0),
(2, 'Promo 10%', 'Aktif', 10)
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO produk (id, idKategori, namaProduk, deskripsi, harga, idPromo) VALUES 
(1, 1, 'Sepatu Anti Mainstream', 'sepatu ini sangat bagus dipakai untuk menggapai mimpi yang belum tercapai', 15000000, 1),
(2, 1, 'T-shirt keep going on', 'baju ini sangat sejuk dipakai, penggunanya akan merasa seolah berada di surga', 250000, 2)
ON DUPLICATE KEY UPDATE id=id;