package com.example.eattendance.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eattendance.R;
import com.example.eattendance.admin.teachers.AssignClassAdapter;
import com.example.eattendance.admin.teachers.AssignClasses;
import com.example.eattendance.backendAdmin.StudentDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    DatePickerDialog picker;
    EditText eText;
    Button chooseDate;
    TextView tvw;
    CircularProgressBar circularProgressBar;
    DatabaseReference mref, mref1;
    int present , absent;
    float avg;
    TextView attendanceValue;
    Button todaysAttendancebtn, checkAttendancebtn;
    ArrayList<TodaysAttendanceItems> TodaysAttendanceList, CheckAttendanceList;
    todaysAttendanceAdapter TodaysAttendanceAdapter = null;
    String studentID = "210_ST_2A_2";
    ListView mList,checkAttendancelist;
    String btnFlag,toCheckDate="",btnFlag2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        circularProgressBar = findViewById(R.id.circularProgressBar);
        attendanceValue = findViewById(R.id.attendanceValue);
        todaysAttendancebtn = findViewById(R.id.todaysAttendancebtn);
        mList = findViewById(R.id.ListViewTodaysAttendance);
        checkAttendancelist = findViewById(R.id.checkAttendancelist);
        btnFlag  = "plus";
        btnFlag2 = "plus";
        TodaysAttendanceList = new ArrayList<>();
        CheckAttendanceList = new ArrayList<>();
        chooseDate = findViewById(R.id.chooseDate);
        checkAttendancebtn = findViewById(R.id.checkAttendancebtn);

        mref = FirebaseDatabase.getInstance().getReference("Admins").child("AD_210").child("Students").child("2A").child("210_ST_2A_2");


        //CardView 1
        // getting absent and present values and showing to progress bar

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StudentDetail sd = dataSnapshot.getValue(StudentDetail.class);
                present = sd.getPresent();
                absent = sd.getAbsent();
                avg = (present*100)/(absent+present);
                changeAttendance(avg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // CardView 2
        // Checking today's attendance in various subjects and showing in list view

        todaysAttendancebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnFlag.equals("plus")){
                    todaysAttendancebtn.setBackgroundResource(R.drawable.minus);
                    btnFlag = "minus";
                    showAttendance("19-7-2020", mList, TodaysAttendanceList);
                }
                else if(btnFlag.equals("minus")){
                    btnFlag = "plus";
                    TodaysAttendanceList.clear();
                    TodaysAttendanceAdapter = new todaysAttendanceAdapter(StudentActivity.this, R.layout.todays_attendance_list, TodaysAttendanceList);
                    mList.setAdapter(TodaysAttendanceAdapter);
                    todaysAttendancebtn.setBackgroundResource(R.drawable.plus);

                }
            }
        });


        // CardView 3
        // Checking attendance of a particular day in various subjects

        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(StudentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                toCheckDate =(String.valueOf(dayOfMonth) +"-"+ String.valueOf(monthOfYear) +"-"+ String.valueOf(year));
                                Toast.makeText(getApplicationContext(),
                                        toCheckDate, Toast.LENGTH_SHORT).show();
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        checkAttendancebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnFlag2.equals("plus")) {
                    if (!toCheckDate.equals("")) {
                        checkAttendancebtn.setBackgroundResource(R.drawable.minus);
                        btnFlag2 = "minus";
                        showAttendance(toCheckDate, checkAttendancelist, CheckAttendanceList);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please choose a date first", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (btnFlag2.equals("minus")) {
                    btnFlag2 = "plus";
                    CheckAttendanceList.clear();
                    TodaysAttendanceAdapter = new todaysAttendanceAdapter(StudentActivity.this, R.layout.todays_attendance_list, CheckAttendanceList);
                    checkAttendancelist.setAdapter(TodaysAttendanceAdapter);
                    checkAttendancebtn.setBackgroundResource(R.drawable.plus);

                }
            }
        });

    }

    void showAttendance(String date, final ListView l, final ArrayList list){

        mref1 = FirebaseDatabase.getInstance().getReference("Admins").child("AD_210").child("Attendance");
        mref1 = FirebaseDatabase.getInstance().getReference("Admins").child("AD_210").child("Attendance").child(date).child("2A");


        mref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list.clear();
                int i = 1;
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    String lec = data.getKey().toString();
                    String flag = data.child(studentID).getValue().toString();
                    list.add(new TodaysAttendanceItems(String.valueOf(i++), lec.split("_")[2], flag));
                }
                TodaysAttendanceAdapter = new todaysAttendanceAdapter(StudentActivity.this, R.layout.todays_attendance_list, list);
                l.setAdapter(TodaysAttendanceAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void changeAttendance(float average){
        attendanceValue = findViewById(R.id.attendanceValue);
        attendanceValue.setText(String.valueOf(average)+"%");
        circularProgressBar = findViewById(R.id.circularProgressBar);
        circularProgressBar.setProgressWithAnimation(average, (long) 1000);
    }
}


