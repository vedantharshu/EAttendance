package com.example.eattendance.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eattendance.R;
import com.example.eattendance.admin.teachers.ClassItems;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class todaysAttendanceAdapter extends ArrayAdapter<TodaysAttendanceItems> {

    Context context;
    List<TodaysAttendanceItems> todaysAttendanceList;
    DatabaseReference mref;

    public todaysAttendanceAdapter(Context context, int resource, @NonNull List<TodaysAttendanceItems> objects) {
        super(context, resource, objects);

        this.context = context;
        todaysAttendanceList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.todays_attendance_list, parent, false);

        TextView sNo = view.findViewById(R.id.textSno1);
        TextView subject = view.findViewById(R.id.textSubjectName);
        TextView attendanceFlag = view.findViewById(R.id.textAttendanceFlag);

        TodaysAttendanceItems tl = todaysAttendanceList.get(position);

        sNo.setText(tl.getsNo());
        subject.setText(tl.getSubject());
        attendanceFlag.setText(tl.getAttendanceFlag());

        return view;
    }
}
