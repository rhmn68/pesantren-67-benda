package com.madrasahdigital.walisantri.ppi67benda.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.widget.Toast;

import com.madrasahdigital.walisantri.ppi67benda.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alhudaghifari on 14:09 12/05/19
 */
public class UtilsManager {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static Drawable getRotateDrawable(final Drawable d, final float angle) {
        final Drawable[] arD = { d };
        return new LayerDrawable(arD) {
            @Override
            public void draw(final Canvas canvas) {
                canvas.save();
                canvas.rotate(angle, (float) d.getBounds().width() / 2, (float) d.getBounds().height() / 2);
                super.draw(canvas);
                canvas.restore();
            }
        };
    }

    public static String getDateAnotherFormatFromString2(String myDate) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(myDate);
            DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateAnotherFormatFromString(String myDate) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(myDate);
            DateFormat dateFormat = new SimpleDateFormat("HH:mm dd MMMM yyyy");
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getDayFromString(String myDate) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(myDate);
            DateFormat dateFormat = new SimpleDateFormat("dd");
            return Integer.parseInt(dateFormat.format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getMonthFromString(String myDate) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(myDate);
            DateFormat dateFormat = new SimpleDateFormat("MM");
            return Integer.parseInt(dateFormat.format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getYearFromString(String myDate) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(myDate);
            DateFormat dateFormat = new SimpleDateFormat("yyyy");
            return Integer.parseInt(dateFormat.format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static String getYear() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getMonth() {
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(yesterday());
    }

    public static String getTodayDateStringMonthLinguistik(Context context) {
        String myDate = "";
        DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        myDate = dateFormat.format(date) + " ";
        myDate += getMonthFromNumber(context, getMonth()) + " ";
        myDate += getYear();
        return myDate;
    }

    public static String getTodayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();

        return dateFormat.format(date);
    }

    public static String getRupiahMoney(String amount) {
        String rupiah = "Rp " + convertLongToCurrencyIDv2WithoutRp(Double.valueOf(amount));
        return rupiah;
    }

    public static String convertLongToCurrencyIDv2WithoutRp(double amount){
        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
        return n.format(amount).replace("$", "").replaceAll("\\,", "#").replaceAll("\\.", ",").replaceAll("\\#", ".").replace(",00", "");
    }

    public static String getMonthNumberFromMonthNameString(String monthName) {
        if (monthName.equals("January") || monthName.equals("Januari")) {
            return "00";
        } else if (monthName.equals("February") || monthName.equals("Februari")) {
            return "01";
        } else if (monthName.equals("March") || monthName.equals("Maret")) {
            return "02";
        } else if (monthName.equals("April")) {
            return "03";
        } else if (monthName.equals("May") || monthName.equals("Mei")) {
            return "04";
        } else if (monthName.equals("June") || monthName.equals("Juni")) {
            return "05";
        } else if (monthName.equals("July") || monthName.equals("Juli")) {
            return "06";
        } else if (monthName.equals("August") || monthName.equals("Agustus")) {
            return "07";
        } else if (monthName.equals("September")) {
            return "08";
        } else if (monthName.equals("October") || monthName.equals("Oktober")) {
            return "09";
        } else if (monthName.equals("November")) {
            return "10";
        } else if (monthName.equals("December") || monthName.equals("Desember")) {
            return "11";
        }
        return "-";
    }

    public static String getMonthFromNumber(Context context, String numString) {
        int num = Integer.parseInt(numString);
        switch (num) {
            case 1:
                return context.getResources().getString(R.string.bln_januari);
            case 2:
                return context.getResources().getString(R.string.bln_februari);
            case 3:
                return context.getResources().getString(R.string.bln_maret);
            case 4:
                return context.getResources().getString(R.string.bln_april);
            case 5:
                return context.getResources().getString(R.string.bln_mei);
            case 6:
                return context.getResources().getString(R.string.bln_juni);
            case 7:
                return context.getResources().getString(R.string.bln_juli);
            case 8:
                return context.getResources().getString(R.string.bln_agustus);
            case 9:
                return context.getResources().getString(R.string.bln_september);
            case 10:
                return context.getResources().getString(R.string.bln_oktober);
            case 11:
                return context.getResources().getString(R.string.bln_november);
            case 12:
                return context.getResources().getString(R.string.bln_desember);
        }
        return "";
    }

    /**
     * @param s
     * @return md5 password
     */
    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void showToast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }
}
