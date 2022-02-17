package com.icicisecurities.hub.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.icicisecurities.hub.HomeActivity;
import com.icicisecurities.hub.R;
import com.icicisecurities.hub.connection.NetworkResponseListener;
import com.icicisecurities.hub.connection.Params_POJO;
import com.icicisecurities.hub.connection.PostResponseAsync;
import com.icicisecurities.hub.utils.AESUtils;
import com.icicisecurities.hub.utils.AppConstants;
import com.icicisecurities.hub.utils.AppUtils;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.icicisecurities.hub.utils.AppUtils.showSnackBar;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, NetworkResponseListener {

    private String TAG = getClass().getSimpleName();

    @BindView(R.id.ntIdLO)
    TextInputLayout _userIDLO;
    @BindView(R.id.passwordLO)
    TextInputLayout _passwordLO;
    @BindView(R.id.ntId)
    EditText _userIDEditText;
    @BindView(R.id.input_password)
    TextInputEditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.appVersionTV)
    TextView _appVersionTV;
    private LogInResponseHandler logInResponseHandler;
    private String token;
    private  String inwardCenter;

    private enum LogInResponseHandler{
        LOG_IN , NOTIFICATION
    }

    private static HashMap<String, Boolean> mSupportedDevicelist;
    static {
        mSupportedDevicelist = new HashMap<String, Boolean>();
        mSupportedDevicelist.put("SM-T116IR", true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        String versionName = "Release version " + AppUtils.getVersionName(LoginActivity.this,LoginActivity.this);
        _appVersionTV.setText(versionName);

        requestPermission();
        _loginButton.setOnClickListener(this);
        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isComplete()){
                    return;
                }
                // Get new Instance ID token

                try {
                    token = task.getResult().getToken();
//                    Log.d(TAG , "token : " + token);

                }catch (Exception e){
                    Log.d(TAG , e.toString());
                }

                // Log and toast
//                    String msg = getString(R.string.msg_token_fmt, token);
//                    Log.d(TAG, msg);
//                    Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();

            }
        });

//        int count = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).getAll().size();
        AppUtils.clearPreferences(LoginActivity.this);
