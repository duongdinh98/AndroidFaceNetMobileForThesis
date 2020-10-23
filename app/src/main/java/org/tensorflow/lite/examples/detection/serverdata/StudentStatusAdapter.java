package org.tensorflow.lite.examples.detection.serverdata;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.lite.examples.detection.FaceCheckHelper;
import org.tensorflow.lite.examples.detection.FaceCheckOutConfirm;
import org.tensorflow.lite.examples.detection.MyCustomDialog;
import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.StudentProfile;
import org.tensorflow.lite.examples.detection.tflite.SaveDataSet;

import java.util.ArrayList;

public class StudentStatusAdapter extends RecyclerView.Adapter<StudentStatusAdapter.ViewHolder>
{
    private ArrayList<StudentData> listData;
    private Context context;


    public StudentStatusAdapter(ArrayList<StudentData> dataModel, Context c) {
        this.listData = dataModel;
        this.context = c;
    }

    @NonNull
    @Override
    public StudentStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.student_status_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentStatusAdapter.ViewHolder holder, int position) {
        String studentName = listData.get(position).getTenHocVien();
        String ngaySinh = listData.get(position).getNgaySinh();
        String soCmnd = listData.get(position).getSoCmnd();

        FaceCheckHelper faceCheckHelper = new FaceCheckHelper(context, "hnd_data.sqlite", null, 1);
        Cursor cursor = faceCheckHelper.getData("SELECT * FROM attendance WHERE idHocVien='" + listData.get(position).getId() + "'");
        if((cursor != null) && (cursor.getCount() > 0)){
            holder.attendanceStatus.setVisibility(View.VISIBLE);
        } else {
            holder.attendanceStatus.setVisibility(View.INVISIBLE);
        }

        holder.txtStudentName.setText(studentName);
        holder.txtNgaySinh.setText(ngaySinh);
        holder.txtSoCmnd.setText(soCmnd);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtStudentName, txtNgaySinh, txtSoCmnd;
        public LinearLayout attendanceStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.txtStudentName = itemView.findViewById(R.id.txt_student_name);
            this.txtNgaySinh = itemView.findViewById(R.id.txt_ngay_sinh);
            this.txtSoCmnd = itemView.findViewById(R.id.txt_so_cmnd);
            this.attendanceStatus = itemView.findViewById(R.id.attendance_status);
        }
    }
}