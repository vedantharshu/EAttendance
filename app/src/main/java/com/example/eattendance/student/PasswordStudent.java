package com.example.eattendance.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eattendance.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PasswordStudent extends AppCompatActivity {

    EditText username, newPass;
    Button save, submit;
    DatabaseReference mref;
    String id, standard, code, password;
    boolean verified = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_student);

        username = findViewById(R.id.username);
        newPass = findViewById(R.id.newPass);
        save = findViewById(R.id.save);
        submit = findViewById(R.id.submit);

        newPass.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPass.setVisibility(View.INVISIBLE);
                save.setVisibility(View.INVISIBLE);
                id = username.getText().toString().trim();
                standard =  id.split("_")[2];
                code = id.split("_")[0];
                mref = FirebaseDatabase.getInstance().getReference("Admins").child("AD_"+code).child("Students");

                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(standard)){
                            if(dataSnapshot.child(standard).hasChild(id)){
                                Toast.makeText(PasswordStudent.this, "ID verified", Toast.LENGTH_SHORT).show();
                                verified = true;
                                newPass.setVisibility(View.VISIBLE);
                                save.setVisibility(View.VISIBLE);
                            }
                            else{
                                Toast.makeText(PasswordStudent.this, "Invalid ID", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(PasswordStudent.this, "Invalid ID", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verified){
                    password = newPass.getText().toString();
                    if(password.length()==0||password.length()<6){
                        newPass.setError("Password too short (Min. 6)");
                    }
                    else{
                        mref.child(standard).child(id).child("password").setValue(password);
                        Toast.makeText(PasswordStudent.this, "Password Changed Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });

    }
}