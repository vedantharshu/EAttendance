package com.example.eattendance.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eattendance.R;
import com.example.eattendance.UserType;
import com.example.eattendance.admin.AdminActivity;
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

public class AdminAuth extends AppCompatActivity {
    EditText input_email,input_password, schoolCode;
    TextView signUp_btn_on_login,forgot_password_on_login;
    Button login_btn;
    String pwd, uname, check,s;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    DatabaseReference mref, mref1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_auth);

        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        login_btn =  findViewById(R.id.login_btn);
        signUp_btn_on_login =  findViewById(R.id.signup_btn_on_login);
        forgot_password_on_login = findViewById(R.id.forgot_password_on_login);
        schoolCode = findViewById(R.id.code);

        mref= FirebaseDatabase.getInstance().getReference("Admins");
        mref1= mref;
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s=schoolCode.getText().toString().trim();
                uname = input_email.getText().toString().trim();
                pwd = input_password.getText().toString().trim();
                if(uname.equals("")){
                    input_email.setError("Enter Email");
                    Toast.makeText(AdminAuth.this, "Enter Email!!",
                            Toast.LENGTH_SHORT).show();
                }
                else if(pwd.equals("")){
                    input_password.setError("Enter Password");
                    Toast.makeText(AdminAuth.this, "Enter Password!!",
                            Toast.LENGTH_SHORT).show();
                }
                else if(s.equals("")){
                    schoolCode.setError("Enter School Code");
                    Toast.makeText(AdminAuth.this, "Enter School Code!!",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    mref1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String k []= uname.split("\\.");
                            check = dataSnapshot.child(k[0]).getValue().toString();
                            if (check.equals(s)) {
                                loginUser(uname, pwd);
                                return;
                            } else {
                                schoolCode.setError("School Code Did Not Match");
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        signUp_btn_on_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminAuth.this,SignUpActivity.class));
            }
        });
        forgot_password_on_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminAuth.this,ForgotPasswordActivity.class));
            }
        });
        mAuth=FirebaseAuth.getInstance();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        user=mAuth.getCurrentUser();
        updateUI(user);
    }
    /*------------ Below Code is for successful login process -----------*/

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(AdminAuth.this, "Authentication failed: " + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
    }
    private void updateUI(FirebaseUser user1) {
        /*-------- Check if user is already logged in or not--------*/
        user1=mAuth.getCurrentUser();
        if (user1 != null) {
            /*------------ If user's email is verified then access login -----------*/
            if(user1.isEmailVerified()){
                Toast.makeText(AdminAuth.this, "Login Success.",
                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AdminAuth.this, AdminActivity.class);
                i.putExtra("code",s);
                Log.d("TAG", "updateUI: "+s);
                startActivity(i);
            }
            else {
                Toast.makeText(AdminAuth.this, "Your Email is not verified.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AdminAuth.this, UserType.class));
    }
}