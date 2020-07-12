package com.example.eattendance.backendAdmin;

public class StudentDetail {
    String username, password;
    int absent, present;

    public StudentDetail(String username, String password, int absent, int present) {
        this.username = username;
        this.password = password;
        this.absent = absent;
        this.present = present;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAbsent() {
        return absent;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }
}
