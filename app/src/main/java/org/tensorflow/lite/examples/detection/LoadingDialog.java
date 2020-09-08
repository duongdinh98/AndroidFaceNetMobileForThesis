package org.tensorflow.lite.examples.detection;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog alertDialog;
    private int totalTime;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public LoadingDialog(Activity activity, int totalTime) {
        this.activity = activity;
        this.totalTime = totalTime;
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismissLoadingDialog() {
        alertDialog.dismiss();
    }

    public void startErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.error_dialog, null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismissErrorDialog() {
        alertDialog.dismiss();
    }

    public void startSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.success_dialog, null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismissSuccessDialog() {
        alertDialog.dismiss();
    }

    public void startSuccessCheckOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.success_check_out_dialog, null);

        TextView txtTotalTime = dialogLayout.findViewById(R.id.txtTotalTime);
        txtTotalTime.setText("Total time: " + convertSecondToTime(this.totalTime));

        builder.setView(dialogLayout);
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();
    }

    private  String convertSecondToTime(int seconds) {
        int ss = seconds % 60;
        int hh = seconds / 60;
        int mm = hh % 60;
        hh = hh / 60;

        String hour = "";
        String minute = "";
        String second = "";

        if(hh != 0) {
            hour = hh + "giờ ";
        }

        if(mm != 0) {
            minute = mm + "phút ";
        }

        if(ss != 0) {
            second = ss + "giây";
        }

        return ( hour + minute + second);
    }

}
