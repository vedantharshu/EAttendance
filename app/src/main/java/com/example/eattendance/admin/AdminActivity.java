package com.example.eattendance.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.eattendance.R;

import com.example.eattendance.admin.students.AddStudent;
import com.example.eattendance.admin.students.EditStudent;
import com.example.eattendance.admin.teachers.AddTeachers;
import com.example.eattendance.admin.teachers.AssignClasses;


public class AdminActivity extends AppCompatActivity {
    Button addStudents;
    Button addTeachers;
    Button editStudents;
    Button assignClasses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        addStudents = findViewById(R.id.addStudents);

        addTeachers=findViewById(R.id.addTeachers);
        assignClasses = findViewById(R.id.assignClassesbtn);
        editStudents = findViewById(R.id.editStudents);

        addStudents.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent i = new Intent(AdminActivity.this, AddStudent.class);
              startActivity(i);
           }
        });

        addTeachers=findViewById(R.id.addTeachers);


        addTeachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this, AddTeachers.class);
                startActivity(i);
            }
        });
        assignClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this , AssignClasses.class);
                startActivity(i);
            }
        });

        editStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this , EditStudent.class);
                startActivity(i);
            }
        });
    }

}