package com.example.eattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class AddTeachers extends AppCompatActivity {
    Spinner subject_spinner, class_spinner, section_spinner;
    EditText teacher_code;
    Button adding_teacher;
    String subject="", standard="", section="", teacher="";
    String []a = new String[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teachers);
        subject_spinner = findViewById(R.id.subject_spinner);
        class_spinner = findViewById(R.id.class_spinner);
        section_spinner =findViewById(R.id.section_spinner);
        teacher_code = findViewById(R.id.teacher_code);
        adding_teacher = findViewById(R.id.adding_teacher);

        subject_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subject = parent.getItemAtPosition(position).toString();
                a[0] = subject;
                Toast.makeText(parent.getContext(), "Selected subject: " + subject, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        class_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                standard = parent.getItemAtPosition(position).toString();
                a[1]=standard;
                Toast.makeText(parent.getContext(), "Selected: class " + standard, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        section_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                section = parent.getItemAtPosition(position).toString();
                a[2]=section;
                Toast.makeText(parent.getContext(), "Selected section: " + section, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        adding_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");
                teacher = teacher_code.getText().toString().trim();
                if(teacher.compareTo("")==0){
                    teacher_code.setError("This Field Cannot Be Empty!!");
                }
                a[3]=teacher;
                for(int i=0;i<4;i++){
                        responseText.append("\n" + a[i]);
                    }

                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();
            }
        });
    }

}