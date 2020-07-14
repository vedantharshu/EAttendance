package com.example.eattendance.admin.teachers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.eattendance.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AssignClasses extends AppCompatActivity {
    EditText teacherId;
    Button search;
    DatabaseReference dref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_classes);
//
//        teacherId = findViewById(R.id.idTeacherSearch);
//        search = findViewById(R.id.idSearchTeacherButton);
//        dref = FirebaseDatabase.getInstance().getReference("Admins").child("AD_201").child("Teachers").child(teacherId.getText().toString().trim()).child("Classes");
    }
}