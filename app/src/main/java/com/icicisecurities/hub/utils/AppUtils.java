package com.icicisecurities.hub.utils;

import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.snackbar.Snackbar;
import com.icicisecurities.hub.HomeActivity;
import com.icicisecurities.hub.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.prefs.PreferenceChangeEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {

    private final static String TAG = "AppUtils";

    public static void showAlertDialog(Context context, Object instanceOfClass, String message) {
        // TODO Auto-generated method stub

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(context.getResources().getString(R.string.app_name));
//			builder.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
        builder.setPositiveButton(context.getResources().getString(R.string.ok_spelling), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {}//onClick closes here....
        });//builder.setPositiveButton closes here....

        builder.create();
        builder.show();
    }//showAlertDialog closes here....

    public static String getVersionName(Context context, Object instanceOfClass) {
        // TODO Auto-generated method stub
        String versionName = null;
        try{
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        }//try closes here....
        catch(Exception e){
            Log.d(TAG, e.getMessage());
            Log.e(TAG, "Exception while fethcing the version number of the Build : "+e);
        }//catch closes here.....
        return versionName;
    }//getVersionName closes here....

    public static void giveIntentEffect(Context context){

        Activity activity = (Activity) context;

        activity.overridePendingTransition(R.anim.intent_in_slide_in_left, R.anim.intent_in_slide_out_left);
    }//intentInEffect closes here....



    public static void givefinishEffect(Context context){
        Activity activity = (Activity) context;

        activity.overridePendingTransition(R.anim.intent_out_slide_in_left, R.anim.intent_out_slide_out_left);
    }//givefinishEffect closes here....

    public static void showSnackBar(Context context , View anchorView , String message , int length){
        InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View view = ((Activity)context).getCurrentFocus();
            if(view != null){
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        Snackbar snackbar = Snackbar.make(anchorView , message , length);
        snackbar.show();
    }

    public static boolean isPANValid(String panNumber){

/*
        1) The first three letters are sequence of alphabets from AAA to zzz
        2) The fourth character informs about the type of holder of the Card. Each assesse is unique:`

    C — Company
    P — Person
    H — HUF(Hindu Undivided Family)
    F — Firm
    A — Association of Persons (AOP)
    T — AOP (Trust)
    B — Body of Individuals (BOI)
    L — Local Authority
    J — Artificial Judicial Person
    G — Government


      3) The fifth character of the PAN is the first character
    (a) of the surname / last name of the person, in the case of
          a "Personal" PAN card, where the fourth character is "P" or
    (b) of the name of the Entity/ Trust/ Society/ Organisation
          in the case of Company/ HUF/ Firm/ AOP/ BOI/ Local Authority/ Artificial Jurdical Person/ Govt,
          where the fourth character is "C","H","F","A","T","B","L","J","G".

      4) The last character is a alphabetic check digit.

      */

//        Pattern pattern = Pattern.compile("[A-Z]{3}[ABCFGHLJPTF]{1}[A-Z]{1}[0-9]{4}[A-Z]{1}");   // validation for all CHFATBLJG
        Pattern pattern = Pattern.compile("[a-zA-Z]{3}[pP]{1}[a-zA-Z]{1}[0-9]{4}[a-zA-Z]{1}"); // validation only for P
//        Pattern pattern = Pattern.compile("[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}"); // no validation

        Matcher matcher = pattern.matcher(panNumber );
        return matcher.matches();
    }

    public static boolean isEmailValid(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static Spanned mandatorySpanned(Context context, Object instanceOfClass, String normalFontString, String smallFontString){

        Spanned txt = Html.fromHtml("<font color='#D72022'>*</font>    <font color='#000000'>"+normalFontString+"</font> <font size=\"0.5\">"+smallFontString+"</font>");

        return txt;
    }//mandatorySpanned closes here.....

    public static Spanned blackBoldSpanned(Context context, Object instanceOfClass, String boldString, String unboldString){
        Spanned txt = Html.fromHtml("<b><font color = '"+context.getResources().getColor(android.R.color.black)+"'>"+boldString+"</font></b>"+unboldString);

        return txt;
    }//blackBoldSpanned closes here....

    public static Spanned blackRegularSpanned(Context context, Object instanceOfClass, String boldString, String unboldString){
        Spanned txt = Html.fromHtml("<font color = '"+context.getResources().getColor(android.R.color.black)+"'>"+boldString+"</font>"+unboldString);

        return txt;
    }//blackRegularSpanned closes here....

    public static boolean chkWhetherUserIsMinorOrNot(String usersEnteredDate_String){


        //We will fetch the DOB entered by the user add 18 yrs in it & compare whether it is greater than today's date or not !!!!

        //Step 1 : Get Current Date....
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(AppConstants.APP_DATE_FORMAT);
        String currentDate_String = df.format(c.getTime());


        //Step 2 : Get Entered Date.....
        SimpleDateFormat formatter = new SimpleDateFormat(AppConstants.APP_DATE_FORMAT);
        Date userEnteredDate_date = null, currentDate_date = null;
        try {
            currentDate_date = formatter.parse(currentDate_String);
            userEnteredDate_date = formatter.parse(usersEnteredDate_String);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            Log.d(TAG, e.getMessage());
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(userEnteredDate_date);
        cal.add(Calendar.YEAR, 18);//Added 18 yrs here.....
//	        return cal.getTime();


        //Since cal gives date with a different format therefore we are again changing the dateformat.
//	        try {
//				userEnteredDate_date = formatter.parse(cal.getTime().toString());
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				Log.d(TAG, e.getMessage());
//			}

        if(cal.getTime().after(currentDate_date)){
//	        	Log.d(TAG,"Calculated time is after Current Date....");

            //Calculated time us after the current Date....So the user is definitely a Minor.
            return true;
        }//if calculated time is after the current date
        else if(cal.getTime().equals(currentDate_date)){
//	        	Log.d(TAG, "Calculated time is same as Current Date.....");
            //Calculated time is same as that of current date i.r. today is Minor's Happy B'day (But still user is a Minor)
            return true;
        }//else if today is Minor's Happy B'day....
//	        else if(cal.getTime().before(currentDate_date)){
//	        	Log.d(TAG, "Calculated time is before the Current Date.....");
        else{
            //Calculated time is before the current Date i.e. the user is not a Minor....
            return false;
        }

    }//chkWhetherUserIsMinorOrNot closes here.....

    public static String convertProperDateFormat(String dateFromPANCard) throws ParseException {
        // TODO Auto-generated method stub

        SimpleDateFormat sourceSDF = new SimpleDateFormat(AppConstants.PAN_DATE_FORMAT);
        Date sourceDate = sourceSDF.parse(dateFromPANCard);//This will convert String to date....
        SimpleDateFormat destinationSDF = new SimpleDateFormat(AppConstants.APP_DATE_FORMAT);
        return destinationSDF.format(sourceDate);
    }//convertProperDateFormat closes here....

    public static Integer getScreenWidth(Context context) {
        // TODO Auto-generated method stub

        //Calculating Height of the Login Btns dynamically according to Screen size....
        Rect rect =  new Rect();
        Window win = ((Activity)context).getWindow();
        win.getDecorView().getWindowVisibleDisplayFrame(rect);

        // By now we got the height of titleBar & statusBar
        // Now lets get the screen size
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return metrics.widthPixels;
    }//getScreenWidth closes here....

    public static Calendar getCalendarInstance(Context context){
        return Calendar.getInstance();
    }//getCalendarInstance closes here....

    public static String formatDate(Context context, Object instanceOfClass, Calendar cal) {
        // TODO Auto-generated method stub

        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.APP_DATE_FORMAT, Locale.US);

        String formatted = sdf.format(cal.getTime());
        Log.d(TAG, "Formatted Date = "+formatted);
        // Output "13-Nov-2015"
        return formatted;
    }//formatDate closes here...

    public static void clearPreferences(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
    }
}
