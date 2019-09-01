package com.madrasahdigital.walisantri.ppi67benda.utils;

/**
 * Created by Alhudaghifari on 21:51 26/04/19
 */
public class Constant {

    public static final String this_app = "com.madrasahdigital.walisantri";
    public static final String TAG = "67Benda";

    private static final String SERVER_LINK_OLD = "http://pesantrenbenda.cloudapp.web.id/";
    private static final String SERVER_LINK = "https://www.67benda.com/";

    public static final String LINK_REGISTER = SERVER_LINK + "api/user/auth/register";
    public static final String LINK_LOGIN = SERVER_LINK + "api/user/auth/login";
    public static final String LINK_FORGOT_PASSWORD = SERVER_LINK + "api/user/auth/reset_password";
    public static final String LINK_ASSIGN_SANTRI = SERVER_LINK + "api/santri/assign";
    public static final String LINK_GET_ALL_SANTRI = SERVER_LINK + "api/santri";
    public static final String LINK_GET_PAYMENT_INFO = SERVER_LINK + "api/tagihan/payment";
    public static final String LINK_GET_RIWAYAT_SPP = SERVER_LINK + "api/santri/1/spp";
    public static final String LINK_GET_TAGIHAN_SPP = SERVER_LINK + "api/spp/unpaid";
    public static final String LINK_GET_PRESENCE_TODAY = SERVER_LINK + "api/santri/*/presensi/"; // add date in the last, example api/santri/1/presensi/20190515
    public static final String LINK_GET_PRESENCE_SANTRI = SERVER_LINK + "api/santri/*/presensi?year=$&month=#"; //replace $ with year and # with month (05 for May)
    public static final String LINK_GET_NOTIFICATION = SERVER_LINK + "api/notification";
    public static final String LINK_GET_NEWS = SERVER_LINK + "api/post";
    public static final String LINK_GET_TAGIHAN_ALL_SANTRI = SERVER_LINK + "api/tagihan";
    public static final String LINK_POST_TAGIHAN_PAYMENT = SERVER_LINK + "api/tagihan/payment";
    public static final String LINK_POST_OTP = SERVER_LINK + "api/user/auth/confirm_otp";
    public static final String LINK_GET_OTP = SERVER_LINK + "api/user/auth/resend_otp/$"; // replace $ with id user
    public static final String LINK_GET_LATEST_VERSION = SERVER_LINK + "api/setting/presensi.app_version";
    public static final String LINK_GET_ABOUT_DETAIL = SERVER_LINK + "api/variable/tentang_aplikasi";
    public static final String LINK_GET_IMAGE_BANNER = SERVER_LINK + "api/entry/index/slider?orderby=order&direction=asc&filter[status]=1";
    // add date in the last, example api/santri/1/presensi/20190515
    // and change $ to id santri
    public static final String LINK_GET_PRESENCE_TODAY_2 = SERVER_LINK + "api/santri/$/presensi/";

    public static final String Authorization = "Authorization";

    public static final String URL_HOW_TO_PAY = "https://www.67benda.com/carapembayaran";
    public static final String URL_APP_IN_PLAYSTORE = "https://play.google.com/store/apps/details?id=com.madrasahdigital.walisantri.ppi67benda";

    public static final int TIMEOUT = 180;
    public static final int TIMEOUT_2 = 60;

    public static final int TYPE_NEWS_HOME = 0;
    public static final int TYPE_NEWS_PAGE = 1;

    public static final int TYPE_HOME = 1;
    public static final int TYPE_WELCOME_MSG = 2;

}
