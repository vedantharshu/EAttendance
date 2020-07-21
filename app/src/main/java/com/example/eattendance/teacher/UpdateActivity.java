package com.example.eattendance.teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.eattendance.R;
import com.example.eattendance.backendAdmin.StudentDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {
    private String date,subject,lecture, adminID,class_value, name, st;
    private ListView listView;
    private Button submit;
    private AttendanceListAdapter adapter = null;
    private ArrayList<AttendanceItem> studentList;
    private ArrayList<Boolean> fetchedList;
    private DatabaseReference mref, mref1,mref2;
    private String stuname, code;
    private String[] sn;
    private boolean mark;
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
                code = adminID.split("_")[1];
            }
            fetchedList=new ArrayList<>();
        listView = findViewById(R.id.attendanceLv);
        mref = FirebaseDatabase.getInstance().getReference("Admins").child(adminID);
        mref1 = FirebaseDatabase.getInstance().getReference("Admins").child(adminID).child("Attendance").child(date).child(class_value).child(lecture+"_"+subject);
        studentList = new ArrayList<>();

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                studentList.clear();
                for(DataSnapshot data: dataSnapshot.child("Attendance").child(date).child(class_value).child(lecture+"_"+subject).getChildren()){
                    if(data.getValue().toString().equals("absent")){
                        mark = false;
                        fetchedList.add(false);
                    }
                    else if(data.getValue().toString().equals("present")){
                        mark = true;
                        fetchedList.add(true);
                    }
                    st = data.getKey();

                    stuname=dataSnapshot.child("Students").child(class_value).child(st).child("username").getValue().toString();
                    sn=stuname.split(" ");

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
                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");
                for(int i=0;i<studentList.size();i++){
                    AttendanceItem attendance = studentList.get(i);
                    if(attendance.isSelected()){
                        mref1.child(code+"_ST_"+class_value+"_"+attendance.getRollno()).setValue("present");
                        responseText.append("\n" + attendance.getName());
                    }
                    else if(!attendance.isSelected()){
                        mref1.child(code+"_ST_"+class_value+"_"+attendance.getRollno()).setValue("absent");
                    }
                }

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
                                int m = ob.getAbsent();
                                if(fetchedList.get(in-1)==false) {
                                    mref2.child(data.getKey()).child("present").setValue(n+1);
                                    mref2.child(data.getKey()).child("absent").setValue(m-1);
                                }
                            }
                            else if (!at.isSelected()) {
                                int n = ob.getAbsent();
                                int m = ob.getPresent();
                                if(fetchedList.get(in-1)==true) {
                                    mref2.child(data.getKey()).child("absent").setValue(n+1);
                                    mref2.child(data.getKey()).child("present").setValue(m-1);
                                }
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
