package com.tika.mykrecek.Model;

public class Product {
    private String alamat, date, harga, image, nama, pid, time;

    public Product() {
    }

    public Product(String alamat, String date, String harga, String image, String nama, String pid, String time) {
        this.alamat = alamat;
        this.date = date;
        this.harga = harga;
        this.image = image;
        this.nama = nama;
        this.pid = pid;
        this.time = time;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
