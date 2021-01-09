package com.tika.mykrecek.Model;

public class Cart {
    private String pid,nama,harga,tanggal,waktu,jumlah;

    public Cart() {
    }

    public Cart(String pid, String nama, String harga, String tanggal, String waktu, String jumlah) {
        this.pid = pid;
        this.nama = nama;
        this.harga = harga;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.jumlah = jumlah;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }
}
