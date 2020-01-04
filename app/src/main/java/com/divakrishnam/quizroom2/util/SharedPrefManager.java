package com.divakrishnam.quizroom2.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static SharedPrefManager mInstance;
    private static Context mContext;

    private static final String SHARED_PREF_NAME = "QuizRoom2";

    private static final String KEY_USERID = "KeyUserId";

    private SharedPrefManager(Context context){
        mContext = context;
        pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void mahasiswaLogin(int userId){
        editor.putInt(KEY_USERID, userId);
        editor.apply();
    }

    public boolean isLoggedIn(){
        return pref.getInt(KEY_USERID, 0) != 0;
    }

    public int getUserId(){
        return pref.getInt(KEY_USERID, 0);
    }

    public void logout(){
        editor.clear();
        editor.apply();
    }

}
