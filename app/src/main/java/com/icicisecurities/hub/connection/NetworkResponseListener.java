package com.icicisecurities.hub.connection;

public interface NetworkResponseListener {

    public void notifyNetworkResponseSuccess(String response);//If resposne starts with 0, then response is success it will give the control back to class...



    /**
     * When we connect to some URL, there can be some connection issue.
     * So the coonection issues are reverted back in this listener method...
     *
     *
     * @param exception
     * @param response
     */
    public void notifyNetworkResponseFailure(Exception exception, String response);//If response starts with 1, then response is Failure (i.e. some validation error or something).
}


