package org.tensorflow.lite.examples.detection.serverdata;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.StudentProfile;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder>
{
    private ArrayList<StudentData> listData;
    private Context context;


    public StudentAdapter(ArrayList<StudentData> dataModel, Context c) {
        this.listData = dataModel;
        this.context = c;
    }

    @NonNull
    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.student_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.ViewHolder holder, int position) {
        String studentName = listData.get(position).getTenHocVien();
        String ngaySinh = listData.get(position).getNgaySinh();
        String soCmnd = listData.get(position).getSoCmnd();

        holder.txtStudentName.setText(studentName);
        holder.txtNgaySinh.setText(ngaySinh);
        holder.txtSoCmnd.setText(soCmnd);
        holder.btnDetail.setOnClickListener(view -> {
            Intent intent = new Intent(context, StudentProfile.class);
            intent.putExtra("identity", soCmnd);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtStudentName, txtNgaySinh, txtSoCmnd;
        public Button btnDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.txtStudentName = itemView.findViewById(R.id.txt_student_name);
            this.txtNgaySinh = itemView.findViewById(R.id.txt_ngay_sinh);
            this.txtSoCmnd = itemView.findViewById(R.id.txt_so_cmnd);
            this.btnDetail = itemView.findViewById(R.id.btn_student_detail);
        }
    }
}