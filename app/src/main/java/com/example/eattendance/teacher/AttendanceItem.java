package com.example.eattendance.teacher;

import android.widget.CheckBox;

public class AttendanceItem {
    boolean selected=false;
    String name = null;
    String rollno=null;

    public AttendanceItem(String rollno, String name,boolean selected) {
        this.selected = selected;
        this.name = name;
        this.rollno=rollno;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getRollno()
    {
        return rollno;
    }
    public void setRollno(String rollno)
    {
        this.rollno=rollno;
    }
}
