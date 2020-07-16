package com.example.eattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eattendance.teacher.AttendanceItem;
import com.example.eattendance.teacher.AttendanceListActivity;
import com.example.eattendance.teacher.AttendanceListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends AppCompatActivity {
    private String date,subject,lecture, adminID,class_value, name, st;
    ListView listView;
    Button submit;
    AttendanceListAdapter adapter = null;
    ArrayList<AttendanceItem> studentList;
    DatabaseReference mref, mref1;
    int i;
    boolean mark;
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
        mref = FirebaseDatabase.getInstance().getReference("Admins").child(adminID).child("Attendance").child(date).child(class_value).child(lecture).child(subject);
        mref1 = FirebaseDatabase.getInstance().getReference("Admins").child(adminID).child("Students").child(class_value);
        studentList = new ArrayList<>();

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                studentList.clear();
                i =1;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getValue().toString().equals("absent")){
                        mark = false;
                    }
                    else if(data.getValue().toString().equals("present")){
                        mark = true;
                    }
                    st = data.getKey();
                    //String[] sn=name.split("\\s",2);
                    studentList.add(new AttendanceItem(Integer.toString(i),st,mark));
                    ++i;
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
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");
                for(int i=0;i<studentList.size();i++){
                    AttendanceItem attendance = studentList.get(i);
                    if(attendance.isSelected()){
                        mref.child(attendance.getName()).setValue("present");
                        responseText.append("\n" + attendance.getName());
                    }
                    else{
                        mref.child(attendance.getName()).setValue("absent");
                    }
                }

                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();

            }
        });

    }
}