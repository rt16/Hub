package com.icicisecurities.hub.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import com.icicisecurities.hub.R;
import com.icicisecurities.hub.connection.NetworkResponseListener;
import com.icicisecurities.hub.connection.Params_POJO;
import com.icicisecurities.hub.connection.PostResponseAsync;
import com.icicisecurities.hub.login.LoginActivity;


/**
 * Created by Ganesh Ghodake on 11/01/17.
 */
public class SessionLogoutDialog implements NetworkResponseListener {

    //High priority UI variables goes below...
    private Context context;


    //Medium priority NON-UI variables goes below....
    private Object instanceOfClass;
    private String msg;

    //Least priority varibales goes below...
    private final String TAG = "Logout_Dialog";

    public SessionLogoutDialog(Context context, Object instanceOfClass, String msg) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.instanceOfClass = instanceOfClass;
        this.msg = msg;

        showDialog();
    }//Logout_Dialog constructor closes here....

    private void showDialog() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getString(R.string.ok_spelling), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
//				new Logout(context, contactID, sessionID);

                //Added by vikas on 16 Dec 16 as per logout requirement.......
                Params_POJO pojo = new Params_POJO();
                TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

//                String IMEI = telephonyManager.getDeviceId();

                String url = AppConstants.URL+AppConstants.LOGIN_URL;
                pojo.setUrl(url);

                //LOGGED_IN_USER_ID is same for both Userid and Password during Logout sending...

                String UserId= PreferenceManager.getDefaultSharedPreferences(context).getString(AppConstants.LOGGED_IN_USER_ID, "");

                String data = "NTId="+UserId+"&NTPass="+UserId+"&IMEI="+"NA";
                //Update : 04-06-2016.....Since this code/build is gng LIVE, so we will send the version to the Login API. such that in future they shud recognize the activities.....
                //& ofcourse the mrkting ppl will for sure do mistake in installing the build, so we have to recognize easily that which build is used by the mrkting ppl to report the bug.
                data += "&version="+AppUtils.getVersionName(context,this)+"&flag=O";
                pojo.setData(data);

                PostResponseAsync postAsync = new PostResponseAsync(context,SessionLogoutDialog.this);
                postAsync.setResponseListener(SessionLogoutDialog.this);
                postAsync.execute(pojo);

                context.startActivity(new Intent(context, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

                AppUtils.givefinishEffect(context);
            }//onClick closes here....
        });//builder.setPositiveButton("Logout", new DialogInterface.OnClickListener()  closes here....

        builder.create();
        builder.show();

    }//showDialog closes here....

    @Override
    public void notifyNetworkResponseSuccess(String response) {

    }

    @Override
    public void notifyNetworkResponseFailure(Exception exception, String response) {

    }
}//SessionLogoutDialog closes hrere...
