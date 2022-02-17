package com.icicisecurities.hub.ui.fatca;

import android.content.Context;
import android.util.Log;


import com.icicisecurities.hub.connection.NetworkResponseListener;
import com.icicisecurities.hub.connection.Params_POJO;
import com.icicisecurities.hub.connection.PostResponseAsync;
import com.icicisecurities.hub.ui.fatca.model.FatcaVO;
import com.icicisecurities.hub.ui.fatca.model.GetSetCountryListListner;
import com.icicisecurities.hub.utils.AppConstants;
import com.icicisecurities.hub.utils.AppUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * Created by yagneshparikh on 27/10/16.
 */
public class FatcaTask implements NetworkResponseListener {


    //Medium priority NON-UI variables goes below.....
    private Context context;
    private Object instanceOfClass;
    private GetSetCountryListListner getSetCountryListListner;

    //Least priority variables goes below....
    private final String TAG = "AnnexureTask";

    public FatcaTask(Context context, Object instanceOfClass) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.instanceOfClass = instanceOfClass;

    }//AnnexureTask constructor closes here.....


    public void getDetails(){

        ///////////.............CALLING ASYNC & DOWNLOADING DATA TO SHOW IN THE DROPDOWNS............\\\\\\\\\\\\\\\\\\\\\\\

        Params_POJO pojo = new Params_POJO();

        String url = AppConstants.URL+AppConstants.COUNTRY_LIST_SUB_URL;
        pojo.setUrl(url);

        String data = AppConstants.RESPONSE_SPLITTER_STRING
                + AppConstants.RESPONSE_SPLITTER_STRING
                + AppConstants.END_OF_URL;
        pojo.setData(data);

        PostResponseAsync getSpinnersData = new PostResponseAsync(context, FatcaTask.this);
        getSpinnersData.setResponseListener(FatcaTask.this);
        getSpinnersData.execute(pojo);

        /////////////.............CALLING ASYNC & DOWNLOADING DATA TO SHOW IN THE DROPDOWNS............\\\\\\\\\\\\\\\\\\\\\\\
    }//getDetails closes here.....


    @Override
    public void notifyNetworkResponseSuccess(String response) {


        if(response != null){

            if(response.startsWith(AppConstants.SUCCESS_STRING_PREFIX)){

                ArrayList<FatcaVO> countryListAl = new ArrayList<FatcaVO>();
                ArrayList<FatcaVO> countryListTAXAl = new ArrayList<FatcaVO>();



                String[] splittedReponse = response.toString().trim().split(Pattern.quote(AppConstants.END_OF_URL));//|$ will delete the Main response in 3 parts...

                String countryList = splittedReponse[1];

                String[] annexureListObject = countryList.split(Pattern.quote(AppConstants.TILDE_SYMBOL));
                FatcaVO fatcaVO;
                for (int i = 0; i < annexureListObject.length; i++) {

                    fatcaVO = new FatcaVO();

                    String[] countryDetails = annexureListObject[i].split(Pattern.quote(AppConstants.CARET_SYMBOL));

                    fatcaVO.setCountryName(countryDetails[0]);
                    fatcaVO.setCountryCode(countryDetails[1]);


                    countryListAl.add(fatcaVO);

                }//for (int i = 0; i < ocuupationsList.length; i++) closes here.....

                Collections.sort(countryListAl, new Comparator<FatcaVO>() {
                    @Override
                    public int compare(FatcaVO fatcaVO, FatcaVO t1) {
                        return  fatcaVO.getCountryName().compareTo(t1.getCountryName());
                    }
                });

                //for tax country
                for (int i = 0; i < annexureListObject.length; i++) {

                    fatcaVO = new FatcaVO();

                    String[] countryDetails = annexureListObject[i].split(Pattern.quote(AppConstants.CARET_SYMBOL));

                    if(countryDetails[0].equals("USA") || countryDetails[0].equals("usa") || countryDetails[0].equals("india") || countryDetails[0].equals("INDIA")) {
                        Log.d(TAG,"invalid endry in arrayList :"+countryDetails[0]);
                    }else{
                        fatcaVO.setCountryName(countryDetails[0]);
                        fatcaVO.setCountryCode(countryDetails[1]);
                        countryListTAXAl.add(fatcaVO);
                    }



                }//for (int i = 0; i < ocuupationsList.length; i++) closes here.....

                Collections.sort(countryListAl, new Comparator<FatcaVO>() {
                    @Override
                    public int compare(FatcaVO fatcaVO, FatcaVO t1) {
                        return  fatcaVO.getCountryName().compareTo(t1.getCountryName());
                    }
                });

                getSetCountryListListner.getCountryList(countryListAl);
                getSetCountryListListner.getCountryTAXList(countryListTAXAl);


            }//if(response.startsWith(AppConstants.SUCCESS_STRING_PREFIX)) closes here....
            else if(response.startsWith(AppConstants.FAILURE_STRING_PREFIX)){
                //The response is Failure....
                String errorMsg = "";

                //Splitting the String w.r.t the Response Divider String....
                String[] splittedMsg = response.split(Pattern.quote(AppConstants.RESPONSE_SPLITTER_STRING));
//				String[] splittedMsg = response.split(AppConstants.RESPONSE_SPLITTER_STRING);
                if(splittedMsg != null){
                    errorMsg = splittedMsg[1];
                    AppUtils.showAlertDialog(context, FatcaTask.this, errorMsg);
                }//if(splittedMsg != null) closes here....
                else
                    Log.w(TAG, "splittedMsg is null...");

            }//response is Failure closes here....
            else
                Log.w(TAG, "Unhandled case : "+response);

        }//if(response != null) closes here....
        else
            Log.w(TAG, "Response is null...");



    }//notifyNetworkResponseSuccess closes here...

    @Override
    public void notifyNetworkResponseFailure(Exception exception, String response) {

    }//notifyNetworkResponseFailure closes here...

    public void setDetailsListener(GetSetCountryListListner getSetCountryListListner) {
        // TODO Auto-generated method stub
        this.getSetCountryListListner = getSetCountryListListner;
    }//setDetailsListener closes here....

}//FatcaTask closes here...
