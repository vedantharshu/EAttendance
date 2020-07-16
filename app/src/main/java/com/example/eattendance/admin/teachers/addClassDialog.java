package com.example.eattendance.admin.teachers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.eattendance.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addClassDialog  extends AppCompatDialogFragment {

    public EditText subject;
    public TextView t1,t2;
    public Spinner selectClass, selectSection;
    DatabaseReference mref;
    String classValue , sectionValue, teacherID;

    public addClassDialog(String teacherID) {
        this.teacherID = teacherID;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_class_dialog, null);

        subject = view.findViewById(R.id.subjectEntry);
        t1 = view.findViewById(R.id.t1);
        t2 = view.findViewById(R.id.t2);
        selectClass = view.findViewById(R.id.selectClass);
        selectSection = view.findViewById(R.id.selectSection);

        selectClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classValue = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sectionValue = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setView(view).setTitle("Add New Class").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addClass(classValue, sectionValue, subject.getText().toString());
            }
        });

        return builder.create();
    }

    private void addClass(String classValue, String sectionValue, String subjectName){

        Toast.makeText(getContext(),
                teacherID+ " " +classValue+sectionValue + " " + subjectName, Toast.LENGTH_SHORT).show();
        mref = FirebaseDatabase.getInstance().getReference("Admins").child("AD_201").child("Teachers").child(teacherID)
                .child("Classes");
        mref.child(classValue+sectionValue).setValue(subjectName);

    }
}
