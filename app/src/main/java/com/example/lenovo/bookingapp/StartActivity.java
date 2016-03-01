package com.example.lenovo.bookingapp;

/**
 * Created by Mangal on 2/5/2016.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.lenovo.bookingapp.Fragments.SignInFragment;
import com.example.lenovo.bookingapp.Fragments.SignUpFragment;
import com.example.lenovo.bookingapp.Utils.CallBackInterface;
import com.example.lenovo.bookingapp.Utils.CallWebService;
import com.example.lenovo.bookingapp.Utils.ConnectionDetector;
import com.example.lenovo.bookingapp.Utils.Constants;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartActivity extends AppCompatActivity implements CallBackInterface {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CallbackManager callbackManager;
    private String accesstkn;
    private TextView forgotpassword;


    SharedPreferences preferences;
    Button signin;
    ConnectionDetector cdr;
    int count = 0;
    String friendListArray = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);


        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.start_screen);

        callbackManager = CallbackManager.Factory.create();
        preferences = MyApplication.preference;
        cdr = new ConnectionDetector(this);
        forgotpassword = (TextView) findViewById(R.id.forgotpassword);
        LoginButton loginButton = (LoginButton) findViewById(R.id.loginButtonFacebook);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        //loginButton.setBackgroundResource(R.drawable.button_fb_selector);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_location", "user_friends"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getMyInformation(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(getResources().getColor(R.color.white_with_alpha), getResources().getColor(android.R.color.white));
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SignInFragment(), getResources().getString(R.string.log_in));
        adapter.addFragment(new SignUpFragment(), getResources().getString(R.string.sign_up));

        viewPager.setAdapter(adapter);
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
            if (new_user.has(Constants.GENDER))
                editor.putString(Constants.GENDER, new_user.getString(Constants.GENDER));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.apply();
        Intent i = new Intent(StartActivity.this, HomeActivity.class);
        startActivity(i);
        finish();

    }

    @Override
    public void onJsonArrarSuccess(JSONArray array) {

    }

    @Override
    public void onFailure(String str) {

        System.out.println("log in res ::" + str);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    public void forgotpassword(View v) {

        Intent i = new Intent(this, ForgotPassword.class);
        startActivity(i);


    }

    private void getMyInformation(AccessToken accessToken) {
        //dialog.show();
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                JSONObject obj = object;
                try {

                    System.out.println("jobj " + obj);
                    final String userId = obj.getString("id");
                    final String name = obj.getString("name");
                    final String gender = obj.getString("gender");
                    final String email = obj.optString("email");


                    // final String dob = obj.getString("birthday");

                    if (gender != null && email != null && !email.isEmpty()) {

                        String currentLocation = "";
                        try {
                            currentLocation = obj.getString("location");
                        } catch (Exception e) {
                            currentLocation = "";
                        }
                        SharedPreferences.Editor e = preferences.edit();
                      /*  e.putBoolean(Constants.IS_FACEBOOK_LOGIN, true);
                        e.putInt(Constants.FACEBOOK_FRIEND_COUNT_PREFS, count);
                        e.putString(Constants.FACEBOOK_FRIEND_ARRAY, friendListArray);*/
                        e.putString(Constants.FB_ID, userId);
                        e.commit();
                        System.out.println("fb details " + email + gender + userId);
                        String url = Constants.WebServices.FB_LOG_IN + "?email=" + email + "&name=" + name + "&fbid=" + userId + "&gender=" + gender;
                        CallWebService.getInstance(StartActivity.this, true).hitJSONObjectVolleyWebService(Request.Method.GET, url, null, StartActivity.this);

                    } else {
                        showAlertDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday,location");
        request.setParameters(parameters);
        request.executeAsync();


        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        try {

                            count = response.getJSONObject().optJSONArray("data").length();
                            friendListArray = response.getJSONObject().getJSONArray("data").toString();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            count = 0;
                        }
                        SharedPreferences.Editor e = preferences.edit();
                        e.putBoolean(Constants.IS_FACEBOOK_LOGIN, true);
                        e.putInt(Constants.FACEBOOK_FRIEND_COUNT_PREFS, count);
                        e.putString(Constants.FACEBOOK_FRIEND_ARRAY, friendListArray);
                        e.commit();
                        System.out.println("frnd " + friendListArray);

                        // response.getRawResponse();
                    }
                }
        ).executeAsync();


    }

    private void showAlertDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(StartActivity.this).create();
        alertDialog.setTitle(getResources().getString(R.string.alert));
        alertDialog.setMessage(getResources().getString(R.string.unable_find_facebook));

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                viewPager.setCurrentItem(1, true);
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor e = preferences.edit();
                e.putBoolean(Constants.IS_FACEBOOK_LOGIN, false);
                e.putInt(Constants.FACEBOOK_FRIEND_COUNT_PREFS, 0);
                e.putString(Constants.FACEBOOK_FRIEND_ARRAY, friendListArray);
                e.commit();
                FacebookSdk.sdkInitialize(getBaseContext());
                LoginManager.getInstance().logOut();
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}