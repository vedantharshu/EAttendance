package com.example.eattendance.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eattendance.R;
import com.example.eattendance.backendAdmin.TeacherDetail;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTeachers extends AppCompatActivity {
    EditText teacher_name, teacher_code;
    Button adding_teacher;
    String teacherCode="", teacherName="";
    DatabaseReference mref, mref1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teachers);

        adding_teacher = findViewById(R.id.adding_teacher);
        teacher_name = findViewById(R.id.teacher_name);
        teacher_code = findViewById(R.id.teacher_code);

        mref = FirebaseDatabase.getInstance().getReference("teacher");

        adding_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(teacher_name.getText().toString().trim().compareTo("")==0){
                    teacher_name.setError("This Field Cannot Be Empty!!");
                }
                else if(teacher_code.getText().toString().trim().compareTo("")==0){
                    teacher_name.setError("This Field Cannot Be Empty!!");
                }
                else {
                        teacherName = teacher_name.getText().toString().trim();
                        teacherCode = teacher_code.getText().toString().trim();
                        TeacherDetail td = new TeacherDetail(teacherName, teacherName + teacherCode);
                        mref1 = mref.child(teacherName + teacherCode);
                        mref1.setValue(td);

                        Toast.makeText(getApplicationContext(),
                            "Teacher Added Successfully", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}