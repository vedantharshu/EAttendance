package com.example.eattendance.teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eattendance.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class TeacherActivity extends AppCompatActivity {
    private Spinner classSpinner,subjectSpinner;
    ArrayList<String> classes;
    ArrayList<String> subjects;
    private String uname;
    private String adminID;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String TAG="Teacher_Activity: ";

    Button mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);










        classSpinner = (Spinner) findViewById(R.id.class_spinner);

        subjectSpinner = (Spinner) findViewById(R.id.subject_spinner);

        classes=new ArrayList<>();


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                uname= null;
                adminID=null;
            } else {
                uname= extras.getString("user_name");
                adminID=extras.getString("adminID");
            }
        } else {
            uname= (String) savedInstanceState.getSerializable("user_name");
            adminID= (String) savedInstanceState.getSerializable("adminID");

        }
        Toast.makeText(this, uname+" "+adminID, Toast.LENGTH_LONG).show();


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Admins").child(adminID).child("Teachers").child(uname);

        myRef.child("Classes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    classes=new ArrayList<>();
                    for(DataSnapshot d : dataSnapshot.getChildren()) {
                        classes.add(d.getKey());

                    }
                }
                Log.d(TAG, "Value retreived..");
                ArrayAdapter<String> class_adapter = new ArrayAdapter<String>(
                        TeacherActivity.this,
                        android.R.layout.simple_spinner_item,
                        classes
                );
                class_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                classSpinner.setAdapter(class_adapter);


                classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        Object item = parent.getItemAtPosition(position);
                        String cl=item.toString();
                        subjects=new ArrayList<>();
                        String[] subs=dataSnapshot.child(cl).getValue().toString().split(" ");

                        for(String x:subs)
                        {
                            subjects.add(x);
                        }
                        ArrayAdapter<String> subject_adapter = new ArrayAdapter<String>(
                                TeacherActivity.this,
                                android.R.layout.simple_spinner_item,
                                subjects
                        );
                        subject_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subjectSpinner.setAdapter(subject_adapter);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                //Toast.makeText(TeacherActivity.this, classes.get(0), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Could not get value");
            }
        });










        mark=(Button)findViewById(R.id.mark_attendanceBTN);
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherActivity.this, AttendanceListActivity.class));
            }
        });




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