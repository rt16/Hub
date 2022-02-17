package com.icicisecurities.hub.ui.new_form;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.icicisecurities.hub.connection.NetworkResponseListener;
import com.icicisecurities.hub.connection.Params_POJO;
import com.icicisecurities.hub.connection.PostResponseAsync;
import com.icicisecurities.hub.ui.new_form.model.GetProductsListener;
import com.icicisecurities.hub.ui.new_form.model.PersonalOneSpinnersDataListner;
import com.icicisecurities.hub.ui.new_form.model.ProductVO;
import com.icicisecurities.hub.ui.new_form.model.RelationVO;
import com.icicisecurities.hub.utils.AppConstants;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ProductTask implements NetworkResponseListener {
    private String TAG = getClass().getSimpleName();
    //Medium priority NON-UI variables goes below.....
    private Context context;
    private Object instanceOfClass;
    private GetProductsListener getProductsListener;

    public ProductTask(Context context, Object instanceOfClass) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.instanceOfClass = instanceOfClass;

    }//RelationTask constructor closes here.....

    public void getProductTypeDetails(String custType){
        Params_POJO pojo = new Params_POJO();
//        http://203.189.92.210/ria/Customer/RI_AccOpenHubActions.aspx?requeststring=GETPRODUCTTYPE|^IND|^PRD|^C711095|^0

        String url = AppConstants.URL+AppConstants.HUB_SUMMARY_URL;
        pojo.setUrl(url);

//        GETPRODUCTTYPE|^IND|^PRD|^C711095|^0

        String data = AppConstants.GET_PRODUCT_TYPE + AppConstants.RESPONSE_SPLITTER_STRING
                + custType + AppConstants.RESPONSE_SPLITTER_STRING
                + AppConstants.PRODUCT_FLAG + AppConstants.RESPONSE_SPLITTER_STRING
                + PreferenceManager.getDefaultSharedPreferences(context).getString(AppConstants.LOGGED_IN_USER_ID,"") +AppConstants.RESPONSE_SPLITTER_STRING
                + PreferenceManager.getDefaultSharedPreferences(context).getString(AppConstants.SESSION_ID,"") ;

        pojo.setData(data);

        PostResponseAsync getSpinnersData = new PostResponseAsync(context, ProductTask.this);
        getSpinnersData.setResponseListener(ProductTask.this);
        getSpinnersData.execute(pojo);

    }


    public void setProductDetailsListener(GetProductsListener getProductsListener) {
        // TODO Auto-generated method stub
        this.getProductsListener = getProductsListener;
    }//setDetailsListener closes here....

    @Override
    public void notifyNetworkResponseSuccess(String response) {
//        Log.d(TAG, "notifyNetworkResponseSuccess : " + response);
//        0|$ONE-IN-ONE|$RI-NRI|$E-INVEST-CURFUT|$MULTI-INVEST|$E-INVEST|$E-INVEST-PLUS|$

        if(response != null){

            if(response.startsWith(AppConstants.SUCCESS_STRING_PREFIX)){

                ArrayList<ProductVO> productTypeList = new ArrayList<ProductVO>();

                String[] splittedReponse = response.toString().trim().split(Pattern.quote(AppConstants.END_OF_URL));//|$ will delete the Main response in 3 parts...

                ProductVO productVO;

                for (int i = 1; i < splittedReponse.length; i++) {

                    productVO = new ProductVO();
                    productVO.setProductType(splittedReponse[i]);
                    productTypeList.add(productVO);

                }

                getProductsListener.getProducts(productTypeList);


            }//if(response.startsWith(AppConstants.SUCCESS_STRING_PREFIX)) closes here...


        }//if(response != null) closes here...

    }

    @Override
    public void notifyNetworkResponseFailure(Exception exception, String response) {
        Log.d(TAG, "notifyNetworkResponseFailure : " + response);
        ArrayList<ProductVO> productVOArrayList = new ArrayList<>();
        getProductsListener.getProducts(productVOArrayList);
    }
}
