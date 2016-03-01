package com.example.lenovo.bookingapp.Fragments;

/**
 * Created by Mangal on 2/5/2016.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.Toast;

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


public class SignUpFragment extends Fragment implements View.OnClickListener, CallBackInterface {


    SharedPreferences preferences;
    Button signup;
    ConnectionDetector cdr;
    EditText edtxtName, edtemail, edtpassword, cnfedtpassword;
    TextInputLayout inpName, tilEmail, tilpassword, tilcnfpassword;
    View v;
    CommonFunctions commonFunctions;

    public SignUpFragment() {
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

        v = inflater.inflate(R.layout.signup_layout, container, false);
        signup = (Button) v.findViewById(R.id.signup);
        signup.setEnabled(false);
        commonFunctions = new CommonFunctions(getActivity());
        edtxtName = (EditText) v.findViewById(R.id.edtxtName);
        edtemail = (EditText) v.findViewById(R.id.edtxtEmail);
        edtpassword = (EditText) v.findViewById(R.id.edtxtpassword);
        cnfedtpassword = (EditText) v.findViewById(R.id.cnfedtxtpassword);

        inpName = (TextInputLayout) v.findViewById(R.id.inpName);
        tilEmail = (TextInputLayout) v.findViewById(R.id.inpEmail);
        tilpassword = (TextInputLayout) v.findViewById(R.id.inppassword);
        tilcnfpassword = (TextInputLayout) v.findViewById(R.id.inpcnfpassword);
        edtxtName.addTextChangedListener(new MyTextWatcher(edtxtName));
        edtemail.addTextChangedListener(new MyTextWatcher(edtemail));
        edtpassword.addTextChangedListener(new MyTextWatcher(edtpassword));
        cnfedtpassword.addTextChangedListener(new MyTextWatcher(cnfedtpassword));
        signup.setOnClickListener(this);

        return v;
    }

    @Override
    public void onJsonObjectSuccess(JSONObject object) {

        SharedPreferences.Editor editor = preferences.edit();

        try {
            JSONObject new_user = object.getJSONObject(Constants.USER);
            editor.putString(Constants.USER_ID, new_user.getString(Constants.USER_ID));
            editor.putString(Constants.NAME, new_user.getString(Constants.NAME));
            editor.putString(Constants.EMAIL, new_user.getString(Constants.EMAIL));
            editor.putString(Constants.ACCESS_TOKEN, new_user.getString(Constants.ACCESS_TOKEN));
            editor.putString(Constants.LOGGED_IN, Constants.LOGGED_IN);
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

        // Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();

    }

    private boolean validateEmail() {
        String email = edtemail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            tilEmail.setError(getString(R.string.err_msg_email));
            requestFocus(edtemail);
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
            requestFocus(edtpassword);
            return false;
        } else {
            tilpassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePasswords() {
        if (!edtpassword.getText().toString().trim().equals(cnfedtpassword.getText().toString())) {
            tilcnfpassword.setError(getString(R.string.mismatch_password));
            //requestFocus(cnfedtpassword);
            return false;
        } else {
            tilcnfpassword.setErrorEnabled(false);
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
                case R.id.edtxtName:
                    commonFunctions.validateName(edtxtName, inpName);
                    break;
                case R.id.edtxtEmail:
                    validateEmail();
                    break;
                case R.id.edtxtpassword:
                    validatePassword();
                    break;
                case R.id.cnfedtxtpassword:
                    validatePasswords();
                    break;
            }

            if (isValidEmail(edtemail.getText().toString()) && !CommonFunctions.isEmpty(edtxtName) && !CommonFunctions.isEmpty(edtpassword) && !CommonFunctions.isEmpty(cnfedtpassword)) {
                signup.setBackgroundResource(R.drawable.pressed_bg);
                signup.setEnabled(true);
            } else {
                signup.setBackgroundResource(R.drawable.normal_bg);
                signup.setEnabled(false);
            }
        }
    }


    @Override
    public void onClick(View v) {

        submitForm();
    }

    private void submitForm() {

        if (!commonFunctions.validateName(edtxtName, inpName))
            return;
        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        if (!validatePasswords()) {
            return;
        }

        String url = Constants.WebServices.SIGN_UP + "?email=" + edtemail.getText().toString() + "&password=" + edtpassword.getText().toString() + "&name=" + edtxtName.getText().toString();
        CallWebService.getInstance(getActivity(), true).hitJSONObjectVolleyWebService(Request.Method.GET, url, null, SignUpFragment.this);

        //Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}