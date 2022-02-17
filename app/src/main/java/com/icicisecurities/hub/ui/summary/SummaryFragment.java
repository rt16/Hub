package com.icicisecurities.hub.ui.summary;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.icicisecurities.hub.HomeActivity;
import com.icicisecurities.hub.R;
import com.icicisecurities.hub.connection.NetworkResponseListener;
import com.icicisecurities.hub.connection.Params_POJO;
import com.icicisecurities.hub.connection.PostResponseAsync;
import com.icicisecurities.hub.ui.new_form.RelationAdapter;
import com.icicisecurities.hub.ui.new_form.model.RelationVO;
import com.icicisecurities.hub.ui.profile.InwardCenterListAdapter;
import com.icicisecurities.hub.ui.summary.model.SummaryVO;
import com.icicisecurities.hub.utils.AppConstants;
import com.icicisecurities.hub.utils.AppUtils;
import com.icicisecurities.hub.custom.BottomSheetDialogFragments;
import com.icicisecurities.hub.utils.FilterLeadsListener;
import com.icicisecurities.hub.utils.SessionLogoutDialog;

import java.util.ArrayList;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SummaryFragment extends Fragment implements FilterLeadsListener, NetworkResponseListener, GetFilterListener {

    private SummaryViewModel galleryViewModel;
    @BindView(R.id.leftHeaderTxtView)
    TextView leftHeader;
    @BindView(R.id.centerHeaderTxtView)
    TextView centerHeader;
    @BindView(R.id.rightHeaderTxtView)
    TextView rightHeader;
    @BindView(R.id.summaryListView)
    ListView summaryListView;
    private AppCompatSpinner spinner;
    private SpinnerAdapter leadsFilterSpinnerAdapter;
    private ActionBar actionBar;
    private ActionBar.OnNavigationListener mOnNavigationListener;
    private Integer screenWidth;
    private String categorySelected="%";
    private String TAG = getClass().getSimpleName();
    private ArrayList<SummaryVO> leadsAl;
    private ArrayList<RelationVO> filterDropDownAL;
    SummaryAdapter adapter;
    private String DATE_PICKER_FRAGMENT_TAG = "DATE_PICKER_FRAGMENT_TAG";

    private enum ResponseFor{ SUMMARY, DROP_DOWN}
    private ResponseFor responseFor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(SummaryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_summary, container, false);
        ButterKnife.bind(this,root);
//        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
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
        //Giving Names to the List Header....
        leftHeader.setText(getResources().getString(R.string.formNumberSpelling));
        centerHeader.setText(getResources().getString(R.string.PANNumberSpelling));
        centerHeader.setVisibility(View.GONE);
        rightHeader.setText(getResources().getString(R.string.statusSpelling));

        setHasOptionsMenu(true);

        //Setting Header UI.....
        screenWidth = AppUtils.getScreenWidth(getActivity());
        Log.d(TAG,"Screen width " + screenWidth);
        leftHeader.setWidth(screenWidth / 2);
//        centerHeader.setWidth(screenWidth / 3);
        rightHeader.setWidth(screenWidth / 2);

        callDropDownApi();

        FilterTask fatcaCountryTask1 = new FilterTask(getActivity(), SummaryFragment.this);
        fatcaCountryTask1.setDetailsListener(SummaryFragment.this);
        fatcaCountryTask1.getDetails();
    }

    private void callSummaryApi() {
        String url_OpenClosedCases = AppConstants.URL + AppConstants.HUB_SUMMARY_URL;
        Params_POJO params = new Params_POJO();
        params.setUrl(url_OpenClosedCases);
//        PANSUMMARY|^Emp ID|^Session ID|^Start Date|^End Date|^|$

        // formnumber and status
//        % % or code
        responseFor = ResponseFor.SUMMARY;
        String data = AppConstants.PAN_SUMMARY_CALL_TYPE + AppConstants.RESPONSE_SPLITTER_STRING
                        + PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.LOGGED_IN_USER_ID, null)
                        + AppConstants.RESPONSE_SPLITTER_STRING + PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.SESSION_ID,null)
                        + AppConstants.RESPONSE_SPLITTER_STRING + BottomSheetDialogFragments.getLeadsFilter_FromDate().trim()
                        + AppConstants.RESPONSE_SPLITTER_STRING + BottomSheetDialogFragments.getLeadsFilter_ToDate().trim()
                        + AppConstants.RESPONSE_SPLITTER_STRING + BottomSheetDialogFragments.getLeadsFilter_SearchForm().trim()
                        + AppConstants.RESPONSE_SPLITTER_STRING + categorySelected
                        + AppConstants.RESPONSE_SPLITTER_STRING + AppConstants.END_OF_URL;
        params.setData(data);

        PostResponseAsync async = new PostResponseAsync(getActivity(), SummaryFragment.this);
        async.setResponseListener(SummaryFragment.this);
        async.execute(params);
    }

    private void callDropDownApi(){
        String url_OpenClosedCases = AppConstants.URL + AppConstants.HUB_SUMMARY_URL;
        Params_POJO params = new Params_POJO();
        params.setUrl(url_OpenClosedCases);

//BINDSTATUSDROPDOWN|^ RM Code |^ Session ID |^$

        responseFor = ResponseFor.DROP_DOWN;
        String data = AppConstants.SUMMARY_STATUS_DROP_DOWN + AppConstants.RESPONSE_SPLITTER_STRING
                + PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.LOGGED_IN_USER_ID, null)
                + AppConstants.RESPONSE_SPLITTER_STRING + PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.SESSION_ID,null)
                + AppConstants.RESPONSE_SPLITTER_STRING + AppConstants.END_OF_URL;
        params.setData(data);

        PostResponseAsync async = new PostResponseAsync(getActivity(), SummaryFragment.this);
        async.setResponseListener(SummaryFragment.this);
        async.execute(params);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        menu.clear();
        inflater.inflate(R.menu.leads_filter_menu, menu);

        MenuItem spinnerItem = menu.findItem(R.id.spinner);
        spinner = (AppCompatSpinner) spinnerItem.getActionView();

