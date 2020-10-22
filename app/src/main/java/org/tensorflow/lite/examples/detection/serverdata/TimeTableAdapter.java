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

import java.util.ArrayList;

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.ViewHolder>
{
    private ArrayList<TimeTableData> listdata;
    private Context context;

    public TimeTableAdapter(ArrayList<TimeTableData> dataModel, Context c) {
        this.listdata = dataModel;
        this.context = c;
    }

    @NonNull
    @Override
    public TimeTableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.time_table_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeTableAdapter.ViewHolder holder, int position) {
        String myListData = listdata.get(position).getNumOfClass() + "";
        final String day = listdata.get(position).getDay();

        holder.txtDay.setText(day);
        holder.txtNumOfClass.setText(myListData);
        holder.btnDetailTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDay;
        public TextView txtNumOfClass;
        public Button btnDetailTable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.txtDay = itemView.findViewById(R.id.txt_day);
            this.txtNumOfClass = itemView.findViewById(R.id.numOfClass);
            this.btnDetailTable = itemView.findViewById(R.id.btn_time_table_detail);
        }
    }
}