//        Log.d(TAG, "pref count : " + count);

    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Since it's Marshallow, chk the permissions now....

            // Assume thisActivity is the current activity
            int cameraPermissionChk = ContextCompat.checkSelfPermission(LoginActivity.this,
                    Manifest.permission.READ_PHONE_STATE);

            // Here, thisActivity is the current activity
            if (cameraPermissionChk != PackageManager.PERMISSION_GRANTED) {//Not condition....

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.READ_PHONE_STATE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.need_permission), Toast.LENGTH_LONG).show();

                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.INTERNET},
                            1);//Internet, ReadPhoneState, Write Settings

                }//if (ActivityCompat.shouldShowRequestPermissionRationale(AccountOpeningActivity.this, Manifest.permission.CAMERA)) closes here....
                else {

                    // No explanation needed, we can request the permission.


                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.INTERNET},
                            1);

                    // CAMERA_AND_WRITE_EXTERNAL_PERMISSIONS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }//else closes here.....
            }//if (cameraPermissionChk != PackageManager.PERMISSION_GRANTED) closes here.....

        }//if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) closes here.....


        //////////..............ENHANCEMENT DATED : 15-07-2016.......ADDING PERMISSION AT APP START CLOSES ABOVE..........\\\\\\\\\\\\\\\\\

    }//requestAllPermissions closes here.....


    @Override
    public void onBackPressed() {
        // Disable going back to the HomeActivity
        moveTaskToBack(true);
    }

    public boolean validate() {
        boolean valid = true;

        String ntId = _userIDEditText.getText().toString();
        String password = _passwordText.getText().toString();

        if (ntId.isEmpty()) {
//            _userIDEditText.setError("enter ntId");
            _userIDLO.setError("Please enter NT ID");
            valid = false;
        } else {
            _userIDLO.setError(null);
        }

        if (password.isEmpty()) {
//            _passwordText.setError("enter password");
            _passwordLO.setError("Please enter password");
            valid = false;
        } else {
            _passwordLO.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            //When user clicks on Login Btn the control comes below....
            case R.id.btn_login:

                Boolean flag = validate();

                if (flag) {


//                    Intent intent = new Intent(this, MainActivityV2.class);
//                    intent.putExtra(AppConstants.LAST_LOGIN,lstLogin);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);

                    Params_POJO pojo = new Params_POJO();
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String IMEI;
                    try {
//                        IMEI = telephonyManager.getDeviceId();
//                        IMEI = "868539044496436";

                    String deviceUsed = "";
                    String devicename = android.os.Build.MODEL;
                    if (mSupportedDevicelist.get(devicename) == null) {
                        deviceUsed = "mobile";
                    } else {
                        deviceUsed = "tab";
                    }

                    String url = AppConstants.URL + AppConstants.LOGIN_URL;
                    logInResponseHandler = LogInResponseHandler.LOG_IN;
                    pojo.setUrl(url);

                    String data = "NTId=" + _userIDEditText.getText().toString().trim()
                            + "&NTPass=" + _passwordText.getText().toString().trim()
                            + "&IMEI=" + "NA";//IMEI + "";
                    //Update : 04-06-2016.....Since this code/build is gng LIVE, so we will send the version to the Login API. such that in future they shud recognize the activities.....
                    //& ofcourse the mrkting ppl will for sure do mistake in installing the build, so we have to recognize easily that which build is used by the mrkting ppl to report the bug.
                    data += "&version=" + AppUtils.getVersionName(LoginActivity.this, LoginActivity.this) + "&flag=L"
                            //sadbhfjhfbcf add commet for this Date
                            + "&device=" + deviceUsed
                            + "&token=" + token
                            + "&app=" + "H"                         // to detect the application whether Hub or Acc Openining
                    ;


                    String encrypted = "";
                        encrypted = AESUtils.encrypt(data);
                        pojo.setData(encrypted);
                        Log.d("TEST", "unencrypted :" + data);

                        Log.d("TEST", "encrypted:" + encrypted);

                    PostResponseAsync postAsync = new PostResponseAsync(LoginActivity.this, LoginActivity.this);
                    postAsync.setResponseListener(LoginActivity.this);
                    postAsync.execute(pojo);
                    }catch (Exception e){
                        AppUtils.showSnackBar(LoginActivity.this,findViewById(R.id.scroll) , getResources().getString(R.string.not_able_to_register_device) , Snackbar.LENGTH_SHORT);
                    }
                }//if(flag) closes here.....
                else
                    Log.w(TAG, "Validation error !");
                break;

            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! do the
                // calendar task you need to do.

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.

                Toast.makeText(LoginActivity.this, "Permission was not granted", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void notifyNetworkResponseSuccess(String response) {

        if(response.startsWith(AppConstants.SUCCESS_STRING_PREFIX)) {

            switch (logInResponseHandler){

                case LOG_IN:

                    String responce = response.split(Pattern.quote(AppConstants.END_OF_URL))[1];

                    String sessionID = responce.split(Pattern.quote(AppConstants.RESPONSE_SPLITTER_STRING))[1].replace("|^", "");
                    String lstLogin = responce.split(Pattern.quote(AppConstants.RESPONSE_SPLITTER_STRING))[2];
                    inwardCenter = responce.split(Pattern.quote(AppConstants.RESPONSE_SPLITTER_STRING))[3];
                    final String notificationCount = responce.split(Pattern.quote(AppConstants.RESPONSE_SPLITTER_STRING))[6];
                    String globalUserId = responce.split(Pattern.quote(AppConstants.RESPONSE_SPLITTER_STRING))[7];
                    String cseIdFlag = responce.split(Pattern.quote(AppConstants.RESPONSE_SPLITTER_STRING))[8];

                    if (!sessionID.equals(""))
                        PreferenceManager.getDefaultSharedPreferences(this)
                                .edit()
                                .putString(AppConstants.SESSION_ID, sessionID)
                                .apply();

                    if (!inwardCenter.equals(""))
                        PreferenceManager.getDefaultSharedPreferences(this)
                                .edit()
                                .putString(AppConstants.INWARD_CENTER, inwardCenter)
                                .apply();
                    else
                        PreferenceManager.getDefaultSharedPreferences(this)
                                .edit()
                                .putString(AppConstants.INWARD_CENTER, "*")
                                .apply();

                    PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit()
                            .putString(AppConstants.LOGGED_IN_USER_ID, globalUserId.trim()).apply();


                    /**CLEARING THE OVERFLOW SHAREDPREFS,
                     *
                     * WHAT IS OVERFLOW SHAREDPREFS:
                     * ==============================
                     *OVERFLOW SHAREDPREFS contains flag for every fragment accessed.
                     * eg. if the FormNumberFragment is displayed then OVERFLOW SHAREDPREFS will contain flag for the same
                     * which we will use to diplay overflow menu. (FormNumber item in overflow menu will be enabled)
                     *
                     * NOTE: WE ARE CLEARING THE OVERLOW SHAREDPREFS BCOZ AFTER LOGINF ALL OPTIONS SHUD BE FREEZED AGAIN.
                     * **/
//                    AppUtils.getOverFlowSharedPrefeerence(this).edit().clear().apply();

                    // Pop up for the Notifications permissions...
                    if (responce.split(Pattern.quote(AppConstants.RESPONSE_SPLITTER_STRING))[5].equalsIgnoreCase("Y")) {
                        AlertDialog.Builder alerBuilder = new AlertDialog.Builder(LoginActivity.this);
                        alerBuilder.setTitle(getResources().getString(R.string.app_name))
                                .setMessage(getResources().getString(R.string.notification_dialog_confirmation_spelling))
                                .setPositiveButton(R.string.yesSpelling, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Params_POJO pojo = new Params_POJO();
                                        String notificationUrl = AppConstants.URL + AppConstants.BANK_FETCH_CALL_FOR_OTP;
                                        pojo.setUrl(notificationUrl);

//                                        NOTFCN_HUB|^LOGGED_IN_USER_ID|^token|^SESSION_ID|^notificationCount;

                                        logInResponseHandler = LogInResponseHandler.NOTIFICATION;
                                        String data = AppConstants.NOTIFICATION + AppConstants.RESPONSE_SPLITTER_STRING
                                                + PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).getString(AppConstants.LOGGED_IN_USER_ID, "") + AppConstants.RESPONSE_SPLITTER_STRING
                                                + token + AppConstants.RESPONSE_SPLITTER_STRING
                                                + PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).getString(AppConstants.SESSION_ID, "") +AppConstants.RESPONSE_SPLITTER_STRING
                                                + notificationCount;
                                        pojo.setData(data);
                                        PostResponseAsync postAsync = new PostResponseAsync(LoginActivity.this, LoginActivity.this);
                                        postAsync.setResponseListener(LoginActivity.this);
                                        postAsync.execute(pojo);

                                    }
                                }).setNegativeButton(R.string.noSpelling, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Intent intent = new Intent(getApplicationContext(), MainActivityV2.class);
//                                intent.putExtra(AppConstants.LAST_LOGIN, lstLogin);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
//                                AppUtils.giveIntentEffect(LoginActivity.this);
//
//                                File parentFile = AppUtils.getRootDirectory(getApplicationContext(), LoginActivity.this);
//                                if (!parentFile.exists())
//                                    parentFile.mkdirs();//This will make the Parent Folder if not exists...
//                                finish();//Bcoz once Login is successful, Login screens shud not come again......

                                // Pop up for the Notifications permissions...
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                AppUtils.giveIntentEffect(LoginActivity.this);
                                finish();
                            }
                        }).show();
                    } else {
//                        Intent intent = new Intent(getApplicationContext(), MainActivityV2.class);
//                        intent.putExtra(AppConstants.LAST_LOGIN,lstLogin);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        AppUtils.giveIntentEffect(LoginActivity.this);
//
//                        File parentFile = AppUtils.getRootDirectory(getApplicationContext(), LoginActivity.this);
//                        if(!parentFile.exists())
//                            parentFile.mkdirs();//This will make the Parent Folder if not exists...
//                        finish();//Bcoz once Login is successful, Login screens shud not come again......

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        AppUtils.giveIntentEffect(LoginActivity.this);
                        finish();
                    }

                    break;

                case NOTIFICATION:

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                    intent.putExtra(AppConstants.LAST_LOGIN,lstLogin);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    AppUtils.giveIntentEffect(LoginActivity.this);

