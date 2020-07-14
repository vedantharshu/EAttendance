package com.example.eattendance.admin.students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eattendance.R;
import com.example.eattendance.backendAdmin.StudentDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditStudent extends AppCompatActivity {

    RemoveStudentAdapter studentAdapter = null;
    ListView lv;
    Button show;
    Button add;
    EditText name;
    Spinner spin1,spin2;
    DatabaseReference mref,mref1;
    LinearLayout newStd;
    String standard,section;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);
        show = findViewById(R.id.showStudents);
        add = findViewById(R.id.AddStudent);
        name = findViewById(R.id.studentName);
        spin1 = findViewById(R.id.spin1);
        spin2 = findViewById(R.id.spin2);
        newStd = findViewById(R.id.newStd);
        lv=findViewById(R.id.lv);
        view = findViewById(R.id.view);
        view.setVisibility(View.INVISIBLE);
        newStd.setVisibility(View.INVISIBLE);
        lv.setVisibility(View.INVISIBLE);
        //choosing class
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                standard = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //choosing section
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                section = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mref= FirebaseDatabase.getInstance().getReference("Admins").child("AD_201").child("Students");
        //Add Student to class
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().trim().length()==0){
                    name.setError("This field cannot be Empty!!");
                }
                else{
                    StudentDetail studentDetail = new StudentDetail(name.getText().toString().trim(),"123",0,0);
                    mref.child(standard+section).child(name.getText().toString().trim()).setValue(studentDetail);
                    Toast.makeText(getApplicationContext(),"Student added successfully!!",Toast.LENGTH_LONG).show();
                    name.setText("");
                }
            }
        });
        //View Student Of Class
        show.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(standard+section)){
                            showList();
                        }
                        else{
                            view.setVisibility(View.INVISIBLE);
                            newStd.setVisibility(View.INVISIBLE);
                            lv.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"CLass Does Not Exists",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    public void showList(){
        newStd.setVisibility(View.VISIBLE);
        lv.setVisibility(View.VISIBLE);
        view.setVisibility(View.VISIBLE);
        final ArrayList<RemoveStudent> studentList = new ArrayList<>();
        mref1=mref.child(standard+section);
        mref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    studentList.add(new RemoveStudent(data.getKey()));
                }
                studentAdapter= new RemoveStudentAdapter(getApplicationContext(), R.layout.edit_list_student,studentList);
                lv.setAdapter(studentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}