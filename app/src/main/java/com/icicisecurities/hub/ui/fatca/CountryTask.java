package com.icicisecurities.hub.ui.fatca;

import android.content.Context;
import android.util.Log;

import com.icicisecurities.hub.connection.NetworkResponseListener;
import com.icicisecurities.hub.connection.Params_POJO;
import com.icicisecurities.hub.connection.PostResponseAsync;
import com.icicisecurities.hub.ui.fatca.model.FatcaVO;
import com.icicisecurities.hub.ui.fatca.model.GetCountry;
import com.icicisecurities.hub.utils.AppConstants;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Ganesh Ghodake on 31/03/17.
 */

public class CountryTask implements NetworkResponseListener {

    //Medium priority NON-UI variables goes below.....
    private Context context;
    private Object instanceOfClass;
    private GetCountry getCountry;

    //Least priority variables goes below....
    private final String TAG = "CountryTask";

    public CountryTask(Context context, Object instanceOfClass) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.instanceOfClass = instanceOfClass;
    }//AnnexureTask constructor closes here.....


    public void getDetails(){

        ///////////.............CALLING ASYNC & DOWNLOADING DATA TO SHOW IN THE DROPDOWNS............\\\\\\\\\\\\\\\\\\\\\\\

        Params_POJO pojo = new Params_POJO();

        String url = AppConstants.URL+AppConstants.COUNTRY_LIST_SUB_URL;
        pojo.setUrl(url);

        String data = "C" + AppConstants.RESPONSE_SPLITTER_STRING
                + "" + AppConstants.RESPONSE_SPLITTER_STRING
                + AppConstants.END_OF_URL;
        pojo.setData(data);

        PostResponseAsync getSpinnersData = new PostResponseAsync(context, CountryTask.this);
        getSpinnersData.setResponseListener(CountryTask.this);
        getSpinnersData.execute(pojo);

        /////////////.............CALLING ASYNC & DOWNLOADING DATA TO SHOW IN THE DROPDOWNS............\\\\\\\\\\\\\\\\\\\\\\\
    }//getDetails closes here.....


    @Override
    public void notifyNetworkResponseSuccess(String response) {
        Log.d(TAG,"sadasdasdasdasdasd"+response);

        if(response != null) {
            if (response.startsWith(AppConstants.SUCCESS_STRING_PREFIX)) {

                ArrayList<FatcaVO> countryListAl = new ArrayList<FatcaVO>();
                String[] splittedReponse = response.toString().trim().split(Pattern.quote(AppConstants.END_OF_URL));
                String countryList = splittedReponse[1];
                String[] countryObject = countryList.split(Pattern.quote(AppConstants.TILDE_SYMBOL));

                FatcaVO fatcaVO;

                fatcaVO = new FatcaVO();
                fatcaVO.setCountryName("Select Country");
                countryListAl.add(fatcaVO);

                for (int i = 0; i < countryObject.length; i++) {

                    fatcaVO = new FatcaVO();

                    String[] countryDetails = countryObject[i].split(Pattern.quote(AppConstants.CARET_SYMBOL));

                    if(countryDetails[0].equals("USA") || countryDetails[0].equals("usa") || countryDetails[0].equals("india") || countryDetails[0].equals("INDIA")) {
                        Log.d(TAG,"invalid endry in arrayList :"+countryDetails[0]);
                    }else {
                        fatcaVO.setCountryName(countryDetails[0]);
                        countryListAl.add(fatcaVO);
                    }

                }//for (int i = 0; i < ocuupationsList.length; i++) closes here.....
                getCountry.getOtherCountryList(countryListAl);
            }
        }

    }//notifyNetworkResponseSuccess closes here....

    @Override
    public void notifyNetworkResponseFailure(Exception exception, String response) {

    }//notifyNetworkResponseFailure closes here....

    public void setDetailsListener(GetCountry getCountry) {
        // TODO Auto-generated method stub
        this.getCountry = getCountry;
    }//setDetailsListener closes here....

}//CountryTask closes here....