//                    File parentFile = AppUtils.getRootDirectory(getApplicationContext(), LoginActivity.this);
//                    if(!parentFile.exists())
//                        parentFile.mkdirs();//This will make the Parent Folder if not exists...
                    finish();//Bcoz once Login is successful, Login screens shud not come again......

                    break;

                default:

                    break;
            } // switch() closes here....

        }else if(response.startsWith(AppConstants.ERROR_STRING_PREFIX)){
            //The response is Failure....
            String errorMsg = "";

            //Splitting the String w.r.t the Response Divider String....
            String[] splittedMsg = response.split(Pattern.quote(AppConstants.END_OF_URL));
//			String[] splittedMsg = response.split(AppConstants.RESPONSE_SPLITTER_STRING);
            if(splittedMsg != null){
                errorMsg = splittedMsg[1];

                if(errorMsg.contains(AppConstants.RESPONSE_SPLITTER_STRING))
                    errorMsg = errorMsg.replace(AppConstants.RESPONSE_SPLITTER_STRING, "");

                AppUtils.showAlertDialog(LoginActivity.this,LoginActivity.this, errorMsg);
            }//if(splittedMsg != null) closes here....
            else
                Log.w(TAG, "splittedMsg is null...");

        }//response is Failure closes here....
        else if(response.startsWith(AppConstants.FAILURE_STRING_PREFIX)){
            //The response is Failure....
            String errorMsg = "";

            //Splitting the String w.r.t the Response Divider String....
            String[] splittedMsg = response.split(Pattern.quote(AppConstants.RESPONSE_SPLITTER_STRING));
//			String[] splittedMsg = response.split(AppConstants.RESPONSE_SPLITTER_STRING);
            if(splittedMsg != null){
                errorMsg = splittedMsg[1];
                if(errorMsg.contains(AppConstants.RESPONSE_SPLITTER_STRING))
                    errorMsg = errorMsg.replace(AppConstants.RESPONSE_SPLITTER_STRING, "");

                AppUtils.showAlertDialog(LoginActivity.this, LoginActivity.this, errorMsg);
            }//if(splittedMsg != null) closes here....
            else
                Log.w(TAG, "splittedMsg is null...");

        }//response is Failure closes here....
        else
            Log.w(TAG, "Unhandled case : "+response);

    }

    @Override
    public void notifyNetworkResponseFailure(Exception exception, String response) {

        showSnackBar(this,findViewById(R.id.scroll) , getResources().getString(R.string.networkErrorSpelling) , Snackbar.LENGTH_SHORT);
//        AppUtils.showAlertDialog(LoginActivity.this, LoginActivity.this, exception.getMessage());


    }
}
