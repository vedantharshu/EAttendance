package com.example.eattendance.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eattendance.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    EditText user_email,user_password,user_name, confirm_password, school_code;
    TextView login_btn_on_signup;
    Button signup_btn;
    String uname,name,pwd,code;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    DatabaseReference mref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mref= FirebaseDatabase.getInstance().getReference("Admins");
        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);
        confirm_password = findViewById(R.id.confirm_password);
        school_code = findViewById(R.id.school_code);
        user_name = findViewById(R.id.user_name);
        signup_btn =  findViewById(R.id.register_btn);
        login_btn_on_signup =  findViewById(R.id.login_btn_on_signup);

        login_btn_on_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,AdminAuth.class));
            }
        });
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = school_code.getText().toString().trim();
                if(code.equals("")){
                    school_code.setError("Enter School Code");
                }
                else {
                    mref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (!dataSnapshot.hasChild("AD_" + code)) {
                                signUpUser(user_email.getText().toString(), user_password.getText().toString(), confirm_password.getText().toString().trim());
                            } else {
                                school_code.setError("Code Already Taken");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        mAuth=FirebaseAuth.getInstance();
    }

    private void signUpUser(String email, String password, String confirmPassword) {
        if(email.equals("")){
            user_email.setError("Enter Email");
        }
        else if(password.equals("")){
            user_password.setError("Enter Password");
        }
        else if(confirmPassword.equals("")){
            confirm_password.setError("Password Didn't Match");
        }
        else if((user_name.getText().toString()).equals("")){
            user_name.setError("Enter School Name");
        }
        else if(password.compareTo(confirmPassword)!=0){
            confirm_password.setError("Password Didn't Match");
        }
        else if(password.length()<8){
            confirm_password.setError("Password Too Short");
        }
        else {
            uname=user_email.getText().toString().trim();
            pwd=user_password.getText().toString().trim();
            name=user_name.getText().toString().trim();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                userProfile();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignUpActivity.this, "Sign up failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void userProfile() {
        user = mAuth.getCurrentUser();
        if(user != null){
            verifyEmailRequest();
        }
    }
    private void verifyEmailRequest() {
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Admin ob=new Admin(name,pwd,uname);
                            code = school_code.getText().toString().trim();
                            mref.child("AD_"+code).setValue(ob);
                            String l[] = uname.split("\\.");
                            mref.child(l[0]).setValue(code);
                            Toast.makeText(SignUpActivity.this,"Email verification sent on\n"+user_email.getText().toString(),Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                            Intent i = new Intent(SignUpActivity.this,AdminAuth.class);
                            startActivity(i);
                    }
                        else {
                            Toast.makeText(SignUpActivity.this,"Sign up Success But Failed to send verification email.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}