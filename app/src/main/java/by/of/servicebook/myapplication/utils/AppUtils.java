package by.of.servicebook.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Pavel on 28.04.2015.
 */
public class AppUtils {
    private static final String APP_PREFERENCES = "app_preferences";
    private static final String APP_TIMESTAMP = "app_timestamp";

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else
            return true;
    }

    public static void setTimeStamp(Context context, long timeStamp){
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(APP_TIMESTAMP, timeStamp);
        editor.commit();
    }

    public static long getTimeStamp(Context context){
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getLong(APP_TIMESTAMP, 0);
    }

    public static long parseDate(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date convertedDate;
        try {
            convertedDate = dateFormat.parse(date);
            return convertedDate.getTime();
        } catch (ParseException e) {
            Log.e("parseDate", "Warning! Parse date EXCEPTION: " + date);
        }
        return -1;
    }

    public static String dateToString(long time){
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
