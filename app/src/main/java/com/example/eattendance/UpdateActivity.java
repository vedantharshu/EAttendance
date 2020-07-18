package com.example.eattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eattendance.backendAdmin.StudentDetail;
import com.example.eattendance.teacher.AttendanceItem;
import com.example.eattendance.teacher.AttendanceListActivity;
import com.example.eattendance.teacher.AttendanceListAdapter;
import com.example.eattendance.teacher.TeacherActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends AppCompatActivity {
    private String date,subject,lecture, adminID,class_value, name, st;
    private ListView listView;
    private Button submit;
    private AttendanceListAdapter adapter = null;
    private ArrayList<AttendanceItem> studentList;
    private DatabaseReference mref, mref1,mref2;
    private String stuname;
    private String[] sn;
    private int i;
    private boolean mark;
    private StringBuffer responseText = new StringBuffer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                adminID=extras.getString("adminID");
                class_value=extras.getString("class");
                date=extras.getString("date");
                subject=extras.getString("subject");
                lecture=extras.getString("lecture");
            }
        listView = findViewById(R.id.attendanceLv);
        mref = FirebaseDatabase.getInstance().getReference("Admins").child(adminID);
        mref1 = FirebaseDatabase.getInstance().getReference("Admins").child(adminID).child("Attendance").child(date).child(class_value).child(lecture).child(subject);
        studentList = new ArrayList<>();

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                studentList.clear();
                for(DataSnapshot data: dataSnapshot.child("Attendance").child(date).child(class_value).child(lecture).child(subject).getChildren()){
                    if(data.getValue().toString().equals("absent")){
                        mark = false;
                    }
                    else if(data.getValue().toString().equals("present")){
                        mark = true;
                    }
                    st = data.getKey();

                    stuname=dataSnapshot.child("Students").child(class_value).child(st).child("username").getValue().toString();
                    sn=stuname.split(" ");
                    //Toast.makeText(UpdateActivity.this, sn[1], Toast.LENGTH_LONG).show();

                    studentList.add(new AttendanceItem(sn[0],sn[1],mark));
                }
                adapter=new AttendanceListAdapter(UpdateActivity.this,R.layout.activity_attendance_list,studentList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        checkButtonClick();
    }

    private void checkButtonClick() {
        submit = findViewById(R.id.submit_attenBTN);
        mref2 = mref.child("Students").child(class_value);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");
                for(int i=0;i<studentList.size();i++){
                    AttendanceItem attendance = studentList.get(i);
                    if(attendance.isSelected()){
                        mref1.child("201_ST_"+class_value+"_"+attendance.getRollno()).setValue("present");
                        responseText.append("\n" + attendance.getName());
                    }
                    else if(!attendance.isSelected()){
                        mref1.child("201_ST_"+class_value+"_"+attendance.getRollno()).setValue("absent");

                        mref2.child("201_ST_"+class_value+"_"+attendance.getRollno()).child("absent").setValue(i+10);
                    }
                }

                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();*/

                mref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int in=0;
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            StudentDetail ob = data.getValue(StudentDetail.class);
                            AttendanceItem at =studentList.get(in);
                            ++in;
                            if(at.isSelected()){
                                int n = ob.getPresent();
                                mref2.child(data.getKey()).child("present").setValue(++n);
                            }
                            else if (!at.isSelected()){
                                int n = ob.getAbsent();
                                mref2.child(data.getKey()).child("absent").setValue(++n);
                            }

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
