package com.example.eattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {
    Button addStudents;
    Button addTeachers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        addStudents = findViewById(R.id.addStudents);
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
    }
}