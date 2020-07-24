package com.example.eattendance.authentication;

public class Admin {
    String schoolName, password, email;


    public Admin(String schoolname, String password, String email) {
        this.schoolName = schoolname;
        this.password = password;
        this.email = email;
    }

    public String getSchoolname() {
        return schoolName;
    }

    public void setSchoolname(String schoolname) {
        this.schoolName = schoolname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
