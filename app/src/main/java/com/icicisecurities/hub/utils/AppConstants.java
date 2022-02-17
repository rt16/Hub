package com.icicisecurities.hub.utils;

public class AppConstants {

    //INTERNET URL FOR UAT....
    public static final String URL = "https://203.189.92.210/ria/Customer/";

    //INTERNET URL FOR DEV....
//	public static final String URL = "http://203.189.92.210/ria/Customer/";

    //INTERNET URL FOR LIVE....
//	public static final String URL = "https://secure.icicidirect.com/idirecttrading/TradeRacerWeb/Customer/";
//	public static final String URL = "https://nri.icicidirect.com/idirecttrading/TradeRacerWeb/Customer/";

    public static final String LOGIN_URL = "MBLLogin.aspx";
    public static final String COUNTRY_LIST_SUB_URL = "Ri_AccOpeningOccCountry.aspx";
    public static final String FATCA_SUB_URL = "Ri_AccOpenFATCADtls.aspx";
    public static final String SUBMIT_NEW_FORM_URL = "RI_AccOpenHubActions.aspx";
    public static final String GET_INCOME_OCCUPATION_DETAILS_SUB_URL = "Ri_AccOpeningIncomeOccCate.aspx";
    public static final String HUB_SUMMARY_URL = "RI_AccOpenHubActions.aspx";
    public static final String BANK_FETCH_CALL_FOR_OTP = "Ri_AccOpenNewBankCalls.aspx";
    public static final String WEBKIT_SUB_URL = "Ri_AccOpeningEsigner.aspx";

    public static final String SUCCESS_STRING_PREFIX = "0";
    public static final String ERROR_STRING_PREFIX = "-1";
    public static final String FAILURE_STRING_PREFIX = "1";

    public static final String END_OF_URL = "|$";
    public static final String RESPONSE_SPLITTER_STRING = "|^";
    public static final String TILDE_SYMBOL = "~";
    public static final String CARET_SYMBOL = "^";
    public static final String APP_DATE_FORMAT = "dd-MMM-yyyy";
    public static final String PAN_DATE_FORMAT = "dd-MM-yyyy";//Changes told by UAT.



    public static final String SESSION_ERROR_FLAG = "SSSN";

    public static final String FATCA_CALL_TYPE = "FTCH";
    public static final String SUBMIT_NEW_FORM_CALL_TYPE = "HUB_INSERT";
    public static final String PAN_SUMMARY_CALL_TYPE = "PANSUMMARY";
    public static final String NOTIFICATION = "NOTFCN_HUB";
    public static final String INWARD_CENTER_FLAG = "IWC";
    public static final String CHANGE_REJECTION_STATUS = "CHANGEREJECTIONSTATUS";
    public static final String SUMMARY_STATUS_DROP_DOWN = "BINDSTATUSDROPDOWN";
    public static final String GET_PRODUCT_TYPE = "GETPRODUCTTYPE";


    public static final String FATCA_BIRTH_COUNTRY = "FATCA_BIRTH_COUNTRY";
    public static final String FATCA_TAX_COUNTRY_TWO = "FATCA_TAX_COUNTRY_TWO";
    public static final String FORM_NUMBER = "FORM_NUMBER";
    public static final String LOGGED_IN_USER_ID = "LOGGED_IN_USER_ID";
    public static final String SESSION_ID = "SESSION_ID";
    public static final String FATCA_COUNTRY = "FATCA_COUNTRY";
    public static final String FATCA_CITY = "FATCA_CITY";
    public static final String FATCA_STATE = "FATCA_STATE";
    public static final String FATCA_PIN_CODE = "FATCA_PIN_CODE";
    public static final String INWARD_CENTER = "INWARD_CENTER";
    public static final String PRODUCT_FLAG = "PRD";




}
