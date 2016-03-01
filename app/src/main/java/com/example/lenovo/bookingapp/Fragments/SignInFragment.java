package com.example.lenovo.bookingapp.Fragments;

/**
 * Created by Mangal on 2/5/2016.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.android.volley.Request;
import com.example.lenovo.bookingapp.HomeActivity;
import com.example.lenovo.bookingapp.MyApplication;
import com.example.lenovo.bookingapp.R;
import com.example.lenovo.bookingapp.Utils.CallBackInterface;
import com.example.lenovo.bookingapp.Utils.CallWebService;
import com.example.lenovo.bookingapp.Utils.CommonFunctions;
import com.example.lenovo.bookingapp.Utils.ConnectionDetector;
import com.example.lenovo.bookingapp.Utils.Constants;
import com.neopixl.pixlui.components.edittext.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SignInFragment extends Fragment implements View.OnClickListener, CallBackInterface {


    SharedPreferences preferences;
    Button signin;
    ConnectionDetector cdr;
    EditText edtemail, edtpassword;
    TextInputLayout tilEmail, tilpassword;
    View v;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = MyApplication.preference;
        cdr = new ConnectionDetector(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.signin_layout, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitViews();

    }

    private void InitViews() {

        signin = (Button) getView().findViewById(R.id.signin_button);
        signin.setEnabled(false);

        signin.setBackgroundResource(R.drawable.normal_bg);
        edtemail = (EditText) getView().findViewById(R.id.edtEmail);
        edtpassword = (EditText) getView().findViewById(R.id.edtpassword);
        tilEmail = (TextInputLayout) getView().findViewById(R.id.tilEmail);
        tilpassword = (TextInputLayout) getView().findViewById(R.id.tilpassword);
        edtemail.addTextChangedListener(new MyTextWatcher(edtemail));
        edtpassword.addTextChangedListener(new MyTextWatcher(edtpassword));
        signin.setOnClickListener(this);
    }


    @Override
    public void onJsonObjectSuccess(JSONObject object) {

        System.out.println("log in res ::" + object.toString());

        SharedPreferences.Editor editor = preferences.edit();

        try {
            JSONObject new_user = object.getJSONObject(Constants.USER);
            editor.putString(Constants.USER_ID, new_user.getString(Constants.USER_ID));
            editor.putString(Constants.NAME, new_user.getString(Constants.NAME));
            editor.putString(Constants.EMAIL, new_user.getString(Constants.EMAIL));
            editor.putString(Constants.ACCESS_TOKEN, new_user.getString(Constants.ACCESS_TOKEN));
            editor.putString(Constants.LOGGED_IN, Constants.LOGGED_IN);
            editor.putString(Constants.PROFILE_PIC, new_user.optString(Constants.IMAGE));
            if (new_user.has(Constants.GENDER))
                editor.putString(Constants.GENDER, new_user.getString(Constants.GENDER));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.apply();
        Intent i = new Intent(getActivity(), HomeActivity.class);
        startActivity(i);
        getActivity().finish();

    }

    @Override
    public void onJsonArrarSuccess(JSONArray array) {


    }


    @Override
    public void onFailure(String str) {

    }

    public void signinclick() {


        if (edtemail.getText().toString().equals("") || edtpassword.getText().toString().equals("")) {
            submitForm();
        } else {

            String url = Constants.WebServices.LOG_IN + "?email=" + edtemail.getText().toString() + "&password=" + edtpassword.getText().toString();
            CallWebService.getInstance(getActivity(), true).hitJSONObjectVolleyWebService(Request.Method.GET, url, null, SignInFragment.this);
        }


    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void submitForm() {


        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        //Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
    }


    private boolean validateEmail() {
        String email = edtemail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            tilEmail.setError(getString(R.string.err_msg_email));
            //requestFocus(edtemail);
            return false;
        } else {
            tilEmail.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validatePassword() {
        if (edtpassword.getText().toString().trim().isEmpty()) {
            tilpassword.setError(getString(R.string.err_msg_password));
            // requestFocus(edtpassword);
            return false;
        } else {
            tilpassword.setErrorEnabled(false);
        }

        return true;
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edtEmail:
                    validateEmail();
                    break;
                case R.id.edtpassword:
                    validatePassword();
                    break;
            }

            if (isValidEmail(edtemail.getText().toString()) && !edtpassword.getText().toString().trim().isEmpty()) {

                signin.setBackgroundResource(R.drawable.pressed_bg);
                signin.setEnabled(true);
            } else {
                signin.setBackgroundResource(R.drawable.normal_bg);
                signin.setEnabled(false);
            }
        }
    }


    @Override
    public void onClick(View v) {
        signinclick();
    }
}