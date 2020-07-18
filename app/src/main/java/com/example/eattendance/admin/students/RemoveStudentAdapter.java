package com.example.eattendance.admin.students;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.eattendance.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.zip.Inflater;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class RemoveStudentAdapter extends ArrayAdapter<RemoveStudent> {
    Context context;
    List<RemoveStudent> studentList;
    DatabaseReference mref;
    String standard, section, code;
    public RemoveStudentAdapter(@NonNull Context context, int resource, @NonNull List<RemoveStudent> objects, String standard, String section, String code) {
        super(context, resource, objects);
        this.context = context;
        studentList = objects;
        this.standard = standard;
        this.section = section;
        this.code = code;
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

               final RemoveStudent r = getItem(position);
               AlertDialog.Builder builder = new AlertDialog.Builder(context);
               builder.setTitle("Delete Student");
               builder.setMessage("Are you sure?");
               builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       removeItem(position,r.getStudentName());
                   }
               });
               builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"Delete cancelled", Toast.LENGTH_LONG);
                   }
               });
               builder.show();
           }
       });
       return view;
    }

    private void removeItem(final int position,String student) {
        Log.d(TAG, "removeItem: "+standard+section);
        String st[]=student.split(" ");
        mref= FirebaseDatabase.getInstance().getReference("Admins").child("AD_"+code).child("Students").child(standard+section);
        mref.child(code+"_ST_"+standard+section+"_"+st[0]).removeValue();

       studentList.remove(position);
       notifyDataSetChanged();
    }
}
