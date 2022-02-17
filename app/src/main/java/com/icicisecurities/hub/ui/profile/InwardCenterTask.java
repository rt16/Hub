package com.icicisecurities.hub.ui.profile;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.icicisecurities.hub.connection.NetworkResponseListener;
import com.icicisecurities.hub.connection.Params_POJO;
import com.icicisecurities.hub.connection.PostResponseAsync;
import com.icicisecurities.hub.ui.profile.model.GetSetInwardListListner;
import com.icicisecurities.hub.ui.profile.model.InwardCenterVO;
import com.icicisecurities.hub.utils.AppConstants;
import com.icicisecurities.hub.utils.AppUtils;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Ganesh Ghodake on 30/01/17.
 */
public class InwardCenterTask implements NetworkResponseListener {

    //Medium priority NON-UI variables goes below.....
    private Context context;
    private Object instanceOfClass;
    private GetSetInwardListListner getSetInwardListListner;
    
    //Least priority variables goes below....
    private final String TAG = "AnnexureTask";

    public InwardCenterTask(Context context, Object instanceOfClass) {
        this.context = context;
        this.instanceOfClass = instanceOfClass;
    }//InwardCenterTask closes here.....

    public void getDetails() {

        ///////////.............CALLING ASYNC & DOWNLOADING DATA TO SHOW IN THE DROPDOWNS............\\\\\\\\\\\\\\\\\\\\\\\

        Params_POJO pojo = new Params_POJO();

        String url = AppConstants.URL + AppConstants.COUNTRY_LIST_SUB_URL;
        pojo.setUrl(url);

        String data = "H|^|$";
        pojo.setData(data);

        PostResponseAsync getSpinnersData = new PostResponseAsync(context, InwardCenterTask.this);
        getSpinnersData.setResponseListener(InwardCenterTask.this);
        getSpinnersData.execute(pojo);

        /////////////.............CALLING ASYNC & DOWNLOADING DATA TO SHOW IN THE DROPDOWNS............\\\\\\\\\\\\\\\\\\\\\\\
    }//getDetails closes here.....

    @Override
    public void notifyNetworkResponseSuccess(String response) {

        if (response != null) {

            if (response.startsWith(AppConstants.SUCCESS_STRING_PREFIX)) {

                ArrayList<InwardCenterVO> inwardCenterList = new ArrayList<InwardCenterVO>();
                String[] splittedReponse = response.toString().trim().split(Pattern.quote(AppConstants.END_OF_URL));//|$ will delete the Main response in 3 parts...

                for (int i = 0; i < splittedReponse.length; i++) {
                    Log.d("TAG" +
                            "", "splittedReponse["+i+"] == "+splittedReponse[i]);
                }
                String annexureCode = splittedReponse[1];
                annexureCode = annexureCode.replace("|^","");

                /**@occupationCode : This String will contains key-value pair of Occupations.
                 * So we will sp[lit all the Occupations & then split the Occupation Code & there Occupation names...
                 *
                 * So, Step 1 >> Splitting all the Occupations using ~ **/

                String[] annexureList = annexureCode.split(Pattern.quote(AppConstants.TILDE_SYMBOL));
                InwardCenterVO inwardCenterVO;
                for (int i = 0; i < annexureList.length; i++) {

                    inwardCenterVO = new InwardCenterVO();

                    String[] annexureDetails = annexureList[i].split(Pattern.quote(AppConstants.CARET_SYMBOL));

                    if(!annexureDetails[0].equals(null) && !annexureDetails[1].equals(null)) {
                        inwardCenterVO.setInwardName(annexureDetails[1]);
                        inwardCenterVO.setInwardCode(annexureDetails[0]);
                        Log.d(TAG,"Name : "+annexureDetails[0]+ " Code : "+annexureDetails[0]);
                    }else{
                        Log.d(TAG, "Error from backend, no values for annexure.");
                    }

                    inwardCenterList.add(inwardCenterVO);

                }//for (int i = 0; i < ocuupationsList.length; i++) closes here.....

                getSetInwardListListner.getInwardCentersList(inwardCenterList);


            }//if(response.startsWith(AppConstants.SUCCESS_STRING_PREFIX)) closes here....
            else if(response.startsWith(AppConstants.FAILURE_STRING_PREFIX)){
                //The response is Failure....
                String errorMsg = "";

                //Splitting the String w.r.t the Response Divider String....
                String[] splittedMsg = response.split(Pattern.quote(AppConstants.RESPONSE_SPLITTER_STRING));
//				String[] splittedMsg = response.split(AppConstants.RESPONSE_SPLITTER_STRING);
                if(splittedMsg != null){
                    errorMsg = splittedMsg[1];
                    AppUtils.showAlertDialog(context, InwardCenterTask.this, errorMsg);
                }//if(splittedMsg != null) closes here....
                else
                    Log.w(TAG, "splittedMsg is null...");

            }//response is Failure closes here....
            else
                Log.w(TAG, "Unhandled case : "+response);

        }//if(response != null) closes here...
    }//notifyNetworkResponseSuccess closes here....

    @Override
    public void notifyNetworkResponseFailure(Exception exception, String response) {

        Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();

    }//notifyNetworkResponseFailure closes here....

    public void setDetailsListener(GetSetInwardListListner getSetInwardListListner) {
        // TODO Auto-generated method stub
        this.getSetInwardListListner = getSetInwardListListner;
    }//setDetailsListener closes here....

}//InwardCenterTask closes here....
