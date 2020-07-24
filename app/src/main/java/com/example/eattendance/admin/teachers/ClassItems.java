package com.example.eattendance.admin.teachers;

public class ClassItems {

    String className;
    String Subject;
    String sNo;

    public ClassItems(String className, String Subject , String sNo){
        this.className = className;
        this.sNo = sNo;
        this.Subject = Subject;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }
}