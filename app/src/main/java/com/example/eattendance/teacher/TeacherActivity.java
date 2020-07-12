package com.example.eattendance.teacher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.eattendance.R;

import java.util.ArrayList;


public class TeacherActivity extends AppCompatActivity {
    private Spinner classSpinner,sectionSpinner,subjectSpinner;
    ArrayList<String> classes;
    ArrayList<String> sections;
    ArrayList<String> subjects;

    Button mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        classSpinner = (Spinner) findViewById(R.id.class_spinner);
        sectionSpinner = (Spinner) findViewById(R.id.section_spinner);
        subjectSpinner = (Spinner) findViewById(R.id.subject_spinner);

        classes=new ArrayList<>();
        sections=new ArrayList<>();
        subjects=new ArrayList<>();

        classes.add("1");
        classes.add("2");
        classes.add("3");

        sections.add("A");
        sections.add("B");
        sections.add("C");

        subjects.add("Physics");
        subjects.add("Chemistry");
        subjects.add("Maths");



        ArrayAdapter<String> class_adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                classes
        );
        class_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(class_adapter);

        ArrayAdapter<String> section_adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                sections
        );
        section_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(section_adapter);
        mark=(Button)findViewById(R.id.mark_attendanceBTN);
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherActivity.this, AttendanceListActivity.class));
            }
        });

        ArrayAdapter<String> subject_adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                subjects
        );
        subject_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(subject_adapter);


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
                finish();
            }
        });
        builder.show();
    }
}