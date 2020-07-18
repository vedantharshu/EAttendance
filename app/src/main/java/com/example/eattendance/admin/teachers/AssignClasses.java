package com.example.eattendance.admin.teachers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eattendance.R;
import com.example.eattendance.admin.students.EditStudent;
import com.example.eattendance.admin.students.RemoveStudent;
import com.example.eattendance.admin.students.RemoveStudentAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AssignClasses extends AppCompatActivity {
    EditText teacherId;
    Button search, assignClass;
    DatabaseReference mref, mref1;
    ListView mlist;
    ArrayList<ClassItems> ClassesList;
    AssignClassAdapter classAdapter = null;
    TextView msg ;
    String teacherID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_classes);

        teacherId = findViewById(R.id.idTeacherSearch);
        search = findViewById(R.id.idSearchTeacherButton);
        assignClass = findViewById(R.id.assignNewClass);
        msg = findViewById(R.id.totalClasses);
        mlist = findViewById(R.id.listViewClasses);

        assignClass.setVisibility(View.INVISIBLE);

        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            Toast.makeText(getApplicationContext(),teacherId.getText().toString()+"is selected" ,Toast.LENGTH_LONG).show();
            mref = FirebaseDatabase.getInstance().getReference("Admins").child("AD_201").child("Teachers");
            teacherID = teacherId.getText().toString();
            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(teacherID)){
                        assignClass.setVisibility(View.VISIBLE);
                        mref = mref.child(teacherID);
                        showList();
                    }
                    else{
                        mlist.setVisibility(View.INVISIBLE);
                        msg.setText("Please Enter Valid Teacher ID");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            }
        });

        assignClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    public void openDialog() {
        addClassDialog d = new addClassDialog(teacherID);
        d.show(getSupportFragmentManager(), "Enter Class");
    }


    public void showList(){

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Classes")){
                    mlist.setVisibility(View.VISIBLE);
                    msg.setText("Classes Assigned :");
                    mref1 = mref.child("Classes");
                    mref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            ClassesList = new ArrayList<>();
                            ClassesList.clear();
                            int i = 1;
                            for(DataSnapshot data: dataSnapshot1.getChildren()){
                                ClassesList.add(new ClassItems(data.getKey().toString(), data.getValue().toString(), String.valueOf(i++)));
                            }
                            classAdapter= new AssignClassAdapter(AssignClasses.this, R.layout.assign_classes_list,ClassesList, teacherID);
                            mlist.setAdapter(classAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    mlist.setVisibility(View.INVISIBLE);
                    msg.setText("No Classes Assigned");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}