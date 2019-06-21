package com.madrasahdigital.walisantri.ppi67benda.utils;

/**
 * Created by Alhudaghifari on 21:51 26/04/19
 */
public class Constant {
    public static final String this_app = "com.madrasahdigital.walisantri";
    private static final String SERVER_LINK = "http://pesantrenbenda.cloudapp.web.id/";

    public static final String LINK_REGISTER = SERVER_LINK + "api/user/auth/register";
    public static final String LINK_LOGIN = SERVER_LINK + "api/user/auth/login";
    public static final String LINK_FORGOT_PASSWORD = SERVER_LINK + "api/user/auth/reset_password";
    public static final String LINK_ASSIGN_SANTRI = SERVER_LINK + "api/santri/assign";
    public static final String LINK_GET_ALL_SANTRI = SERVER_LINK + "api/santri";
    public static final String LINK_GET_PAYMENT_INFO = SERVER_LINK + "api/payment";
    public static final String LINK_GET_RIWAYAT_SPP = SERVER_LINK + "api/santri/1/spp";
    public static final String LINK_GET_TAGIHAN_SPP = SERVER_LINK + "api/spp/unpaid";
    public static final String LINK_GET_PRESENCE_TODAY = SERVER_LINK + "api/santri/1/presensi/"; // add date in the last, example api/santri/1/presensi/20190515
    public static final String LINK_GET_PRESENCE_SANTRI = SERVER_LINK + "api/santri/1/presensi?year=$&month=#"; //replace $ with year and # with month (05 for May)
    public static final String LINK_GET_NOTIFICATION = SERVER_LINK + "api/notification";
    public static final String LINK_GET_TAGIHAN_ALL_SANTRI = SERVER_LINK + "api/tagihan";
    // add date in the last, example api/santri/1/presensi/20190515
    // and change $ to id santri
    public static final String LINK_GET_PRESENCE_TODAY_2 = SERVER_LINK + "api/santri/$/presensi/";

    public static final String Authorization = "Authorization";

    public static final int TYPE_NEWS_HOME = 0;
    public static final int TYPE_NEWS_PAGE = 1;

}
