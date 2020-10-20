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
import org.tensorflow.lite.examples.detection.Student;

import java.util.ArrayList;

public class ClassroomAdapter extends RecyclerView.Adapter<ClassroomAdapter.ViewHolder>
{
    private ArrayList<ClassroomData> listData;
    private Context context;


    public ClassroomAdapter(ArrayList<ClassroomData> dataModel, Context c) {
        this.listData = dataModel;
        this.context = c;
    }

    @NonNull
    @Override
    public ClassroomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.classroom_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClassroomAdapter.ViewHolder holder, int position) {
        String id = listData.get(position).getId();
        String tenLop = listData.get(position).getTenLop();
        String khaiGiang = listData.get(position).getKhaiGiang();
        String loaiBang = listData.get(position).getLoaiBang();
        String thoiGianHoc = listData.get(position).getThoiGianHoc();

        holder.txtClassName.setText(tenLop);
        holder.txtKhaiGiang.setText(khaiGiang);
        holder.txtLoaiBang.setText(loaiBang);
        holder.txtThoiGianHoc.setText(thoiGianHoc);

        holder.btnDetail.setOnClickListener(view -> {
            Intent intent = new Intent(context, Student.class);
            intent.putExtra("className", tenLop);
            intent.putExtra("classId", id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtClassName, txtKhaiGiang, txtLoaiBang, txtThoiGianHoc;
        public Button btnDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.txtClassName = itemView.findViewById(R.id.txt_class_name);
            this.txtKhaiGiang = itemView.findViewById(R.id.txt_khai_giang);
            this.txtLoaiBang = itemView.findViewById(R.id.txt_loai_bang);
            this.txtThoiGianHoc = itemView.findViewById(R.id.txt_so_gio);
            this.btnDetail = itemView.findViewById(R.id.btn_classroom_detail);
        }
    }
}