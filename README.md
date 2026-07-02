# LokalMarket: Belanja Tanpa Batas, Tanpa Lag

LokalMarket adalah aplikasi e-commerce yang menawarkan tampilan antarmuka sederhana guna mempermudah pengguna awam dalam mengoperasikannya.

## Arsitektur 
- **Frontend:** Android Studio, Kotlin, ViewBinding, Retrofit2, RecyclerView.
- **Backend:** Node.js, Express, MySQL.
- **Infrastructure:** Docker.

## Langkah-Langkah Menjalankan Program
### 1. Konfigurasi & Menjalankan Backend
1. Pastikan Docker sudah terinstall di komputer anda.
2. Buka terminal dan masuk ke direktori utama backend.
3. Buat file `.env` di root direktori backend dan sesuaikan konfigurasinya:
   ```env
   MYSQL_DATABASE=db_lokalmarket
   MYSQL_ROOT_PASSWORD="changeMe"
   DB_USER=root
   DB_HOST=mysql
   DB_PORT=3306
4. Jalankan container Docker dengan menggunakan perintah:
   ```docker-compose up -d --build```
5. Periksa koneksi server dengan database menggunakan perintah:
   ```docker logs -f backend_service```
6. Jika koneksi gagal gunakan perintah:
   ```docker compose restart backend_service```

### 2. Konfigurasi & Menjalankan Android Studio
1. Buka folder proyek menggunakan Android Studio
2. Sync project
3. Jalankan aplikasi
