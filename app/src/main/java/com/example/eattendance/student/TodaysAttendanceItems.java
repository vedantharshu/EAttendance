package com.example.eattendance.student;

public class TodaysAttendanceItems {

    String AttendanceFlag;
    String Subject;
    String sNo;

    public TodaysAttendanceItems(String sNo, String Subject , String AttendanceFlag){
        this.AttendanceFlag = AttendanceFlag;
        this.sNo = sNo;
        this.Subject = Subject;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public String getAttendanceFlag() {
        return AttendanceFlag;
    }

    public void setAttendanceValue(String AttendanceFlag) {
        this.AttendanceFlag = AttendanceFlag;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }
}
