package com.icicisecurities.hub.ui.profile;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.icicisecurities.hub.R;
import com.icicisecurities.hub.connection.NetworkResponseListener;
import com.icicisecurities.hub.connection.Params_POJO;
import com.icicisecurities.hub.connection.PostResponseAsync;
import com.icicisecurities.hub.ui.profile.model.GetSetInwardListListner;
import com.icicisecurities.hub.ui.profile.model.InwardCenterVO;
import com.icicisecurities.hub.utils.AppConstants;

import java.util.ArrayList;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;
import static com.icicisecurities.hub.utils.AppUtils.showSnackBar;

public class InWardCenterFragment extends Fragment implements GetSetInwardListListner, View.OnClickListener, NetworkResponseListener {

    @BindView(R.id.inwardCenterTV)
    TextView inwardCenterTV;
    @BindView(R.id.inwardCenterDD)
    AppCompatSpinner inwardCenterDD;
    @BindView(R.id.inwardCenterSubmit)
    Button inwardCenterSubmitBtn;
    private View root;
    private String inwardCenterCode = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_inward_center, container, false);
        ButterKnife.bind(this,root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initialization();
    }

    private void initialization() {
        inwardCenterDD = root.findViewById(R.id.inwardCenterDD);
        inwardCenterSubmitBtn = root.findViewById(R.id.inwardCenterSubmit);

        //if inward centers value from login is not * then we will have to make default selection to
        //logged in users inward center. Code for the  same goes below.

        InwardCenterTask inwardCenterTask = new InwardCenterTask(getActivity(), InWardCenterFragment.this);
        inwardCenterTask.setDetailsListener(InWardCenterFragment.this);
        inwardCenterTask.getDetails();

        generateEvents();
    }

    private void generateEvents() {
        inwardCenterSubmitBtn.setOnClickListener(this);
    }


    @Override
    public void getInwardCentersList(final ArrayList<InwardCenterVO> inwardCenterListAl) {

        InwardCenterListAdapter inwardCenterListAdapter = new InwardCenterListAdapter(getActivity(),
                R.layout.spinner_row_layout, inwardCenterListAl);
        inwardCenterDD.setAdapter(inwardCenterListAdapter);


        if (!PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.INWARD_CENTER, "").equals("*")) {

            String inwardCenter = PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .getString(AppConstants.INWARD_CENTER, "");

            for (int i = 0; i < inwardCenterListAl.size(); i++) {
                if (inwardCenterListAl.get(i).getInwardCode().equalsIgnoreCase(inwardCenter))
                    inwardCenterDD.setSelection(i);
            }
        }

        inwardCenterDD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inwardCenterCode = inwardCenterListAl.get(i).getInwardCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                inwardCenterCode = inwardCenterListAl.get(0).getInwardCode();
                Log.d("@@@@", "______-------> " + adapterView.getSelectedItem().toString());
            }
        });

    }//getInwardCentersList closes here.....

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.inwardCenterSubmit:

                if (!inwardCenterCode.equals("")) {
                    try {
                        String submitInwardCenterUrl = AppConstants.URL + AppConstants.WEBKIT_SUB_URL;
                        String callType = AppConstants.INWARD_CENTER_FLAG;
//                    String formNumber = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.FORM_NUMBER, null);
                        String rmId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.LOGGED_IN_USER_ID, null);
                        String sessionID = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.SESSION_ID, null);


                        String data = callType + AppConstants.RESPONSE_SPLITTER_STRING
                                + "" + AppConstants.RESPONSE_SPLITTER_STRING
                                + rmId + AppConstants.RESPONSE_SPLITTER_STRING
                                + sessionID + AppConstants.RESPONSE_SPLITTER_STRING
                                + inwardCenterCode + AppConstants.RESPONSE_SPLITTER_STRING
                                + AppConstants.END_OF_URL;

                        Params_POJO pojo = new Params_POJO();
                        pojo.setData(data);
                        pojo.setUrl(submitInwardCenterUrl);

                        PostResponseAsync submitCustomerDetailsAsync = new PostResponseAsync(getActivity(), InWardCenterFragment.this);
                        submitCustomerDetailsAsync.setResponseListener(InWardCenterFragment.this);
                        submitCustomerDetailsAsync.execute(pojo);
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                        Log.e(TAG, "Exception while parsing DOB : " + e);
                    }//catch closes here.....
                }else{
//                    Toast.makeText(getActivity(), "Invalid inward center", Toast.LENGTH_SHORT).show();
                    showSnackBar(getActivity(),getActivity().findViewById(R.id.drawer_layout) , "Invalid inward center" , Snackbar.LENGTH_SHORT);

                }
                break;

            default:
                break;
        }//switch (view.getId()) closes here....
    }

    @Override
    public void notifyNetworkResponseSuccess(String response) {
        if (response != null) {
            if (response.startsWith(AppConstants.SUCCESS_STRING_PREFIX)) {
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                        .putString(AppConstants.INWARD_CENTER, inwardCenterCode)
                        .apply();

                Navigation.findNavController(root).navigate(R.id.action_nav_profile_to_nav_new);

            }//if (response.startsWith(AppConstants.SUCCESS_STRING_PREFIX)) closes here.....
            else if (response.startsWith(AppConstants.ERROR_STRING_PREFIX)) {
//                Toast.makeText(getActivity(),
//                        response.split(Pattern.quote(AppConstants.END_OF_URL))[1].replace("|^", ""),
//                        Toast.LENGTH_SHORT)
//                        .show();
                showSnackBar(getActivity(),getActivity().findViewById(R.id.drawer_layout) , response.split(Pattern.quote(AppConstants.END_OF_URL))[1].replace("|^", "") , Snackbar.LENGTH_SHORT);

            }//else if(response.startsWith(AppConstants.ERROR_STRING_PREFIX)) closes here....
        }//if(response != null) closes here....
    }

    @Override
    public void notifyNetworkResponseFailure(Exception exception, String response) {
        showSnackBar(getActivity(),getActivity().findViewById(R.id.drawer_layout) , "Network Error" , Snackbar.LENGTH_SHORT);

    }
}
