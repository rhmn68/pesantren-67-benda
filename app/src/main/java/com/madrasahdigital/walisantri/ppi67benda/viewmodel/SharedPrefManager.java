package com.madrasahdigital.walisantri.ppi67benda.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Alhudaghifari on 5:42 26/01/19
 */
public class SharedPrefManager {
    // LogCat tag
    private String TAG = SharedPrefManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    private final String KEY_IS_LOGGED_IN = "isLoggedIn";

    // Shared preferences file name
    private final String PREF_NAME = Constant.this_app;

    /**
     * constructor session manager wajib mengirim context aktivitas
     * @param context context activity
     */
    public SharedPrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * procedure untuk melakukan set apakah sudah login atau belum.
     * isLoggedIn bernilai true ketika berhasil melakukan login.
     * isLoggedIn bernilai false ketika dalam kondisi logout.
     * @param isLoggedIn boolean
     */
    public void setLogin(boolean isLoggedIn) {
        editor = pref.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
        Log.d(TAG, "User login session modified!");
    }
    /**
     * function untuk melihat apakah sudah login atau belum
     * true : sudah
     * false : tidak login
     * @return boolean login
     */
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void resetData() {
        editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