//        MenuItem item = menu.findItem(R.id.search);
//        SearchView searchView = new SearchView(((HomeActivity) getActivity()).getSupportActionBar().getThemedContext());
//        MenuItemCompat.setActionView(item, searchView);
//        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
//        searchView.setQueryHint("Search Form Number");
//        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }//onCreateOptionsMenu closes here....

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.action_filter:
                BottomSheetDialogFragments filteDialogDisplay = new BottomSheetDialogFragments(getActivity(), SummaryFragment.this);
                filteDialogDisplay.setFilterListener(SummaryFragment.this);
                filteDialogDisplay.show(getFragmentManager().beginTransaction(),"BOTTOMSHEETDIALOGFRAG");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }//switch (item.getItemId()) closes here....

    }//onOptionsItemSelected closes here....

    @Override
    public void filterLeadsListener(Boolean startFilter) {
        if (leadsAl != null) {
            leadsAl.clear();
        }
        callSummaryApi();
    }

//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        Log.d(TAG, "query submitted " + query);
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        if (leadsAl != null) {
//            adapter.filter(newText);
//        }
//        return true;
//    }

    @Override
    public void notifyNetworkResponseSuccess(String response) {
//        0|$874552155|^02-Jan-2020|^|^C711095|^MUMBAI|^RECD. FROM SALES|^|$
        Log.d(TAG, "notifyNetworkResponseSuccess :: " + response);

//        customAdapter = new CustomAdapter(mDataset);
//        // Set CustomAdapter as the adapter for RecyclerView.
//        summaryListView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)

        if(response != null){

        if (response.startsWith(AppConstants.SUCCESS_STRING_PREFIX)) {
            /**BUG :
             * WHEN THERE ARE NO RECORDS IN THE RESPONSE THEN THE RESPONSE IS VERY DIFFERENT.
             * SO, WE ARE SIMPLY CHKING FOR THE STRING IN THE RESPONSE & DISPLAYING RESPECTIVE ERROR MESSAGE...**/

//            0|$No Record Found|^|$ acc opening
//            0|$NO RECORD FOUND|^|$ now
            if (response.contains(getResources().getString(R.string.noRecordFoundErrorMsg))) {
                ///No records found in the Records....
                AppUtils.showAlertDialog(getActivity(), SummaryFragment.this, getResources().getString(R.string.noRecordFoundErrorMsg));
            } else {

                switch (responseFor){
                    case SUMMARY:
                        //Here response is Success...
//                        0|$3333333335|^25-Feb-2020|^W|^C711095|^W|^INWARDED|^NA|^N|^|$+
                        leadsAl = new ArrayList<>();
                        String[] allLeadCasesString = response.split(Pattern.quote(AppConstants.END_OF_URL));//We split the String with |$ then we get all the Lead cases....

                        for (int i = 1; i < allLeadCasesString.length; i++) {//Starting the loop from 1 bcoz at 0th position there is a Success/Failure String
                            Log.d(TAG, "Check Values : " + " " + i + " " + allLeadCasesString[i]);//You can chk the All Lead Cases here....

                            //We have to fetch the details of Leads....So,
                            String[] leadDetails = allLeadCasesString[i].split(Pattern.quote(AppConstants.RESPONSE_SPLITTER_STRING));

                            SummaryVO summaryVO = new SummaryVO();

                            String formNumber = leadDetails[0];//lead Details has FORM number in 1st place....
                            summaryVO.setFormNumber(formNumber);

                            String status = leadDetails[5];
                            summaryVO.setStatus(status);


                            String rejectedReason = leadDetails[7];//If the form is rejected then reason comes here..else this space comes empty....
                            summaryVO.setRejectedReason(rejectedReason);

                            String rejectionFlag = leadDetails[8];
                            summaryVO.setRejectedFlag(rejectionFlag);

                            String submittedDate = leadDetails[1]; // added on 12 may 2020 as discussed with rahul submitted and updated date
                            summaryVO.setSubmittedDate(submittedDate);

                            String updatedDate = leadDetails[6];
                            summaryVO.setUpdatedDate(updatedDate);
                            leadsAl.add(summaryVO);
                        }

                        break;

                    case DROP_DOWN:
//                      0|$R2~RECD. FROM SALES|$W~INWARDED|$ S5~SENT FOR SCANNING|$ R11~RECD. FOR SCANNING|$ L~LOCAL REJECTION|$ R6~REJECTED FORM SENT TO SALES|$.....
                        filterDropDownAL = new ArrayList<>();
                        String[] dropdownDetails = response.split(Pattern.quote(AppConstants.END_OF_URL));
                        RelationVO relationVO1 = new RelationVO();
                        relationVO1.setEntityCode("%");
                        relationVO1.setEntityDescription("ALL");
                        filterDropDownAL.add(relationVO1);
                        for (int i=1; i< dropdownDetails.length; i++) {
                            String[] filterDropDown = dropdownDetails[i].split(Pattern.quote(AppConstants.TILDE_SYMBOL));
                            RelationVO relationVO = new RelationVO();

                            String code = filterDropDown[0];
                            String dropDownValue = filterDropDown[1];
                            relationVO.setEntityCode(code);
                            relationVO.setEntityDescription(dropDownValue);
                            filterDropDownAL.add(relationVO);

                            spinner.getLayoutParams().width = AppUtils.getScreenWidth(getActivity())/2;


                            mOnNavigationListener = new ActionBar.OnNavigationListener() {

                                @Override
                                public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                                    // TODO Auto-generated method stub
                                    if (leadsAl != null)
                                        leadsAl.clear();

                                    String url_OpenClosedCases = AppConstants.URL + AppConstants.HUB_SUMMARY_URL;
                                    Params_POJO params = new Params_POJO();
                                    params.setUrl(url_OpenClosedCases);
//        PANSUMMARY|^Emp ID|^Session ID|^Start Date|^End Date|^|$

                                    // formnumber and status
//        % % or code
                                    responseFor = ResponseFor.SUMMARY;
                                    String data = AppConstants.PAN_SUMMARY_CALL_TYPE + AppConstants.RESPONSE_SPLITTER_STRING
                                            + PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.LOGGED_IN_USER_ID, null)
                                            + AppConstants.RESPONSE_SPLITTER_STRING + PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.SESSION_ID,null)
                                            + AppConstants.RESPONSE_SPLITTER_STRING + BottomSheetDialogFragments.getLeadsFilter_FromDate().trim()
                                            + AppConstants.RESPONSE_SPLITTER_STRING + BottomSheetDialogFragments.getLeadsFilter_ToDate().trim()
                                            + AppConstants.RESPONSE_SPLITTER_STRING + BottomSheetDialogFragments.getLeadsFilter_SearchForm().trim()
                                            + AppConstants.RESPONSE_SPLITTER_STRING + filterDropDownAL.get(itemPosition).getEntityCode()
                                            + AppConstants.RESPONSE_SPLITTER_STRING + AppConstants.END_OF_URL;
                                    params.setData(data);

                                    PostResponseAsync async = new PostResponseAsync(getActivity(), SummaryFragment.this);
                                    async.setResponseListener(SummaryFragment.this);
                                    async.execute(params);
                                    return true;
                                }
                            };
                        }

                        break;
                }


            }//Here response is Success closes here....

            //Setting the Adapter now....
            //NOTE: Setting the Adapter here bcoz If there are no results in the Response then empty adapter will be setted.
            //& if response has results in it then List will be setted.
            if (leadsAl != null) {
                //Setting the Adapter now....
                adapter = new SummaryAdapter(getActivity(), R.layout.single_row_leads, leadsAl);
                summaryListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }//if(leadsAl != null) closes here.....
            else
                Log.w(TAG, "Open cases ArrayList is null !");

        }//if(response.startsWith(AppConstants.SUCCESS_STRING_PREFIX)) closes here....
        else if (response.startsWith(AppConstants.FAILURE_STRING_PREFIX)) {
            //The response is Failure....
            String errorMsg = "";

            //Splitting the String w.r.t the Response Divider String....
            String[] splittedMsg = response.split(Pattern.quote(AppConstants.RESPONSE_SPLITTER_STRING));
            //					String[] splittedMsg = response.split(AppConstants.RESPONSE_SPLITTER_STRING);
            if (splittedMsg != null) {
                errorMsg = splittedMsg[1];
                AppUtils.showAlertDialog(getActivity(), SummaryFragment.this, errorMsg);
            }//if(splittedMsg != null) closes here....
            else
                Log.w(TAG, "splittedMsg is null...");

        }//response is Failure closes here....
        else if (response.startsWith(AppConstants.ERROR_STRING_PREFIX)) {

            //Else if response starts with -1 then below logic is executed....

            String errorMsg = "";

            //Splitting the String w.r.t the |$....
            String[] splittedMsg = response.split(Pattern.quote(AppConstants.END_OF_URL));
            if (splittedMsg != null) {
                errorMsg = splittedMsg[1];
                if (errorMsg.contains(AppConstants.SESSION_ERROR_FLAG)) {
                    new SessionLogoutDialog(getActivity(), SummaryFragment.this, errorMsg.split(Pattern.quote(AppConstants.RESPONSE_SPLITTER_STRING))[1].replace("|^", ""));
//                    AppUtils.deleteFolderRecursively(getActivity(), SummaryFragment.this, AppUtils.getRootDirectory(getActivity(), SummaryFragment.this));
                } else {
                    if (errorMsg.contains(AppConstants.RESPONSE_SPLITTER_STRING))
                        errorMsg = errorMsg.replace(AppConstants.RESPONSE_SPLITTER_STRING, "");
                    AppUtils.showAlertDialog(getActivity(), SummaryFragment.this, errorMsg);
                }
            }//if(splittedMsg != null) closes here....
            else
                Log.w(TAG, "splittedMsg is null...");

        }//else if response starts with -1 closes here....
        else
            Log.w(TAG, "Unhandled case : " + response);


    }//if(response != null) closes here....
        else
                Log.w(TAG, "Response is null...");
    }

    @Override
    public void notifyNetworkResponseFailure(Exception exception, String response) {
        Log.d(TAG, "notifyNetworkResponseFailure :: " + response);

    }


    @Override
    public void getFilterList(ArrayList<RelationVO> filterList) {

        FilterAdapter fatcaCountryAdapter = new FilterAdapter(getActivity(), R.layout.spinner_row_layout,
                filterList);
        spinner.setAdapter(fatcaCountryAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySelected  = filterList.get(position).getEntityCode();

                if (leadsAl != null) {
                    leadsAl.clear();
                }
                String url_OpenClosedCases = AppConstants.URL + AppConstants.HUB_SUMMARY_URL;
                Params_POJO params = new Params_POJO();
                params.setUrl(url_OpenClosedCases);
//        PANSUMMARY|^Emp ID|^Session ID|^Start Date|^End Date|^|$

                // formnumber and status
//        % % or code
                responseFor = ResponseFor.SUMMARY;
                String data = AppConstants.PAN_SUMMARY_CALL_TYPE + AppConstants.RESPONSE_SPLITTER_STRING
                        + PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.LOGGED_IN_USER_ID, null)
                        + AppConstants.RESPONSE_SPLITTER_STRING + PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.SESSION_ID,null)
                        + AppConstants.RESPONSE_SPLITTER_STRING + BottomSheetDialogFragments.getLeadsFilter_FromDate().trim()
                        + AppConstants.RESPONSE_SPLITTER_STRING + BottomSheetDialogFragments.getLeadsFilter_ToDate().trim()
                        + AppConstants.RESPONSE_SPLITTER_STRING + BottomSheetDialogFragments.getLeadsFilter_SearchForm().trim()
                        + AppConstants.RESPONSE_SPLITTER_STRING + categorySelected
                        + AppConstants.RESPONSE_SPLITTER_STRING + AppConstants.END_OF_URL;
                params.setData(data);

                PostResponseAsync async = new PostResponseAsync(getActivity(), SummaryFragment.this);
                async.setResponseListener(SummaryFragment.this);
                async.execute(params);

            }//onItemSelected closes here....

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


}