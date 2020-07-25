package com.example.eattendance.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eattendance.LoginActivity;
import com.example.eattendance.R;
import com.example.eattendance.admin.teachers.AssignClassAdapter;
import com.example.eattendance.admin.teachers.AssignClasses;
import com.example.eattendance.backendAdmin.StudentDetail;
import com.example.eattendance.teacher.TeacherActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    DatePickerDialog picker;
    Button chooseDate;
    TextView welcome;
    CircularProgressBar circularProgressBar;
    DatabaseReference mref, mref1;
    int present , absent;
    float avg;
    TextView attendanceValue;
    Button todaysAttendancebtn, checkAttendancebtn, logout;
    ArrayList<TodaysAttendanceItems> TodaysAttendanceList, CheckAttendanceList;
    todaysAttendanceAdapter TodaysAttendanceAdapter = null;
    ListView mList,checkAttendancelist;
    String btnFlag,toCheckDate,btnFlag2, uname, standard, adminID, code;
    boolean clicked = false;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        circularProgressBar = findViewById(R.id.circularProgressBar);
        attendanceValue = findViewById(R.id.attendanceValue);
        todaysAttendancebtn = findViewById(R.id.todaysAttendancebtn);
        mList = findViewById(R.id.ListViewTodaysAttendance);
        checkAttendancelist = findViewById(R.id.checkAttendancelist);
        welcome = findViewById(R.id.welcome);
        logout = findViewById(R.id.logout);

        chooseDate = findViewById(R.id.chooseDate);
        checkAttendancebtn = findViewById(R.id.checkAttendancebtn);

        btnFlag  = "plus";
        btnFlag2 = "plus";
        TodaysAttendanceList = new ArrayList<>();
        CheckAttendanceList = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            adminID=extras.getString("adminID");//AD_210
            uname=extras.getString("studId");//210_ST_2A_1
            standard=extras.getString("class");//2A
            code = adminID.split("_")[1];//210
        }

        mref = FirebaseDatabase.getInstance().getReference("Admins").child(adminID).child("Students").child(standard).child(uname);

        //logout student
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentActivity.this, LoginActivity.class);
                i.putExtra("item","Student");
                startActivity(i);
                finish();
            }
        });

        //CardView 1
        // getting absent and present values and showing to progress bar

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StudentDetail sd = dataSnapshot.getValue(StudentDetail.class);
                welcome.setText("WELCOME "+sd.getUsername().split(" ")[1].toUpperCase());
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

                calendar = Calendar.getInstance();
                dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                date1 = dateFormat.format(calendar.getTime());
                if(date1.charAt(3)=='0'){
                    dateFormat = new SimpleDateFormat("dd-M-yyyy");
                    date1 = dateFormat.format(calendar.getTime());
                }

                if(btnFlag.equals("plus")){
                    btnFlag2="minus";
                    showAttendance(date1, mList, TodaysAttendanceList);
                }
                else if(btnFlag.equals("minus")){
                    ViewGroup.LayoutParams p = mList.getLayoutParams();
                    p.height = 0;
                    mList.setLayoutParams(p);
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
                if(!btnFlag2.equals("minus")) {
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    picker = new DatePickerDialog(StudentActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    monthOfYear = monthOfYear + 1;
                                    toCheckDate = (dayOfMonth + "-" + monthOfYear + "-" + year);
                                    clicked = true;
                                }
                            }, year, month, day);
                    picker.show();
                }
            }
        });

        checkAttendancebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnFlag2.equals("plus")) {
                    btnFlag = "minus";
                    if (clicked) {
                        showAttendance(toCheckDate, checkAttendancelist, CheckAttendanceList);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Click on calendar to choose a date", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (btnFlag2.equals("minus")) {
                    ViewGroup.LayoutParams p = checkAttendancelist.getLayoutParams();
                    p.height = 0;
                    checkAttendancelist.setLayoutParams(p);
                    clicked = false;
                    btnFlag2 = "plus";
                    CheckAttendanceList.clear();
                    TodaysAttendanceAdapter = new todaysAttendanceAdapter(StudentActivity.this, R.layout.todays_attendance_list, CheckAttendanceList);
                    checkAttendancelist.setAdapter(TodaysAttendanceAdapter);
                    checkAttendancebtn.setBackgroundResource(R.drawable.plus);
                }
            }
        });

    }

    void showAttendance(final String date, final ListView l, final ArrayList list){

        mref1 = FirebaseDatabase.getInstance().getReference("Admins").child(adminID).child("Attendance");

        mref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                int i = 1;
                if(dataSnapshot.hasChild(date)) {
                    if(btnFlag2.equals("plus")){
                        btnFlag = "plus";
                        TodaysAttendanceList.clear();
                        TodaysAttendanceAdapter = new todaysAttendanceAdapter(StudentActivity.this, R.layout.todays_attendance_list, TodaysAttendanceList);
                        mList.setAdapter(TodaysAttendanceAdapter);
                        todaysAttendancebtn.setBackgroundResource(R.drawable.plus);
                        ViewGroup.LayoutParams p2 = mList.getLayoutParams();
                        p2.height = 0;
                        mList.setLayoutParams(p2);
                        checkAttendancebtn.setBackgroundResource(R.drawable.minus);
                        btnFlag2 = "minus";
                        ViewGroup.LayoutParams p = checkAttendancelist.getLayoutParams();
                        p.height = 500;
                        checkAttendancelist.setLayoutParams(p);
                    }
                    else if(btnFlag.equals("plus")){
                        CheckAttendanceList.clear();
                        TodaysAttendanceAdapter = new todaysAttendanceAdapter(StudentActivity.this, R.layout.todays_attendance_list, CheckAttendanceList);
                        checkAttendancelist.setAdapter(TodaysAttendanceAdapter);
                        checkAttendancebtn.setBackgroundResource(R.drawable.plus);
                        ViewGroup.LayoutParams p1 = checkAttendancelist.getLayoutParams();
                        p1.height = 0;
                        checkAttendancelist.setLayoutParams(p1);
                        btnFlag2= "plus";
                        ViewGroup.LayoutParams p = mList.getLayoutParams();
                        p.height = 500;
                        mList.setLayoutParams(p);
                        todaysAttendancebtn.setBackgroundResource(R.drawable.minus);
                        btnFlag = "minus";
                    }
                    for (DataSnapshot data : dataSnapshot.child(date).child(standard).getChildren()) {
                        String lec = data.getKey();
                        String flag = data.child(uname).getValue().toString();
                        list.add(new TodaysAttendanceItems(String.valueOf(i++), lec.split("_")[2], flag));
                    }
                    TodaysAttendanceAdapter = new todaysAttendanceAdapter(StudentActivity.this, R.layout.todays_attendance_list, list);
                    l.setAdapter(TodaysAttendanceAdapter);
                }
                else{
                    Toast.makeText(StudentActivity.this, "No attendance record available for this date", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void changeAttendance(float average){
        attendanceValue = findViewById(R.id.attendanceValue);
        attendanceValue.setText(average+"%");
        circularProgressBar = findViewById(R.id.circularProgressBar);
        circularProgressBar.setProgressWithAnimation(average, (long) 1000);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("This will exit you.");
        builder.setMessage("Do you want to exit? ");
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(StudentActivity.this, LoginActivity.class);
                i.putExtra("item","Student");
                startActivity(i);
                finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}


