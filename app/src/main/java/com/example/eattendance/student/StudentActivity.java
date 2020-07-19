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
    Button btnGet;
    TextView tvw;
    CircularProgressBar circularProgressBar;
    DatabaseReference mref;
    int present , absent;
    float avg;
    TextView attendanceValue;
    Button todaysAttendancebtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        circularProgressBar = findViewById(R.id.circularProgressBar);
        attendanceValue = findViewById(R.id.attendanceValue);
        todaysAttendancebtn = findViewById(R.id.todaysAttendancebtn);

        mref = FirebaseDatabase.getInstance().getReference("Admins").child("AD_201").child("Students").child("2A").child("201_ST_2A_1");

        // getting absent and present values

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

    }

    void changeAttendance(float average){
        attendanceValue = findViewById(R.id.attendanceValue);
        attendanceValue.setText(String.valueOf(average)+"%");
        circularProgressBar = findViewById(R.id.circularProgressBar);
        circularProgressBar.setProgressWithAnimation(average, (long) 1000);
    }
}








//................Jagrat's code...................................................//

//        tvw=(TextView)findViewById(R.id.textView1);
//        eText=(EditText) findViewById(R.id.editText1);
//        eText.setInputType(InputType.TYPE_NULL);
//        eText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar cldr = Calendar.getInstance();
//                int day = cldr.get(Calendar.DAY_OF_MONTH);
//                int month = cldr.get(Calendar.MONTH);
//                int year = cldr.get(Calendar.YEAR);
//                // date picker dialog
//                picker = new DatePickerDialog(StudentActivity.this,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                            }
//                        }, year, month, day);
//                picker.show();
//            }
//        });
//        btnGet=(Button)findViewById(R.id.button1);
//        btnGet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvw.setText("Selected Date: "+ eText.getText());
//            }
//        });