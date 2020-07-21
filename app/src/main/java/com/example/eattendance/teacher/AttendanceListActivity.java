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
import com.example.eattendance.backendAdmin.StudentDetail;
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
    private DatabaseReference myRef,myRef1,myRef2;
    private String schoolID;
    private String rollNo;
    private ArrayList<String> IDs;
    private ArrayList<String> pres_abs;
    private String date,subject,lecture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);

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
                for(DataSnapshot stu:dataSnapshot.child("Students").child(class_value).getChildren())
                {
                    String[] sn=stu.child("username").getValue().toString().split(" ");
                    studentList.add(new AttendanceItem(sn[0],sn[1],false));
                }
                adapter=new AttendanceListAdapter(AttendanceListActivity.this,R.layout.activity_attendance_list,studentList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
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

                myRef2 = myRef.child("Students").child(class_value);

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
                myRef1=myRef.child("Attendance").child(date).child(class_value).child(lecture+"_"+subject);
                        for(int i=0;i<IDs.size();i++) {
                            myRef1.child(IDs.get(i)).setValue(pres_abs.get(i));
                        }

                myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int in=0;
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            StudentDetail ob = data.getValue(StudentDetail.class);
                            AttendanceItem at =studentList.get(in);
                            ++in;
                            if(at.isSelected()){
                                int n = ob.getPresent();
                                    myRef2.child(data.getKey()).child("present").setValue(n+1);

                            }
                            else if (!at.isSelected()) {
                                int n = ob.getAbsent();
                                    myRef2.child(data.getKey()).child("absent").setValue(n+1);

                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                finish();
            }
        });
    }
}