package com.icicisecurities.hub.utils;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import com.icicisecurities.hub.R;
import com.icicisecurities.hub.login.LoginActivity;


public class LogoutDialog {

    private Context context;
    private Object instanceOfClass;


    public LogoutDialog(Context context, Object instanceOfClass) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.instanceOfClass = instanceOfClass;

        showDialog();
    }//Logout_Dialog constructor closes here....

    private void showDialog() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(R.string.logoutMessage));
        builder.setNegativeButton(context.getResources().getString(R.string.cancelSpelling), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                //Initially whenever this Activity starts for the 1st time always Dashboard shud open....
                //						selectItem(0);
            }//onClick closes here....
        });//builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() closes here....

        builder.setPositiveButton(context.getResources().getString(R.string.ok_spelling), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppUtils.clearPreferences(context);
                context.startActivity(new Intent(context, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                AppUtils.givefinishEffect(context);
            }//onClick closes here....
        });//builder.setPositiveButton("Logout", new DialogInterface.OnClickListener()  closes here....

        builder.create();
        builder.show();

    }//showDialog closes here....

}//Logout_Dialog closes here....
