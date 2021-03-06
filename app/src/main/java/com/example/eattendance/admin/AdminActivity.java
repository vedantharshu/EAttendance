package com.example.eattendance.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eattendance.LoginActivity;
import com.example.eattendance.R;

import com.example.eattendance.admin.students.AddStudent;
import com.example.eattendance.admin.students.EditStudent;
import com.example.eattendance.admin.teachers.AddTeachers;
import com.example.eattendance.admin.teachers.AssignClasses;
import com.example.eattendance.admin.teachers.RemoveTeacher;
import com.example.eattendance.authentication.AdminAuth;
import com.google.firebase.auth.FirebaseAuth;


public class AdminActivity extends AppCompatActivity {
    Button addStudents;
    Button addTeachers;
    Button editStudents;
    Button assignClasses;
    Button removeTeacherbtn;
    Button signout;
    String code;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth=FirebaseAuth.getInstance();
        addStudents = findViewById(R.id.addStudents);
        signout = findViewById(R.id.logout);
        addTeachers=findViewById(R.id.addTeachers);
        assignClasses = findViewById(R.id.assignClassesbtn);
        editStudents = findViewById(R.id.editStudents);
        removeTeacherbtn = findViewById(R.id.removeTeacherbtn);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            code = extras.getString("code");
        }

        addStudents.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent i = new Intent(AdminActivity.this, AddStudent.class);
              i.putExtra("code",code);
              startActivity(i);
           }
        });

        addTeachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this, AddTeachers.class);
                i.putExtra("code",code);
                startActivity(i);
            }
        });
        assignClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this , AssignClasses.class);
                i.putExtra("code",code);
                startActivity(i);
            }
        });

        editStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this , EditStudent.class);
                i.putExtra("code",code);
                startActivity(i);
            }
        });
        removeTeacherbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this , RemoveTeacher.class);
                i.putExtra("code",code);
                startActivity(i);
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent i = new Intent(AdminActivity.this , AdminAuth.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.signOut();
    }

    @Override
    public void onBackPressed() {
        final androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("You will be signed out.");
        builder.setMessage("Do you want to exit? ");
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mAuth.signOut();
                startActivity(new Intent(AdminActivity.this, AdminAuth.class));
                finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}