package com.example.eattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddStudent extends AppCompatActivity {
    Button submit;
    EditText addStudentName;
    EditText addStudentClass;
    EditText addStudentSection;
    EditText addStudentRoll;
    String uniqueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        submit = (Button)findViewById(R.id.idAddStudentSubmit);
        addStudentClass = (EditText)findViewById(R.id.idStudentClass);
        addStudentName = (EditText)findViewById(R.id.idStudentFullName);
        addStudentSection = (EditText)findViewById(R.id.idStudentSection);
        addStudentRoll = (EditText)findViewById(R.id.idStudentRollNo);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uniqueId = addStudentClass.getText().toString() + "."+ addStudentSection.getText().toString() + "."+ addStudentRoll.getText().toString();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Student with Name : " + addStudentName.getText().toString() + " and UniqueId : " + uniqueId + " is added successfully.",
                        Toast.LENGTH_SHORT);

                toast.show();
            }
        });
    }
}