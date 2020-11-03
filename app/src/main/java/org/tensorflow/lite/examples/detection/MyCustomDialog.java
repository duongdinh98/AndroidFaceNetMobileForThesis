package org.tensorflow.lite.examples.detection;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class MyCustomDialog {
    private Activity activity;
    private AlertDialog alertDialog;
    private String text;

    public MyCustomDialog(Activity activity) {
        this.activity = activity;
    }

    public MyCustomDialog(Activity activity, String text) {
        this.activity = activity;
        this.text = text;
    }

    public void dismissDialog() {
        alertDialog.dismiss();
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.loading_dialog, null);

        TextView txtLoader = dialogLayout.findViewById(R.id.txtDialog);
        txtLoader.setText(this.text);

        builder.setView(dialogLayout);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void startLoadingDialogNoAnim() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.loading_no_anim_dialog, null);

        TextView txtLoader = dialogLayout.findViewById(R.id.txtDialog);
        txtLoader.setText(this.text);

        builder.setView(dialogLayout);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void startSuccessMakeARollCallDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.success_make_a_roll_call_dialog, null);

        Button btnOk = dialogLayout.findViewById(R.id.btn_ok_dialog);
        TextView txtDialog = dialogLayout.findViewById(R.id.txtDialog);
        txtDialog.setText(text);
        btnOk.setOnClickListener(view -> {
            alertDialog.dismiss();
            activity.finish();
        });

        builder.setView(dialogLayout);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void startErrorMakeARollCallDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.error_make_a_roll_call_dialog, null);

        Button btnOk = dialogLayout.findViewById(R.id.btn_ok_dialog);
        TextView txtDialog = dialogLayout.findViewById(R.id.txtDialog);
        txtDialog.setText(text);
        btnOk.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        builder.setView(dialogLayout);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void startSuccessMakeARollCallDialogNoFinish(int numOfRegistered) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.success_make_a_roll_call_dialog, null);

        Button btnOk = dialogLayout.findViewById(R.id.btn_ok_dialog);
        TextView txtDialog = dialogLayout.findViewById(R.id.txtDialog);
        txtDialog.setText(text);
        btnOk.setOnClickListener(view -> {
            alertDialog.dismiss();
            if(numOfRegistered == 3) {
                Toasty.success(activity, "Hoàn tất quá trình đăng kí", Toast.LENGTH_SHORT, true).show();
                activity.finish();
            }
        });

        builder.setView(dialogLayout);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }
}
