package com.example.eattendance.admin.teachers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eattendance.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RemoveTeacher extends AppCompatActivity {
    Button remove;
    EditText teacherID;
    TextView scode;
    String s,code;
    DatabaseReference mref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_teacher);
        remove = findViewById(R.id.remove);
        teacherID = findViewById(R.id.teacherID);
        scode = findViewById(R.id.scode);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            code = extras.getString("code");
        }
        scode.setText(code);

        mref = FirebaseDatabase.getInstance().getReference("Admins").child("AD_"+code).child("Teachers");
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s = teacherID.getText().toString().trim();
                if(s.equals("")){
                    teacherID.setError("Enter Teacher ID");
                }
                else{
                    mref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.hasChild(code+"_TE_"+s)){
                                teacherID.setError("Invalid ID");
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(RemoveTeacher.this);

                                builder.setTitle("Delete Teacher");
                                builder.setMessage("Are you sure?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mref.child(code+"_TE_"+s).removeValue();
                                        Toast.makeText(getApplicationContext(), "Removed Successfully",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                builder.setCancelable(false);
                                builder.show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }
}