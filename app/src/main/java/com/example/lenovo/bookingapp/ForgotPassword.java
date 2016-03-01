package com.example.lenovo.bookingapp;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.lenovo.bookingapp.Utils.AlertMessage;
import com.example.lenovo.bookingapp.Utils.CallBackInterface;
import com.example.lenovo.bookingapp.Utils.CommonFunctions;
import com.example.lenovo.bookingapp.Utils.Constants;
import com.neopixl.pixlui.components.edittext.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by deii on 12/13/2015.
 */
public class ForgotPassword extends AppCompatActivity implements CallBackInterface{
    private EditText edtEmail;
    private CommonFunctions functions;
    private TextInputLayout tilEmail;
    String EmailID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.forgot_password);

        InitViews();

    }

    private void InitViews() {

        functions = new CommonFunctions(this);



        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
    }

    @Override
    public void onJsonObjectSuccess(JSONObject object) {
        //Utils.showAlert(this, "Email Sent. Kindly check email in your inbox as well as junk folder!","ALERT");
        AlertMessage.showAlertDialog(this, "Alert","Email Sent. Kindly check email in your inbox as well as junk folder!", false );
    }

    @Override
    public void onJsonArrarSuccess(JSONArray array) {

    }

    @Override
    public void onFailure(String str) {
        CommonFunctions.showSnackBarWithoutAction(getCurrentFocus(),str);

    }

    private HashMap<String, String> createJsonForForgotPassword() {
        HashMap<String, String> outerJsonObject = new HashMap<String, String>();
        try {

            outerJsonObject.put(Constants.EMAIL_ID, EmailID);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outerJsonObject;
    }


}
