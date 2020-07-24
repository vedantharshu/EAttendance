package com.example.eattendance.teacher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.eattendance.R;
import com.example.eattendance.teacher.AttendanceItem;

import java.util.List;

public class AttendanceListAdapter extends ArrayAdapter<AttendanceItem>{

    Context context;
    List<AttendanceItem> attendanceList;

    public AttendanceListAdapter(@NonNull Context context, int resource, @NonNull List<AttendanceItem> objects) {
        super(context, resource, objects);
        this.context=context;
        attendanceList=objects;
    }

    private class ViewHolder {
        TextView rollno;
        TextView name;
        CheckBox checkBox;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.attendance_list_item, parent, false);

            holder = new ViewHolder();

            holder.rollno=(TextView)convertView.findViewById(R.id.class_roll_no);
            holder.name = (TextView) convertView.findViewById(R.id.student_name);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    AttendanceItem attendance = (AttendanceItem) cb.getTag();
                    attendance.setSelected(cb.isChecked());
                }
            });
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        AttendanceItem student = attendanceList.get(position);
        holder.rollno.setText(student.getRollno());
        holder.name.setText(student.getName());
        holder.checkBox.setText("");
        holder.checkBox.setChecked(student.isSelected());
        holder.checkBox.setTag(student);

        return convertView;

    }
}
