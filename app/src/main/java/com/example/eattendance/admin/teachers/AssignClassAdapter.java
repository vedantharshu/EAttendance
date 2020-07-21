package com.example.eattendance.admin.teachers;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.eattendance.R;
import com.example.eattendance.admin.students.RemoveStudent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AssignClassAdapter extends ArrayAdapter<ClassItems> {
    Context context;
    List<ClassItems> classList;
    DatabaseReference mref;
    String teacherID, code;

    public AssignClassAdapter(Context context, int resource,  @NonNull List<ClassItems> objects, String teacherID,String code) {
        super(context, resource, objects);
        this.context = context;
        classList = objects;
        this.teacherID = teacherID;
        this.code = code;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.assign_classes_list, parent, false);
        TextView sno = view.findViewById(R.id.textSno);
        TextView className = view.findViewById(R.id.textClass);
        TextView subject = view.findViewById(R.id.textSubjects);
        Button delete = view.findViewById(R.id.deleteClass);

        ClassItems cI = classList.get(position);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ClassItems r = getItem(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Class");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(position,r.getClassName());
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


        sno.setText(cI.getsNo());
        className.setText(cI.getClassName());
        subject.setText(cI.getSubject());
        return view;
    }

    private void removeItem(final int position,String classItem) {

        mref= FirebaseDatabase.getInstance().getReference("Admins").child("AD_"+code).child("Teachers").child(teacherID).child("Classes");
        mref.child(classItem).removeValue();

        classList.remove(position);
        notifyDataSetChanged();
    }
}