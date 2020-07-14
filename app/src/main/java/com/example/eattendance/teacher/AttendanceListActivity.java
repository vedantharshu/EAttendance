package com.example.eattendance.teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);
        Log.d(TAG,"AttendanceListActivity: Started");








        listView=(ListView)findViewById(R.id.attendanceLV);

        AttendanceItem ojasva=new AttendanceItem("1","Ojasva",false);
        AttendanceItem vedant=new AttendanceItem("2","Vedant",false);
        AttendanceItem shivansh=new AttendanceItem("3","Shivansh",false);
        AttendanceItem jagrat=new AttendanceItem("4","Jagrat",false);
        AttendanceItem A=new AttendanceItem("5","A",false);
        AttendanceItem B=new AttendanceItem("6","B",false);
        AttendanceItem C=new AttendanceItem("7","C",false);
        AttendanceItem D=new AttendanceItem("8","D",false);
        AttendanceItem E=new AttendanceItem("9","E",false);
        AttendanceItem F=new AttendanceItem("10","F",false);
        AttendanceItem G=new AttendanceItem("11","G",false);
        AttendanceItem H=new AttendanceItem("12","H",false);
        AttendanceItem I=new AttendanceItem("13","I",false);
        AttendanceItem J=new AttendanceItem("14","J",false);
        AttendanceItem K=new AttendanceItem("15","K",false);




        ArrayList<AttendanceItem> studentList=new ArrayList<>();


        studentList.add(ojasva);
        studentList.add(vedant);
        studentList.add(shivansh);
        studentList.add(jagrat);
        studentList.add(A);
        studentList.add(B);
        studentList.add(C);
        studentList.add(D);
        studentList.add(E);
        studentList.add(F);
        studentList.add(G);
        studentList.add(H);
        studentList.add(I);
        studentList.add(J);
        studentList.add(K);


        adapter=new AttendanceListAdapter(this,R.layout.activity_attendance_list,studentList);
        listView.setAdapter(adapter);

        presentCount=0;

        present = new StringBuffer();
        present.append("Present:\n");

        absent = new StringBuffer();
        absent.append("Present:\n");

        submit=(Button)findViewById(R.id.submit_attenBTN);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<AttendanceItem> atList =adapter.attendanceList;
                for(int i=0;i<atList.size();i++){
                    AttendanceItem attendance = atList.get(i);
                    if(attendance.isSelected()){
                        present.append("\n" + attendance.getName());
                        presentCount++;
                    }
                    else
                    {
                        absent.append("\n"+attendance.getName());
                    }

                }
                present.append("\nTotal Present: "+String.valueOf(presentCount));

                Toast.makeText(getApplicationContext(),present, Toast.LENGTH_LONG).show();

                finish();
            }
        });




    }
}