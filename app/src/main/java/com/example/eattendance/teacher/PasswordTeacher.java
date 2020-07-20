package com.example.eattendance.teacher;

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

public class PasswordTeacher extends AppCompatActivity {

    EditText username, newPass;
    Button save, submit;
    DatabaseReference mref;
    String id, code, password;
    boolean verified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_teacher);

        username = findViewById(R.id.username);//210_TE_11
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
                code = id.split("_")[0];
                mref = FirebaseDatabase.getInstance().getReference("Admins").child("AD_"+code).child("Teachers");

                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(id)){
                            Toast.makeText(PasswordTeacher.this, "ID verified", Toast.LENGTH_SHORT).show();
                            verified = true;
                            newPass.setVisibility(View.VISIBLE);
                            save.setVisibility(View.VISIBLE);
                        }
                        else{
                            Toast.makeText(PasswordTeacher.this, "Invalid ID", Toast.LENGTH_SHORT).show();
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
                        mref.child(id).child("Password").setValue(password);
                        Toast.makeText(PasswordTeacher.this, "Password Changed Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }
}