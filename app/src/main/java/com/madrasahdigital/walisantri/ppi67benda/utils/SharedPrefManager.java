package com.madrasahdigital.walisantri.ppi67benda.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.madrasahdigital.walisantri.ppi67benda.model.allsantri.AllSantri;
import com.madrasahdigital.walisantri.ppi67benda.model.notification.ListNotificationArticlesModel;
import com.madrasahdigital.walisantri.ppi67benda.model.notification.NotificationArticlesModel;
import com.madrasahdigital.walisantri.ppi67benda.model.notification.NotificationModel;
import com.madrasahdigital.walisantri.ppi67benda.model.tagihanallsantri.TagihanAllSantriModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private final String KEY_TOKEN = "tokenku";
    private final String KEY_ALL_SANTRI = "allsantri321";
    private final String KEY_ID_ACTIVE_SANTRI_IN_HOMEPAGE = "santriactive231";
    private final String KEY_DATA_NOTIF = "datanotif323";
    private final String KEY_TOTAL_TAGIHAN = "tottagihan21";
    private final String KEY_TAGIHAN_ALL_SANTRI = "tagihanallsantri32";
    private final String KEY_IS_GUIDE_SHOWED = "isguideshw32";
    private final String KEY_SHOW_GET_LATEST_VERSION = "gtltsttvers12";
    private final String KEY_IS_SET_ALARM = "key_is_set_alarm";
    private final String KEY_CURRENT_DATE = "key_current_date";
    private final String KEY_DATA_ARTICLES_NOTIFICATION = "key_data_articles_notification";

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

    public void setShowGetLatestVersionApp(boolean doShow) {
        editor = pref.edit();
        editor.putBoolean(KEY_SHOW_GET_LATEST_VERSION, doShow);
        editor.apply();
    }

    public boolean getStatusShowVersionApp() {
        return pref.getBoolean(KEY_SHOW_GET_LATEST_VERSION, false);
    }

    public String getToken() {
        return pref.getString(KEY_TOKEN, "");
    }

    public void setToken(String token) {
        editor = pref.edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String getIdActiveSantriInHomePage() {
        return pref.getString(KEY_ID_ACTIVE_SANTRI_IN_HOMEPAGE, "");
    }

    public void setIdActiveSantriInHomePage(String idActiveSantri) {
        editor = pref.edit();
        editor.putString(KEY_ID_ACTIVE_SANTRI_IN_HOMEPAGE, idActiveSantri);
        editor.apply();
    }

    public String getTotalTagihan() {
        return pref.getString(KEY_TOTAL_TAGIHAN, "0");
    }

    public void setTotalTagihan(String totalTagihan) {
        editor = pref.edit();
        editor.putString(KEY_TOTAL_TAGIHAN, totalTagihan);
        editor.apply();
    }

    public void saveNotificationList(List<NotificationModel> notificationModels) {
        editor = pref.edit();
        Gson gson = new Gson();
        String jsonChat = gson.toJson(notificationModels);
        editor.putString(KEY_DATA_NOTIF, jsonChat);
        editor.apply();
    }

    public ArrayList<NotificationModel> getNotificationList() {
        List<NotificationModel> item;

        if (pref.contains(KEY_DATA_NOTIF)) {
            String jsonChat = pref.getString(KEY_DATA_NOTIF, null);
            Gson gson = new Gson();
            NotificationModel[] ChatItems = gson.fromJson(jsonChat,
                    NotificationModel[].class);

            item = Arrays.asList(ChatItems);
            item = new ArrayList<>(item);
        } else
            return null;

        return (ArrayList<NotificationModel>) item;
    }


    public void saveAllSantri(AllSantri allSantri) {
        Gson gson = new Gson();
        String jsonUser = gson.toJson(allSantri);

        editor = pref.edit();
        editor.putString(KEY_ALL_SANTRI, jsonUser);
        editor.apply();
    }

    public AllSantri getAllSantri() {
        String jsonUser = pref.getString(KEY_ALL_SANTRI, "");

        Gson gson = new Gson();
        AllSantri allSantri = gson.fromJson(jsonUser,
                AllSantri.class);

        return allSantri;
    }

    public void saveTagihanAllSantri(TagihanAllSantriModel tagihanAllSantriModel) {
        Gson gson = new Gson();
        String jsonUser = gson.toJson(tagihanAllSantriModel);

        editor = pref.edit();
        editor.putString(KEY_TAGIHAN_ALL_SANTRI, jsonUser);
        editor.apply();
    }

    public TagihanAllSantriModel getTagihanAllSantri() {
        String jsonUser = pref.getString(KEY_TAGIHAN_ALL_SANTRI, "");

        Gson gson = new Gson();
        TagihanAllSantriModel tagihanAllSantriModel = gson.fromJson(jsonUser,
                TagihanAllSantriModel.class);

        return tagihanAllSantriModel;
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor = pref.edit();
        editor.putBoolean(KEY_IS_GUIDE_SHOWED, isFirstTime);
        editor.apply();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(KEY_IS_GUIDE_SHOWED, true);
    }

    public void resetData() {
        editor = pref.edit();
        editor.clear();
        editor.apply();
    }

    public void setAlarm() {
        editor = pref.edit();
        editor.putBoolean(KEY_IS_SET_ALARM, true);
        editor.apply();
    }

    public boolean isSetAlarm() {return  pref.getBoolean(KEY_IS_SET_ALARM, false);}


    public void addArticleNotification(NotificationArticlesModel notificationArticlesModel){
        ArrayList<NotificationArticlesModel> notificationArticlesModelArrayList = new ArrayList<>();
        String dataArticles;
        ListNotificationArticlesModel listNotificationArticlesModel = new ListNotificationArticlesModel();
        if (getNotificationsArticles() != null){
            getNotificationsArticles().add(notificationArticlesModel);
            dataArticles = new Gson().toJson(getNotificationsArticles());
        }else {
            notificationArticlesModelArrayList.add(notificationArticlesModel);
            listNotificationArticlesModel.setListNotificationArticles(notificationArticlesModelArrayList);
            dataArticles = new Gson().toJson(listNotificationArticlesModel);
        }
        editor = pref.edit();
        editor.putString(KEY_DATA_ARTICLES_NOTIFICATION, dataArticles);
        editor.apply();
    }

    public List<NotificationArticlesModel> getNotificationsArticles(){
        String jsonDataArticles = pref.getString(KEY_DATA_ARTICLES_NOTIFICATION, "");
        ListNotificationArticlesModel listNotificationArticlesModel;
        if (jsonDataArticles.isEmpty()){
            return null;
        }else {
            listNotificationArticlesModel = new Gson().fromJson(jsonDataArticles, ListNotificationArticlesModel.class);
        }

        return listNotificationArticlesModel.getListNotificationArticles();
    }

    public void deleteArticles(){
        editor = pref.edit();
        editor.remove(KEY_DATA_ARTICLES_NOTIFICATION);
        editor.apply();
    }

    public void setLastAccessDate(String currentDate){
        editor = pref.edit();
        editor.putString(KEY_CURRENT_DATE, currentDate);
        editor.apply();
    }

    public String getLastAccessDate(){
        return pref.getString(KEY_CURRENT_DATE, "");
    }

    public void deleteLastAccessDate(){
        editor = pref.edit();
        editor.remove(KEY_CURRENT_DATE);
        editor.apply();
    }
}
