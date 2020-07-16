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
import android.widget.Toast;

import com.example.eattendance.admin.AdminActivity;
import com.example.eattendance.student.StudentActivity;
import com.example.eattendance.teacher.TeacherActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    Button login;
    String item;
    EditText username,password;
    String uname="",pass="";
    String TAG="Login_Activity";
    DatabaseReference myRef ;
    FirebaseDatabase database;
    String adminID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        spinner=(Spinner)findViewById(R.id.role_spinner);
        login=(Button)findViewById(R.id.loginBTN);
        username=(EditText)findViewById(R.id.usernameET);
        password=(EditText)findViewById(R.id.passwordET);

        database = FirebaseDatabase.getInstance();


        spinner.setOnItemSelectedListener(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(item.equals("Teacher")) {
                    uname=username.getText().toString();
                    pass=password.getText().toString();
                    String[] s=uname.split("_");
                    String code=s[0];
                    adminID="AD_"+code;

                    myRef= database.getReference("Admins");


                    myRef.child(adminID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {

                                myRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if((dataSnapshot.child(adminID).child("Teachers").child(uname).child("Password").getValue().toString()).equals(pass))
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

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        Toast.makeText(LoginActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                                        Log.w(TAG, "Failed to read value.", error.toException());
                                    }
                                });



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

                else if(item.equals("Teacher"))
                    startActivity(new Intent(LoginActivity.this, TeacherActivity.class));

                else if(item.equals("Student"))
                    startActivity(new Intent(LoginActivity.this, StudentActivity.class));

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}