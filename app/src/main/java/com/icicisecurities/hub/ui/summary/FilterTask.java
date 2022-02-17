package com.icicisecurities.hub.ui.summary;

import android.content.Context;
import android.preference.PreferenceManager;

import com.icicisecurities.hub.connection.NetworkResponseListener;
import com.icicisecurities.hub.connection.Params_POJO;
import com.icicisecurities.hub.connection.PostResponseAsync;
import com.icicisecurities.hub.ui.new_form.model.RelationVO;
import com.icicisecurities.hub.utils.AppConstants;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class FilterTask implements NetworkResponseListener {

    //Medium priority NON-UI variables goes below.....
    private Context context;
    private Object instanceOfClass;
    private GetFilterListener getFilterListener;

    //Least priority variables goes below....
    private final String TAG = "CountryTask";


    public FilterTask(Context context, Object instanceOfClass) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.instanceOfClass = instanceOfClass;
    }//AnnexureTask constructor closes here.....

    public void getDetails(){

        ///////////.............CALLING ASYNC & DOWNLOADING DATA TO SHOW IN THE DROPDOWNS............\\\\\\\\\\\\\\\\\\\\\\\


        String url_OpenClosedCases = AppConstants.URL + AppConstants.HUB_SUMMARY_URL;
        Params_POJO params = new Params_POJO();
        params.setUrl(url_OpenClosedCases);

//BINDSTATUSDROPDOWN|^ RM Code |^ Session ID |^$

        String data = AppConstants.SUMMARY_STATUS_DROP_DOWN + AppConstants.RESPONSE_SPLITTER_STRING
                + PreferenceManager.getDefaultSharedPreferences(context).getString(AppConstants.LOGGED_IN_USER_ID, null)
                + AppConstants.RESPONSE_SPLITTER_STRING + PreferenceManager.getDefaultSharedPreferences(context).getString(AppConstants.SESSION_ID,null)
                + AppConstants.RESPONSE_SPLITTER_STRING + AppConstants.END_OF_URL;
        params.setData(data);


        PostResponseAsync getSpinnersData = new PostResponseAsync(context, FilterTask.this);
        getSpinnersData.setResponseListener(FilterTask.this);
        getSpinnersData.execute(params);

        /////////////.............CALLING ASYNC & DOWNLOADING DATA TO SHOW IN THE DROPDOWNS............\\\\\\\\\\\\\\\\\\\\\\\
    }//getDetails closes here.....

    @Override
    public void notifyNetworkResponseSuccess(String response) {
        if(response != null) {
            if (response.startsWith(AppConstants.SUCCESS_STRING_PREFIX)) {

                ArrayList<RelationVO> filterDropDownAL = new ArrayList<RelationVO>();

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

                }//for (int i = 0; i < ocuupationsList.length; i++) closes here.....
                getFilterListener.getFilterList(filterDropDownAL);
            }
        }
    }

    @Override
    public void notifyNetworkResponseFailure(Exception exception, String response) {

    }

    public void setDetailsListener(GetFilterListener getFilterListener) {
        // TODO Auto-generated method stub
        this.getFilterListener = getFilterListener;
    }//setDetailsListener closes here....
}
