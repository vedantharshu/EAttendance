package com.example.eattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eattendance.student.StudentActivity;
import com.example.eattendance.teacher.TeacherActivity;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    Button login;
    String item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        spinner=(Spinner)findViewById(R.id.role_spinner);
        login=(Button)findViewById(R.id.loginBTN);
        spinner.setOnItemSelectedListener(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(item.equals("Admin"))
                    startActivity(new Intent(LoginActivity.this,AdminActivity.class));
                else if(item.equals("Teacher"))
                    startActivity(new Intent(LoginActivity.this, TeacherActivity.class));
                else if(item.equals("Student"))
                    startActivity(new Intent(LoginActivity.this, StudentActivity.class));

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}