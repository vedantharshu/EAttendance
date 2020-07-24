package com.example.eattendance.backendAdmin;

import com.google.firebase.database.DatabaseReference;

public class TeacherDetail {
    String Name, Password, Classes;

    public TeacherDetail(){

    }
    public TeacherDetail(String Name, String Password, String Classes) {
        this.Name = Name;
        this.Password = Password;
        this.Classes = null;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = Name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getClasses(){
        return Classes;
    }

    public void setClasses(String Class, String Subject){
        this.Classes = this.getClasses() + " "+ Class + "_" + Subject;
    }
}
