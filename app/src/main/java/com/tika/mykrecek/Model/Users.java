package com.tika.mykrecek.Model;

public class Users {
    private String nama, telepon, password;

    public Users ()
    {

    }

    public Users(String nama, String telepon, String password) {
        this.nama = nama;
        this.telepon = telepon;
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
