package com.example.eattendance.admin.students;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eattendance.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.zip.Inflater;

public class RemoveStudentAdapter extends ArrayAdapter<RemoveStudent> {
    Context context;
    List<RemoveStudent> studentList;
    DatabaseReference mref;
    public RemoveStudentAdapter(@NonNull Context context, int resource, @NonNull List<RemoveStudent> objects) {
        super(context, resource, objects);
        this.context = context;
        studentList = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View view = LayoutInflater.from(context).inflate(R.layout.edit_list_student, parent, false);
       TextView name = view.findViewById(R.id.student_name);
       Button delete = view.findViewById(R.id.deleteStudent);

       RemoveStudent rem = studentList.get(position);
       name.setText(rem.getStudentName());

       delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               RemoveStudent r = getItem(position);

               removeItem(position,r.getStudentName());
           }
       });
       return view;
    }

    private void removeItem(final int position,String student) {
        mref= FirebaseDatabase.getInstance().getReference("Admins").child("AD_201").child("Students").child("2A");
        mref.child(student).removeValue();

       studentList.remove(position);
       notifyDataSetChanged();
    }
}
