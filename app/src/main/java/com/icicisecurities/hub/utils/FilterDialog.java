package com.icicisecurities.hub.utils;

import android.app.Activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.icicisecurities.hub.R;

import java.util.Calendar;

public class FilterDialog implements View.OnClickListener {

    //High priority UI variables goes below....
    private Dialog dialog;
    private Button mOKBtn, mSelectFromDateBtn, mSelectToDateBtn;

    //Medium priority NON-UI variables goes below....
    private static Context context;//Bcoz this is used to open & close SharedPrefernece by 2 static methods...
    private Object instanceOfClass;
    private FragmentTransaction fragmentTransaction;
    private String DATE_PICKER_FRAGMENT_TAG = "DATE_PICKER_FRAGMENT_TAG";
    private FilterLeadsListener mFilterLeadsListener;
    private static String currentDate;//This is static bcoz we return the Date to the Calling class....

    private final static String LEADS_FILTER_FROM_DATE = "LEADS_FILTER_FROM_DATE";
    private final static String LEADS_FILTER_TO_DATE = "LEADS_FILTER_TO_DATE";

    /**@LEADS_FILTER_FROM_DATE & @LEADS_FILTER_TO_DATE:
     *
     * These values are stored in SharedPrfernces.
     * The To Date & From Date are stored using these keys..
     *
     * Now, never use these keys to extract the dates.
     * instead use getLeadsFilter_FromDate, getLeadsFilter_ToDate respectively.
     *
     * **/



    //Least priority variables goes below...
    private final static String TAG = "FilterDialog";

    public FilterDialog(Context context, Object instanceOfClass) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.instanceOfClass = instanceOfClass;
    }//FilterDialog constructor closes here....

    public void showDialog(){
        dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_filter_dates);
//		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.setCancelable(true);



        //Initializing the Dialog's Widgets....
        mSelectFromDateBtn = (Button) dialog.findViewById(R.id.selectFromDateBtn);
        mSelectToDateBtn = (Button) dialog.findViewById(R.id.selectToDateBtn);
        mOKBtn = (Button) dialog.findViewById(R.id.okBtn);

        //Generating Events...
        mSelectFromDateBtn.setOnClickListener(FilterDialog.this);
        mSelectToDateBtn.setOnClickListener(FilterDialog.this);
        mOKBtn.setOnClickListener(FilterDialog.this);

        //Setting data on the UI...
        //Whenever this Fragment is inflated, the Current Date shud be setted on the Buttons....
        mSelectFromDateBtn.setText(FilterDialog.getLeadsFilter_FromDate());
        mSelectToDateBtn.setText(FilterDialog.getLeadsFilter_ToDate());


        dialog.show();
    }//showDialog closes here....

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.selectFromDateBtn:
                //Display the Date Picker Dialog....
//                DialogFragment fromDatePickerFragment = new DatePickerDialogFragment(context, mSelectFromDateBtn, SELECT_RANGE_FOR_OPEN_CLOSED_CASES.FROM_DATE);
//                fromDatePickerFragment.show(((Activity) context).getFragmentManager(), DATE_PICKER_FRAGMENT_TAG);
                break;

            case R.id.selectToDateBtn:
                //Display the Date Picker Dialog....
//                DialogFragment toDatePickerFragment = new DatePickerDialogFragment(context, mSelectToDateBtn, SELECT_RANGE_FOR_OPEN_CLOSED_CASES.TO_DATE);
//                toDatePickerFragment.show(((Activity)context).getFragmentManager(), DATE_PICKER_FRAGMENT_TAG);
                break;

            case R.id.okBtn:
                dialog.dismiss();

                //Setting the Filter below...
                mFilterLeadsListener.filterLeadsListener(true);
                break;

            default:
                break;
        }//switch (v.getId()) closes here...
    }//onClick closes here....


    public static String getLeadsFilter_FromDate(){
//		PreferenceManager.getDefaultSharedPreferences(context).
        if(context == null){
            //User has not edited the From & To date till now....
            Calendar calendar = AppUtils.getCalendarInstance(context);
            currentDate = AppUtils.formatDate(context, null, calendar);
//			Log.d(TAG, "Returning Current Date = "+currentDate);
            return currentDate;
        }//if(context == null) closes here....
        else{
            if(PreferenceManager.getDefaultSharedPreferences(context).contains(LEADS_FILTER_FROM_DATE))
                return PreferenceManager.getDefaultSharedPreferences(context).getString(LEADS_FILTER_FROM_DATE, null);
            else{
                Log.d(TAG, "From Date does not exists in SharedPreference...");
                Calendar calendar = AppUtils.getCalendarInstance(context);
                currentDate = AppUtils.formatDate(context, null, calendar);
                return currentDate;
            }
        }//else context is not null closes here...
    }//getFromDate closes here....


    public static String getLeadsFilter_ToDate(){
//		PreferenceManager.getDefaultSharedPreferences(context).
        if(context == null){
            //User has not edited the From & To date till now....
            Calendar calendar = AppUtils.getCalendarInstance(context);
            currentDate = AppUtils.formatDate(context, null, calendar);
//			Log.d(TAG, "Returning Current Date = "+currentDate);
            return currentDate;
        }//if(context == null) closes here....
        else{
            if(PreferenceManager.getDefaultSharedPreferences(context).contains(LEADS_FILTER_TO_DATE))
                return PreferenceManager.getDefaultSharedPreferences(context).getString(LEADS_FILTER_TO_DATE, null);
            else{
                Log.d(TAG, "From Date does not exists in SharedPreference...");
                Calendar calendar = AppUtils.getCalendarInstance(context);
                currentDate = AppUtils.formatDate(context, null, calendar);
                return currentDate;
            }
        }//else context is not null closes here...

    }//getFromDate closes here....

    public static void setLeadsFilter_FromDate(String formattedDate) {
        // TODO Auto-generated method stub
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(LEADS_FILTER_FROM_DATE, formattedDate).apply();
    }//setLeadsFilter_FromDate closes here....



    public static void setLeadsFilter_ToDate(String formattedDate) {
        // TODO Auto-generated method stub
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(LEADS_FILTER_TO_DATE, formattedDate).apply();
    }//setLeadsFilter_ToDate closes here....

    public void setFilterListener(FilterLeadsListener filterListenerInstance) {
        // TODO Auto-generated method stub
        this.mFilterLeadsListener = filterListenerInstance;
    }//setFilterListener closes here....
}//FilterDialog closes here....


