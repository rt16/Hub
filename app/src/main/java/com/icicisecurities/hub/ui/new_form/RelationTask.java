package com.icicisecurities.hub.ui.new_form;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.icicisecurities.hub.connection.NetworkResponseListener;
import com.icicisecurities.hub.connection.Params_POJO;
import com.icicisecurities.hub.connection.PostResponseAsync;
import com.icicisecurities.hub.ui.new_form.model.RelationVO;
import com.icicisecurities.hub.ui.new_form.model.PersonalOneSpinnersDataListner;
import com.icicisecurities.hub.utils.AppConstants;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Ganesh Ghodake on 18/11/16.
 */
public class RelationTask implements NetworkResponseListener {

    //Medium priority NON-UI variables goes below.....
    private Context context;
    private Object instanceOfClass;
    private PersonalOneSpinnersDataListner personalOneSpinnersDataListner;

    public RelationTask(Context context, Object instanceOfClass) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.instanceOfClass = instanceOfClass;

    }//RelationTask constructor closes here.....

    public void getDetails(){

        ///////////.............CALLING ASYNC & DOWNLOADING DATA TO SHOW IN THE DROPDOWNS............\\\\\\\\\\\\\\\\\\\\\\\

        Params_POJO pojo = new Params_POJO();

        String url = AppConstants.URL+AppConstants.GET_INCOME_OCCUPATION_DETAILS_SUB_URL;
        pojo.setUrl(url);

        String data = "I" + AppConstants.RESPONSE_SPLITTER_STRING
                + AppConstants.END_OF_URL;
        pojo.setData(data);

        PostResponseAsync getSpinnersData = new PostResponseAsync(context, RelationTask.this);
        getSpinnersData.setResponseListener(RelationTask.this);
        getSpinnersData.execute(pojo);



        /////////////.............CALLING ASYNC & DOWNLOADING DATA TO SHOW IN THE DROPDOWNS............\\\\\\\\\\\\\\\\\\\\\\\
    }//getDetails closes here.....


    public void getDetailsForAddress(){

        ///////////.............CALLING ASYNC & DOWNLOADING DATA TO SHOW IN THE DROPDOWNS............\\\\\\\\\\\\\\\\\\\\\\\

        Params_POJO pojo = new Params_POJO();

        String url = AppConstants.URL+AppConstants.COUNTRY_LIST_SUB_URL;
        pojo.setUrl(url);

        // "A" for Address proof   "S" for state "C" for city

        String data = "A" + AppConstants.RESPONSE_SPLITTER_STRING
                + AppConstants.RESPONSE_SPLITTER_STRING
                + AppConstants.END_OF_URL;
        pojo.setData(data);

        PostResponseAsync getSpinnersData = new PostResponseAsync(context, RelationTask.this);
        getSpinnersData.setResponseListener(RelationTask.this);
        getSpinnersData.execute(pojo);

        /////////////.............CALLING ASYNC & DOWNLOADING DATA TO SHOW IN THE DROPDOWNS............\\\\\\\\\\\\\\\\\\\\\\\
    }//getDetails closes here.....



    @Override
    public void notifyNetworkResponseSuccess(String response) {

        Log.d("PersonalDetailOne ","asdsad" + response);

        if(response != null){

            if(response.startsWith(AppConstants.SUCCESS_STRING_PREFIX)){

                ArrayList<RelationVO> incomeListAl = new ArrayList<RelationVO>();
                ArrayList<RelationVO> occupationListAl = new ArrayList<RelationVO>();
                ArrayList<RelationVO> relationListAl = new ArrayList<RelationVO>();
                ArrayList<RelationVO> addressTypeListAl = new ArrayList<RelationVO>();

                String[] splittedReponse = response.toString().trim().split(Pattern.quote(AppConstants.END_OF_URL));//|$ will delete the Main response in 3 parts...

                String incomeCategoryList = splittedReponse[2];
                String occupationCodeList = splittedReponse[1];
                String relationList = splittedReponse[4];
                String addrTypeList = splittedReponse[7];

                String[] incomeCatListObject = incomeCategoryList.split(Pattern.quote(AppConstants.TILDE_SYMBOL));
                String[] occupationCatListObject = occupationCodeList.split(Pattern.quote(AppConstants.TILDE_SYMBOL));
                String[] relationListObject = relationList.split(Pattern.quote(AppConstants.TILDE_SYMBOL));
                String[] addrTypeObjListObject = addrTypeList.split(Pattern.quote(AppConstants.TILDE_SYMBOL));

                RelationVO relationVO;


                relationVO = new RelationVO();
                relationVO.setEntityCode("");
                relationVO.setEntityDescription("Select Income");
                relationVO.setEntityKRAMapCode("");
                incomeListAl.add(relationVO);

                //for Income
                for (int i = 0; i < incomeCatListObject.length; i++) {

                    relationVO = new RelationVO();

                    String[] details = incomeCatListObject[i].split(Pattern.quote(AppConstants.CARET_SYMBOL));

                    relationVO.setEntityCode(details[0]);
                    relationVO.setEntityDescription(details[1]);
                    relationVO.setEntityKRAMapCode(details[2]);


                    incomeListAl.add(relationVO);

                }//for (int i = 0; i < ocuupationsList.length; i++) closes here.....


                relationVO = new RelationVO();
                relationVO.setEntityCode("");
                relationVO.setEntityDescription("Select Occupation");
                relationVO.setEntityKRAMapCode("");
                occupationListAl.add(relationVO);


                //for Occupation
                for (int i = 0; i < occupationCatListObject.length; i++) {

                    relationVO = new RelationVO();

                    String[] details = occupationCatListObject[i].split(Pattern.quote(AppConstants.CARET_SYMBOL));

                    relationVO.setEntityCode(details[0]);
                    relationVO.setEntityDescription(details[1]);
                    relationVO.setEntityKRAMapCode(details[2]);


                    occupationListAl.add(relationVO);

                }//for (int i = 0; i < ocuupationsList.length; i++) closes here.....

                //for Relation
                for (int i = 0; i < relationListObject.length; i++) {

                    relationVO = new RelationVO();

                    String[] details = relationListObject[i].split(Pattern.quote(AppConstants.CARET_SYMBOL));

                    relationVO.setEntityCode(details[0]);
                    relationVO.setEntityDescription(details[1].replace("|",""));

                    relationListAl.add(relationVO);

                }//for (int i = 0; i < ocuupationsList.length; i++) closes here.....



                /*
                Date : 17.05.2017
                Ganesh Ghodake
                On this Vinod told me to remove "Select Address Type" ad default text and put Residential as defauld
                 */
                //Address Type
//                relationVO = new RelationVO();
//                relationVO.setEntityCode("");
//                relationVO.setEntityDescription("Select Address Type");
//                relationVO.setEntityKRAMapCode("");
//                addressTypeListAl.add(relationVO);


                //for Occupation
                for (int i = 0; i < addrTypeObjListObject.length; i++) {

                    relationVO = new RelationVO();

                    String[] details = addrTypeObjListObject[i].split(Pattern.quote(AppConstants.CARET_SYMBOL));

                    relationVO.setEntityCode(details[0]);
                    relationVO.setEntityDescription(details[1].replace("|",""));



                    addressTypeListAl.add(relationVO);

                }//for (int i = 0; i < ocuupationsList.length; i++) closes here.....



                personalOneSpinnersDataListner.getIncomeCategory(incomeListAl);
                personalOneSpinnersDataListner.getOccupationCategoty(occupationListAl);
                personalOneSpinnersDataListner.getRelation(relationListAl);
                personalOneSpinnersDataListner.getAddressType(addressTypeListAl);


            }//if(response.startsWith(AppConstants.SUCCESS_STRING_PREFIX)) closes here...


        }//if(response != null) closes here...


    }//notifyNetworkResponseSuccess closes here...

    @Override
    public void notifyNetworkResponseFailure(Exception exception, String response) {

    }//notifyNetworkResponseFailure closes here...

    public void setDetailsListener(PersonalOneSpinnersDataListner personalOneSpinnersDataListner) {
        // TODO Auto-generated method stub
        this.personalOneSpinnersDataListner = personalOneSpinnersDataListner;
    }//setDetailsListener closes here....

}//RelationTask closes here.....
