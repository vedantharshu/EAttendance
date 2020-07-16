package com.example.eattendance.teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eattendance.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AttendanceListActivity extends AppCompatActivity {

    private static final String TAG="AttendanceListActivity";
    ListView listView;
    Button submit;
    AttendanceListAdapter adapter;
    private StringBuffer present;
    private StringBuffer absent;
    private int presentCount;
    private String class_value;
    private String uname;
    private String adminID;
    ArrayList<AttendanceItem> studentList;
    private FirebaseDatabase database;
    private DatabaseReference myRef,myRef2;
    private String schoolID;
    private String rollNo;
    private ArrayList<String> IDs;
    private ArrayList<String> pres_abs;
    private String date,subject,lecture;
    private boolean dateExists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);
        Log.d(TAG,"AttendanceListActivity: Started");


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                adminID=null;
                class_value=null;
                date=null;
                subject=null;
                lecture=null;
            } else {
                adminID=extras.getString("adminID");
                class_value=extras.getString("class");
                date=extras.getString("date");
                subject=extras.getString("subject");
                lecture=extras.getString("lecture");
            }
        } else {

            adminID= (String) savedInstanceState.getSerializable("adminID");
            class_value= (String) savedInstanceState.getSerializable("class");
            date= (String) savedInstanceState.getSerializable("date");
            subject= (String) savedInstanceState.getSerializable("subject");
            lecture= (String) savedInstanceState.getSerializable("lecture");


        }
        String[] x=adminID.split("_");
        schoolID=x[1];

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Admins").child(adminID);

        listView=(ListView)findViewById(R.id.attendanceLV);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                studentList=new ArrayList<>();
                int i=1;
                for(DataSnapshot stu:dataSnapshot.child("Students").child(class_value).getChildren())
                {
                    String[] sn=stu.child("username").getValue().toString().split("\\s",2);
                    studentList.add(new AttendanceItem(Integer.toString(i),sn[1],false));
                            i++;
                }
                adapter=new AttendanceListAdapter(AttendanceListActivity.this,R.layout.activity_attendance_list,studentList);
                listView.setAdapter(adapter);
                Log.d(TAG, "StudentList got........:)");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Toast.makeText(AttendanceListActivity.this, "database error", Toast.LENGTH_LONG).show();
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        presentCount=0;

        present = new StringBuffer();
        present.append("Present:\n");

        absent = new StringBuffer();
        absent.append("Present:\n");

        submit=(Button)findViewById(R.id.submit_attenBTN);

        // SUBMIT BUTTON

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IDs=new ArrayList<>();
                pres_abs=new ArrayList<>();


                List<AttendanceItem> atList =adapter.attendanceList;
                for(int i=0;i<atList.size();i++){
                    AttendanceItem attendance = atList.get(i);
                    if(attendance.isSelected()){
                        present.append("\n" + attendance.getName());
                        presentCount++;
                        rollNo=schoolID+"_ST_"+class_value+"_"+(attendance.getRollno());
                        IDs.add(rollNo);
                        pres_abs.add("present");
                    }
                    else
                    {
                        absent.append("\n"+attendance.getName());
                        rollNo=schoolID+"_ST_"+class_value+"_"+(attendance.getRollno());
                        IDs.add(rollNo);
                        pres_abs.add("absent");
                    }

                }
                present.append("\nTotal Present: "+String.valueOf(presentCount));


                myRef2=myRef.child("Attendance").child(date).child(class_value).child(lecture).child(subject);
                myRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for(int i=0;i<IDs.size();i++)
                        {
                            myRef2.child(IDs.get(i)).setValue(pres_abs.get(i));
                        }

                        return;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //Toast.makeText(getApplicationContext(),present, Toast.LENGTH_LONG).show();

                finish();
            }
        });




    }
}