const express = require("express");
const bodyParser = require("body-parser");
const cors = require("cors");

const penggunaRouter = require("./pengguna");
const kategoriRouter = require("./kategori");
const produkRouter = require("./produk");
const promoRouter = require("./promo");
const pesananRouter = require("./pesanan");

const app = express()

app.use(cors())
app.use(bodyParser.json())

app.use("/pengguna", penggunaRouter);
app.use("/kategori", kategoriRouter);
app.use("/produk", produkRouter);
app.use("/promo", promoRouter);
app.use("/pesanan", pesananRouter);

const PORT = 3000;

app.listen(PORT, () => {
    console.log(`Server berjalan pada port ${PORT}`)
});