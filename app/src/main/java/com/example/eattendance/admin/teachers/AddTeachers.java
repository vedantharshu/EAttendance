package com.example.eattendance.admin.teachers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eattendance.R;
import com.example.eattendance.backendAdmin.TeacherDetail;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTeachers extends AppCompatActivity {
    EditText teacher_name, teacher_id;
    Button submit;
    String TeacherUniqueId, TeacherName;
    DatabaseReference mref, mref1, mref2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teachers);

        mref = FirebaseDatabase.getInstance().getReference("Admins").child("AD_201").child("Teachers");
        submit = findViewById(R.id.idAddTeacherSubmit);
        teacher_name = findViewById(R.id.idTAddeacherFullName);
        teacher_id = findViewById(R.id.idAddTeacherID);

        submit.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(teacher_name.getText().toString().trim().compareTo("")==0){
                  teacher_name.setError("This field cannot be empty");
              }
              else if((teacher_id.getText().toString().trim().compareTo("")==0)){
                  teacher_id.setError("This Field Cannot Be Empty");
              }
              else{
                  TeacherName = teacher_name.getText().toString().trim();
                  TeacherUniqueId = "201" + "_TE_" + teacher_id.getText().toString();
                  mref1 = mref.child(TeacherUniqueId);
                  mref2 = mref1.child("Classes");
                  TeacherDetail td = new TeacherDetail(TeacherName, "Check", "None");
                  mref1.child("Name").setValue(td.getName());
                  mref1.child("Password").setValue(td.getPassword());
                  mref1.child("Classes").setValue(td.getClasses());
                  Toast.makeText(getApplicationContext(),
                          "Teacher with ID " + TeacherUniqueId + " added Successfully", Toast.LENGTH_SHORT).show();
              }
          }
      });
    }
}