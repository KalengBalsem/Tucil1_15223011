# Tucil1-IF2211
## Penyelesaian IQ Puzzler Pro dengan Algoritma Brute Force

Program ini merupakan implementasi algoritma Brute Force untuk menyelesaikan permainan IQ Puzzler Pro. Pemain harus mengisi papan berukuran N × M dengan P blok puzzle unik. Program membaca file input yang mendefinisikan ukuran papan, jumlah blok, jenis kasus (DEFAULT atau CUSTOM), dan bentuk setiap blok. Algoritma mencoba semua kemungkinan penyusunan blok dengan rotasi dan pencerminan hingga menemukan solusi yang memenuhi aturan permainan atau menyatakan bahwa tidak ada solusi. Hasilnya ditampilkan dalam format berwarna, mencatat waktu eksekusi serta jumlah iterasi yang dilakukan.

### Beberapa batasan program dan asumsi yang digunakan:
- Setiap blok puzzle direpresentasikan dengan huruf kapital A-Z, tanpa karakter lain.
- Tiap input yang memiliki bentuk berbeda harus memiliki representasi alfabet yang berbeda juga (tidak ada bentuk yang berbeda dengan alfabet yang sama).
- Tidak ada dua blok dengan huruf alfabet yang sama.
- Puzzle selalu memiliki konfigurasi yang valid berdasarkan format input (DEFAULT atau CUSTOM).
- Pada mode CUSTOM, area papan yang harus diisi hanya ditandai dengan karakter 'X', sedangkan '.' menandakan area kosong yang tidak perlu diisi.

## Struktur Folder
*Workspace* ini berisi folder-folder berikut:
- `src`: Berisi source code program.
- `bin`: Menyimpan file eksekusi yang telah dikompilasi (disesuaikan dengan bahasa pemrograman yang digunakan).
- `test`: Berisi solusi jawaban dari data uji yang digunakan dalam laporan.
- `doc`: Berisi laporan tugas kecil dalam format PDF.

## Program Requirements & Dependencies
Program ini dibuat dengan Visual Studio Code. Berikut merupakan beberapa *dependencies* yang diperlukan untuk menjalankan program.
- Java 17 atau lebih baru
- (Opsional) Terminal dengan dukungan ANSI escape code untuk output berwarna

### Instalasi Java (Jika Belum Terinstal)
Windows: Unduh dan instal dari [`Oracle JDK`](https://www.oracle.com/java/technologies/downloads/?er=221886) atau gunakan[` Adoptium OpenJDK`](https://adoptium.net/).

Linux/macOS:
    ```
    sudo apt install openjdk-17-jdk  # Untuk Ubuntu/Debian  
    brew install openjdk@17          # Untuk macOS dengan Homebrew  
    ```
Detail lebih lanjut mengenai java dependency dalam VSCode dapat diakses di [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## Cara Menjalankan dan Menggunakan Program
### 1. Pastikan Java Sudah Terinstal
Program ini membutuhkan **Java 17 atau lebih baru**.
Cek versi Java yang terinstal dengan perintah:
```sh
java -version
```
Jika belum terinstal, silakan instal sesuai dengan keterangan dalam [Program Requirements & Dependencies](#cara-menjalankan-dan-menggunakan-program)

---

### 2. Kompilasi Program
Buka terminal atau command prompt, lalu jalankan:
```sh
javac -d bin src/*.java
```
Perintah ini akan mengompilasi semua file `.java` dalam folder `src/` dan menyimpannya ke dalam folder `bin/`.

---

### 3. Jalankan Program
Setelah program dikompilasi, jalankan dengan perintah:
```sh
java -cp bin Main
```

---

### 4. Input File dan Menjalankan Program
Saat program dijalankan, pengguna akan diminta memasukkan **nama file input** yang berisi spesifikasi puzzle, misalnya:
```
Enter filename (including .txt): ./test_input/test1.txt
```
Program kemudian akan membaca file, memproses puzzle, dan menampilkan hasilnya di terminal.

---

### 5. Output dan Penyimpanan Hasil
- Jika solusi ditemukan, program akan menampilkan papan permainan yang sudah terisi.
- Program juga akan mencetak **waktu eksekusi** dan **jumlah iterasi** brute force.
- Pengguna akan ditanya apakah ingin menyimpan solusi:
  ```
  Apakah anda ingin menyimpan solusi? (ya/tidak)
  ```
  Jika **"ya"**, program akan menyimpan output dalam folder `test/` sebagai:
  - **File teks**: `test/[nama_file]_output.txt`
  - **Gambar puzzle** (jika mendukung visualisasi): `test/[nama_file].png`

---

### Contoh Alur Eksekusi
1. **Menjalankan program**
   ```sh
   java -cp bin Main
   ```
2. **Masukkan file input**
   ```
   Enter filename (including .txt): ./test_input/test1.txt
   ```
3. **Hasil solusi (jika ditemukan)**
   ```
   AGGGD  
   AABDD  
   CCBBF  
   CEEFF  
   EEEFF  
   
   Waktu pencarian: 604 ms  
   Banyak kasus yang ditinjau: 7387  
   Apakah anda ingin menyimpan solusi? (ya/tidak)  
   ```
4. **Jika memilih menyimpan solusi** → File output tersimpan di `test_output/`.


## Tentang Author
Program ini dibuat oleh Asybel Bintang (NIM: 15223011)
