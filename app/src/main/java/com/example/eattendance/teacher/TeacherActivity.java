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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eattendance.LoginActivity;
import com.example.eattendance.R;
import com.example.eattendance.UpdateActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class TeacherActivity extends AppCompatActivity {
    private Spinner classSpinner,subjectSpinner,lectureSpinner;
    private DatePicker datePicker;
    ArrayList<String> classes;
    ArrayList<String> subjects;
    ArrayList<String> lectures;
    private String uname;
    private String adminID;
    private FirebaseDatabase database;
    private DatabaseReference myRef,myRef1,myRef2;
    private String TAG="Teacher_Activity: ";
    private String cl;
    private String selectedSub;
    private String selectedLec;
    private  String date;

    private Button mark;
    private Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);




        classSpinner = (Spinner) findViewById(R.id.class_spinner);

        subjectSpinner = (Spinner) findViewById(R.id.subject_spinner);

        lectureSpinner=(Spinner)findViewById(R.id.lecture_spinner);

        datePicker=(DatePicker)findViewById(R.id.datepicker_teacher);



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
        //Toast.makeText(this, uname+" "+adminID, Toast.LENGTH_LONG).show();

        lectures=new ArrayList<>();
        lectures.add("1");
        lectures.add("2");
        lectures.add("3");
        lectures.add("4");
        lectures.add("5");
        lectures.add("6");
        lectures.add("7");
        lectures.add("8");
        lectures.add("9");
        lectures.add("10");

        ArrayAdapter<String> lecture_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                lectures
        );
        lecture_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lectureSpinner.setAdapter(lecture_adapter);




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
                        cl=item.toString();
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

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSub=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lectureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLec=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        myRef1=database.getReference("Admins").child(adminID);
        mark=(Button)findViewById(R.id.mark_attendanceBTN);
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date= datePicker.getDayOfMonth()+"-"+ (datePicker.getMonth() + 1)+"-"+datePicker.getYear();


                myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        if(dataSnapshot1.child("Attendance").child(date).child(cl).hasChild("Lec_"+selectedLec))
                        {
                            Toast.makeText(TeacherActivity.this, "Selected lecture has an entry.\nClick on Update to update.", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            //Toast.makeText(TeacherActivity.this, date+" "+cl+" "+selectedLec, Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(TeacherActivity.this, AttendanceListActivity.class);

                            intent.putExtra("adminID",adminID);
                            intent.putExtra("class",cl);
                            intent.putExtra("date",date);
                            intent.putExtra("subject",selectedSub);
                            intent.putExtra("lecture","Lec_"+selectedLec);
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





            }
        });

        myRef2=database.getReference("Admins").child(adminID);
        updateBtn=(Button)findViewById(R.id.update_attendanceBTN);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date= datePicker.getDayOfMonth()+"-"+ (datePicker.getMonth() + 1)+"-"+datePicker.getYear();
                Intent intent=new Intent(TeacherActivity.this, UpdateActivity.class);
                intent.putExtra("adminID",adminID);
                intent.putExtra("class",cl);
                intent.putExtra("date",date);
                intent.putExtra("subject",selectedSub);
                intent.putExtra("lecture","Lec_"+selectedLec);
                startActivity(intent);

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
                startActivity(new Intent(TeacherActivity.this, LoginActivity.class));
                finish();
            }
        });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}