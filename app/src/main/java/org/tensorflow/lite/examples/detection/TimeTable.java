package org.tensorflow.lite.examples.detection;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.lite.examples.detection.serverdata.TimeTableAdapter;
import org.tensorflow.lite.examples.detection.serverdata.TimeTableData;

import java.util.ArrayList;
import java.util.Objects;

public class TimeTable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        Window window = TimeTable.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(TimeTable.this, R.color.timeTableColor));

        RecyclerView timeTable = (RecyclerView) findViewById(R.id.timeTable);

        ArrayList<TimeTableData> timeTableData = new ArrayList<>();
        timeTableData.add(new TimeTableData("Chủ nhật", 3));
        timeTableData.add(new TimeTableData("Thứ hai", 1));
        timeTableData.add(new TimeTableData("Thứ ba", 2));
        timeTableData.add(new TimeTableData("Thứ tư", 3));
        timeTableData.add(new TimeTableData("Thứ năm", 5));
        timeTableData.add(new TimeTableData("Thứ sáu", 7));
        timeTableData.add(new TimeTableData("Thứ bảy", 2));


        TimeTableAdapter timeTableAdapter = new TimeTableAdapter(timeTableData, TimeTable.this);
        timeTable.setLayoutManager(new LinearLayoutManager(this));
        timeTable.setAdapter(timeTableAdapter);
    }
}