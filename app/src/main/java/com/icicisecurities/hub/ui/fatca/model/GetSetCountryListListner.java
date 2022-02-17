package com.icicisecurities.hub.ui.fatca.model;

import java.util.ArrayList;

/**
 * Created by Ganesh Hhodake on 27/10/16.
 */
public interface GetSetCountryListListner  {

    /*This will return the Income Category...*/
    public void getCountryList(ArrayList<FatcaVO> fatcaAl);

    /*This will return the Income Category...*/
    public void getCountryTAXList(ArrayList<FatcaVO> fatcaAl);


}
