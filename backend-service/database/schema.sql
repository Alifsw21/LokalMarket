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