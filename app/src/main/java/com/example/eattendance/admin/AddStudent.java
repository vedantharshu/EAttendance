package com.example.eattendance.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eattendance.R;
import com.example.eattendance.backendAdmin.StudentDetail;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddStudent extends AppCompatActivity {
    Button submit;
    EditText addStudentName;
    EditText addStudentClass;
    EditText addStudentSection;
    EditText addStudentRoll;
    String standard;
    DatabaseReference mref,mref1, mref2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        //reference to database and creating node named --> student
        mref = FirebaseDatabase.getInstance().getReference("student");
        submit = findViewById(R.id.idAddStudentSubmit);
        addStudentClass = findViewById(R.id.idStudentClass);
        addStudentName = findViewById(R.id.idStudentFullName);
        addStudentSection = findViewById(R.id.idStudentSection);
        addStudentRoll = findViewById(R.id.idStudentRoll);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Handling Errors
                if(addStudentClass.getText().toString().trim().compareTo("")==0){
                    addStudentClass.setError("This field cannot be empty");
                }
                else if((addStudentName.getText().toString().trim().compareTo("")==0)){
                    addStudentName.setError("This Field Cannot Be Empty");
                }
                else if((addStudentSection.getText().toString().trim().compareTo("")==0)){
                    addStudentSection.setError("This Field Cannot Be Empty");
                }
                else if((addStudentRoll.getText().toString().trim().compareTo("")==0)){
                    addStudentRoll.setError("This Field Cannot Be Empty");
                }
                else{
                    standard = addStudentClass.getText().toString().trim() + "-" + addStudentSection.getText().toString().trim();
                    //creating child for node student
                    mref1 = FirebaseDatabase.getInstance().getReference("student").child(standard);

                    mref2 = mref1.child(addStudentName.getText().toString().trim() + addStudentRoll.getText().toString().trim());
                    //adding student detail and the object will be passed to mref2.
                    StudentDetail st = new StudentDetail(addStudentName.getText().toString().trim(),addStudentName.getText().toString().trim() + addStudentRoll.getText().toString().trim(),0,0);
                    mref2.setValue(st);

                    Toast.makeText(getApplicationContext(),
                            "Student Added Successfully", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}