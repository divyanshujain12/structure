package com.example.lenovo.bookingapp.Utils;

/**
 * Created by deii on 13-10-2015.
 */
public class Constants {
    public static final String TRUST_ONE_PREFERENCE = "trust_one";
    /*
    WebService Constants
     */
    public static String ACCESS_TOKEN = "accesstoken";
    public static String EVENT_ID = "eventid";
    public static String EVENTS = "events";
    public static String SUCCESS = "success";
    public static String MESSAGE = "msg";
    public static String EMAIL_ID = "emailid";
    public static String EMAIL = "email";
    public static String NAME = "name";
    public static String USER_ID = "user_id";
    public static String LOGGED_IN = "loggedIn";
    public static String GENDER = "gender";
    public static String USER = "user";
    public static String FACEBOOK_FRIEND_COUNT_PREFS = "pref_facebook_friends_count";
    public static String FACEBOOK_FRIEND_ARRAY = "facebook_friends_array";
    public static String IS_FACEBOOK_LOGIN = "is_facebook_loggedin";
    public static String FB_ID = "fb_id";
    public static String PROFILE_PIC = "profilepic";
    public static String IMAGE = "image";
    public static String USERLAT = "userlat";
    public static String USERLONG = "userlong";
    public static String DATA = "data";

    public interface webServiceSendKeys {
        public static String EMAIL_ID = "EmailID";
        public static String SUB_CATEGORY_ID = "subcategory_id";
    }

    public interface WebServices {

        //public static String BASE = "http://whatsupguys.in/demo/trust1_api/api/";

        public static String BASE = "http://119.9.108.137/bookingapp/";

        public static String SIGN_UP = BASE + "signup.php";

        public static String LOG_IN = BASE + "signin.php";

        public static String FB_LOG_IN = BASE + "fbsignup.php";

        public static String HOME = BASE + "home";

        public static String EVENTS = BASE + "events.php";

        public static String USER_EVENTS = BASE+"userevents.php?accesstoken=8dd9294632aeed1b62a6bc2eba8b41c5";

    }
}
