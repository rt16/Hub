package com.icicisecurities.hub.ui.new_form.model;

import java.util.ArrayList;

/**
 * Created by Ganesh Ghodake on 18/11/16.
 */
public interface PersonalOneSpinnersDataListner {

    /*This will return the Income Category...*/
    public void getIncomeCategory(ArrayList<RelationVO> incomeAL);

    /*This will return the Occupation Category...*/
    public void getOccupationCategoty(ArrayList<RelationVO> occupationAL);

    /*This will return the Relation Category...*/
    public void getRelation(ArrayList<RelationVO> relationAL);

    /*This will return the Address Category...*/
    public void getAddressType(ArrayList<RelationVO> addressTypeAL);

}//PersonalOneSpinnersDataListner closes here....
