package org.tensorflow.lite.examples.detection.serverdata;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.StudentLocation;
import org.tensorflow.lite.examples.detection.UpdateStudentLocation;

import java.util.ArrayList;

public class StudentLocationAdapter extends RecyclerView.Adapter<StudentLocationAdapter.ViewHolder>
{
    private ArrayList<StudentLocationData> listdata;
    private Context context;

    public StudentLocationAdapter(ArrayList<StudentLocationData> dataModel, Context c) {
        this.listdata = dataModel;
        this.context = c;
    }

    @NonNull
    @Override
    public StudentLocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.student_location_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentLocationAdapter.ViewHolder holder, int position) {
        String name = listdata.get(position).getTenHocVien();
        String phone = listdata.get(position).getSdt();
        String location  = listdata.get(position).getLocation();
        String id = listdata.get(position).getId();

        holder.txtStudentName.setText(name);
        holder.txtPhoneNum.setText(phone);
        holder.txtLocation.setText(location == null ? "Trống" : location);
        holder.btnUpdate.setOnClickListener(view -> {
            Intent intent = new Intent(context, UpdateStudentLocation.class);
            intent.putExtra("id", id);
            intent.putExtra("name", name);
            intent.putExtra("currentLocation", location);
            context.startActivity(intent);
        });

        holder.btnCallStudent.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtStudentName, txtPhoneNum, txtLocation;
        public Button btnUpdate;
        public ImageView btnCallStudent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.txtStudentName = itemView.findViewById(R.id.txt_name_of_student);
            this.txtPhoneNum = itemView.findViewById(R.id.txt_phone_num);
            this.txtLocation = itemView.findViewById(R.id.txt_location);
            this.btnUpdate = itemView.findViewById(R.id.btn_update_location);
            this.btnCallStudent = itemView.findViewById(R.id.btn_call_student);
        }
    }
}