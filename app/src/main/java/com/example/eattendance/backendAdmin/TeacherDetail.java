package com.example.eattendance.backendAdmin;

import com.google.firebase.database.DatabaseReference;

public class TeacherDetail {
    String name, password;
    DatabaseReference classes;

    public TeacherDetail(String username, String password, DatabaseReference classes) {
        this.name = username;
        this.password = password;
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
