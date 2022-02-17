package com.icicisecurities.hub.ui.logout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.icicisecurities.hub.HomeActivity;
import com.icicisecurities.hub.R;
import com.icicisecurities.hub.connection.NetworkResponseListener;
import com.icicisecurities.hub.connection.Params_POJO;
import com.icicisecurities.hub.connection.PostResponseAsync;
import com.icicisecurities.hub.custom.BottomSheetDialogFragments;
import com.icicisecurities.hub.login.LoginActivity;
import com.icicisecurities.hub.ui.summary.SummaryViewModel;
import com.icicisecurities.hub.utils.AppConstants;
import com.icicisecurities.hub.utils.AppUtils;

import java.util.regex.Pattern;

import butterknife.ButterKnife;

public class LogoutFragment extends Fragment implements NetworkResponseListener {

    private String TAG = LogoutFragment.this.getClass().getSimpleName();
    private LogoutViewModel logoutViewModel;
    View root;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logoutViewModel =
                ViewModelProviders.of(this).get(LogoutViewModel.class);
        root = inflater.inflate(R.layout.fragment_logout, container, false);
        ButterKnife.bind(this,root);
//        final TextView textView = root.findViewById(R.id.text_gallery);
        logoutViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Params_POJO pojo = new Params_POJO();
//        TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);

//        String IMEI = telephonyManager.getDeviceId();

        String url = AppConstants.URL+AppConstants.LOGIN_URL;
        pojo.setUrl(url);

        //LOGGED_IN_USER_ID is same for both Userid and Password during Logout sending...

        String UserId= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.LOGGED_IN_USER_ID, "");


//        String data = "NTId="+UserId+"&NTPass="+UserId+"&IMEI="+IMEI+"";
        String data = "NTId="+UserId+"&NTPass="+UserId+"&IMEI="+"NA";

        //Update : 04-06-2016.....Since this code/build is gng LIVE, so we will send the version to the Login API. such that in future they shud recognize the activities.....
        //& ofcourse the mrkting ppl will for sure do mistake in installing the build, so we have to recognize easily that which build is used by the mrkting ppl to report the bug.
        data += "&version="+ AppUtils.getVersionName(getActivity(),this)+"&flag=O";
        pojo.setData(data);


        PostResponseAsync postAsync = new PostResponseAsync(getActivity(),this);
        postAsync.setResponseListener(LogoutFragment.this);
//        postAsync.execute(pojo);

        //Added by vikas on 16 Dec 16 as per logout requirement end.......

        //Clear the SharedPreferences here....
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().clear().apply();//Bcoz after Logout no use of any data....
        BottomSheetDialogFragments.setSearch("");
        AppUtils.clearPreferences(getActivity());

        startActivity(new Intent(getActivity(), LoginActivity.class));
        (getActivity()).finish();
    }

    @Override
    public void notifyNetworkResponseSuccess(String response) {

        if (response.startsWith(AppConstants.SUCCESS_STRING_PREFIX)) {

        }else if (response.startsWith(AppConstants.ERROR_STRING_PREFIX)){

            String errorMsg = "";

            //Splitting the String w.r.t the Response Divider String....
            String[] splittedMsg = response.split(Pattern.quote(AppConstants.END_OF_URL));
//			String[] splittedMsg = response.split(AppConstants.RESPONSE_SPLITTER_STRING);
            if(splittedMsg != null){
                errorMsg = splittedMsg[1];

                if(errorMsg.contains(AppConstants.RESPONSE_SPLITTER_STRING))
                    errorMsg = errorMsg.replace(AppConstants.RESPONSE_SPLITTER_STRING, "");

                AppUtils.showAlertDialog(getActivity(),LogoutFragment.this,errorMsg);
            }//if(splittedMsg != null) closes here....
            else
                Log.w(TAG, "splittedMsg is null...");


        }

    }

    @Override
    public void notifyNetworkResponseFailure(Exception exception, String response) {
        Log.w(TAG, "notifyNetworkResponseFailure : " + response);

    }
}
