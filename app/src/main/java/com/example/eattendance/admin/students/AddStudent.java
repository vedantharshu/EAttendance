package com.example.eattendance.admin.students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eattendance.R;
import com.example.eattendance.backendAdmin.StudentDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddStudent extends AppCompatActivity {
    Button submit;
    EditText addStudentName;
    Spinner addStudentClass;
    Spinner addStudentSection;
    Spinner addStudentRoll;
    String standard,code, classValue, sectionValue, rollValue, sid, sname;
    DatabaseReference mref1, mref2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        submit = findViewById(R.id.idAddStudentSubmit);
        addStudentClass = findViewById(R.id.idStudentClass);
        addStudentName = findViewById(R.id.idStudentFullName);
        addStudentSection = findViewById(R.id.idStudentSection);
        addStudentRoll = findViewById(R.id.idStudentRoll);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            code = extras.getString("code");
        }
        Log.d("TAG2", "onCreate: addstu"+code);
        addStudentClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classValue = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addStudentSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sectionValue = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addStudentRoll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rollValue = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(addStudentName.getText().toString().trim().equals("")){
                        addStudentName.setError("Name Cannot Be Empty!!");
                    }
                    else{
                        sname = addStudentName.getText().toString().trim();
                        standard = classValue+sectionValue;
                        //creating child for node student
                        mref1 = FirebaseDatabase.getInstance().getReference("Admins").child("AD_"+code).child("Students").child(standard);
                        sid = code+"_ST_"+standard+"_"+rollValue;
                        mref1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(sid)){
                                    Toast.makeText(AddStudent.this, "Roll Number Already Assigned To "+dataSnapshot.child(sid).child("username").getValue().toString(),Toast.LENGTH_LONG).show();
                                }
                                else {
                                    mref2 = mref1.child(code+"_ST_"+standard+"_"+rollValue);
                                    //adding student detail and the object will be passed to mref2.
                                    StudentDetail st = new StudentDetail(rollValue+" " +sname,
                                            sname + rollValue,
                                            0,
                                            0);
                                    mref2.setValue(st);
                                    Toast.makeText(getApplicationContext(),
                                            "Student Added Successfully", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        addStudentName.setText("");
                    }
            }
        });
    }
}