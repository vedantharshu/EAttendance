package com.example.eattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eattendance.student.PasswordStudent;
import com.example.eattendance.student.StudentActivity;
import com.example.eattendance.teacher.PasswordTeacher;
import com.example.eattendance.teacher.TeacherActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    Button login;
    String item;
    EditText username,password;
    String uname="",pass="";
    String TAG="Login_Activity";
    DatabaseReference myRef, mref;
    FirebaseDatabase database;
    String adminID, standard;
    TextView forgot_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.loginBTN);
        username= findViewById(R.id.usernameET);
        password= findViewById(R.id.passwordET);
        forgot_password = findViewById(R.id.forgot_password);
        database = FirebaseDatabase.getInstance();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            item = extras.getString("item");
        }
        if(item.equals("Teacher")){
            username.setHint("Username(eg. 201_TE_1)");
        }
        if(item.equals("Student")){
            username.setHint("Username(eg. 201_ST_2A_1)");
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(item.equals("Teacher")) {
                    uname=username.getText().toString().trim();
                    pass=password.getText().toString();
                    String[] s=uname.split("_");
                    String code=s[0];
                    adminID="AD_"+code;

                    myRef= database.getReference("Admins").child(adminID);
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child("Teachers").child(uname).exists())
                            {
                                if((dataSnapshot.child("Teachers").child(uname).child("Password").getValue().toString()).equals(pass))
                                {
                                    username.getText().clear();
                                    password.getText().clear();
                                    Intent i=new Intent(LoginActivity.this, TeacherActivity.class);
                                    i.putExtra("user_name",uname);
                                    i.putExtra("adminID",adminID);
                                    startActivity(i);
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                                }

                                Log.d(TAG, "Value retreived..");
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else if(item.equals("Student")) {
                    uname=username.getText().toString().trim();//username 210_ST_2A_14
                    pass=password.getText().toString();//Check
                    String[] s=uname.split("_");
                    String code=s[0];//210
                    adminID="AD_"+code;//AD_210
                    standard = s[2];
                    mref = FirebaseDatabase.getInstance().getReference("Admins").child(adminID).child("Students");
                    mref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(standard)){
                                if(dataSnapshot.child(standard).hasChild(uname)){
                                    if(dataSnapshot.child(standard).child(uname).child("password").getValue().toString().equals(pass)){
                                        Intent  intent = new Intent(LoginActivity.this, StudentActivity.class);
                                        intent.putExtra("studId",uname);
                                        intent.putExtra("class",standard);
                                        intent.putExtra("adminID",adminID);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(LoginActivity.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Invalid Username!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        //forgot password part
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.equals("Student")){
                    Intent x = new Intent(LoginActivity.this, PasswordStudent.class);
                    startActivity(x);
                }
                else if(item.equals("Teacher")){
                    Intent x = new Intent(LoginActivity.this, PasswordTeacher.class);
                    startActivity(x);
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}