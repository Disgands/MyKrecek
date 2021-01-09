package com.tika.mykrecek.Model;

public class NewOrders {
    private String Pembayaran , TotalHarga, alamat, kabupaten, nama, phone, status, tanggal, waktu;

    public NewOrders() {
    }

    public NewOrders(String pembayaran, String totalHarga, String alamat, String kabupaten, String nama, String phone, String status, String tanggal, String waktu) {
        this.Pembayaran = pembayaran;
        this.TotalHarga = totalHarga;
        this.alamat = alamat;
        this.kabupaten = kabupaten;
        this.nama = nama;
        this.phone = phone;
        this.status = status;
        this.tanggal = tanggal;
        this.waktu = waktu;
    }

    public String getPembayaran() {
        return Pembayaran;
    }

    public void setPembayaran(String pembayaran) {
        Pembayaran = pembayaran;
    }

    public String getTotalHarga() {
        return TotalHarga;
    }

    public void setTotalHarga(String totalHarga) {
        TotalHarga = totalHarga;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKabupaten() {
        return kabupaten;
    }

    public void setKabupaten(String kabupaten) {
        this.kabupaten = kabupaten;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
