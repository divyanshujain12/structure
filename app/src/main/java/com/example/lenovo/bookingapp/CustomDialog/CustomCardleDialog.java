package com.example.lenovo.bookingapp.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.lenovo.bookingapp.R;
import com.neopixl.pixlui.components.textview.TextView;

/**
 * Created by Lenovo on 23-02-2016.
 */
public class CustomCardleDialog extends Dialog {

    NewtonCradleLoading newtonCradleLoading = null;
    TextView txtLoadingMessage;

    public CustomCardleDialog(Context context) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_cardle_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        newtonCradleLoading = (NewtonCradleLoading) view.findViewById(R.id.newton_cradle_loading);

        txtLoadingMessage = (TextView) view.findViewById(R.id.txtLoadingMessage);
        getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        setContentView(view);
    }

    @Override
    public void show() {
        super.show();
        newtonCradleLoading.start();

    }

    public void Success() {
        txtLoadingMessage.setText("S U C C E S S");
        newtonCradleLoading.setLoadingColor(getContext().getResources().getColor(android.R.color.holo_green_dark));
        newtonCradleLoading.stop();
    }

    public void Fail(String message) {
        txtLoadingMessage.setText(message);
        newtonCradleLoading.setLoadingColor(getContext().getResources().getColor(android.R.color.holo_red_dark));
        newtonCradleLoading.stop();
    }


    @Override
    protected void onStop() {
        super.onStop();
        newtonCradleLoading.stop();
    }
}
