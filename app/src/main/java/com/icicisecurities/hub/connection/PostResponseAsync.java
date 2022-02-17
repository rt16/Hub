package com.icicisecurities.hub.connection;



import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.icicisecurities.hub.R;
import com.icicisecurities.hub.utils.AppUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

public class PostResponseAsync extends AsyncTask<Params_POJO, String, String> {

    //High priority UI variables goes below.....
    private ProgressDialog progressDialog;

    //Medium priority NON-UI variables goes below...
    private Context context;
    private Object instanceOfClass;
    private NetworkResponseListener listener;
    private Exception exception;//If in some case, some Exception occurs then we send back this Exception to the Calling Class....

    //Least priority variables goes below.....
    private final String TAG = "PostResponseAsync";

    public PostResponseAsync(Context context, Object instanceOfClass) {
        this.context = context;
        this.instanceOfClass = instanceOfClass;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.pleaseWaitSpelling));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
    }//PostResponseAsync closes here....



    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub

        //Start the progressDialog here...
        progressDialog.show();
        super.onPreExecute();
    }//onPreExecute closes here....


    @Override
    protected String doInBackground(Params_POJO... params) {
        // TODO Auto-generated method stub
        // UAT Code Without Certificate Pinning
        String response = null;
        DataOutputStream wr = null;
        InputStream is = null;
        BufferedReader rd = null;
        try{
//			Below code shud only be activated if, we are extracting build for UAT, Bcoz UAT certificate is expired....
//			#1 ... Bypassing SSL Certificate chking in Android....
            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            sc.init(null, new TrustManager[] { new TrustAllX509TrustManager() }, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier(){
                public boolean verify(String string, SSLSession ssls) {
                    return true;
                }
            });
            ///////////////////////////////////////////////////////////

            String url_string = params[0].getUrl();
            Log.d(TAG, "url_string == "+url_string);
            URL url = null;
            String urlParameters = params[0].getData();
            Log.d(TAG, "urlParameters == "+urlParameters);
            //AccountOpeningExceptionHandler accountOpeningExceptionHandler=new AccountOpeningExceptionHandler(context,PostResponseAsync.this);
            //accountOpeningExceptionHandler.uncaughtException(null,new Exception());
            HttpsURLConnection connection = null;
            try {
                //Create connection
                url = new URL(url_string);
                connection = (HttpsURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");
                //					if(!(instanceOfClass instanceof VisitingCardFragment))//Not condition....[i.e. If user is uploading the Images then don't use the Timeout, otherwise use this Timeout thing.....]
                //						connection.setReadTimeout(10000);

                connection.setFixedLengthStreamingMode(urlParameters.getBytes().length);

                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //Send request
                wr = new DataOutputStream (connection.getOutputStream ());
                wr.writeBytes (urlParameters);

                //Get Response
                is = connection.getInputStream();
                rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer responseStringBuffer = new StringBuffer();
                while((line = rd.readLine()) != null) {
                    responseStringBuffer.append(line);
                    responseStringBuffer.append('\r');
                }

                response = responseStringBuffer.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
                exception = e;
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                else
                    Log.w(TAG, "HttpURLCOnnection's variable connection is null...");
                if(wr != null){
                    wr.flush ();
                    wr.close ();
                }
                else
                    Log.w(TAG, "Data OutputStream variable wr is null...");
                if(rd != null)
                    rd.close();
                else
                    Log.w(TAG, "BufferReader variable rd is null...");
                if(is != null)
                    is.close();
                else
                    Log.w(TAG, "InputStreamReader variable is is null...");
            }//finally closes here....
        }//try closes here....
        catch(Exception e){
            Log.e(TAG, "Exception while posting Parameters to get Response from Service : "+e);
            e.printStackTrace();
            exception = e;
        }//catch closes here....
        return response;


////UAT with Certificate Pinning Code
//		String response = null;
//		DataOutputStream wr = null;
//		InputStream is = null;
//		BufferedReader rd = null;
//
//		try{
//			Certificate ca;
//			CertificateFactory cf = CertificateFactory.getInstance("X.509");
//			InputStream caInput = new BufferedInputStream((context.getAssets().open("uatcert.crt")));
//
//			try {
//				ca = cf.generateCertificate(caInput);
////				System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
//			} finally {
//				caInput.close();
//			}
//
//			// Create a KeyStore containing our trusted CAs
//			String keyStoreType = KeyStore.getDefaultType();
//			KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//			keyStore.load(null, null);
//			keyStore.setCertificateEntry("ca", ca);
//
//			// Create a TrustManager that trusts the CAs in our KeyStore
//			String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//			TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//			tmf.init(keyStore);
//
//			// Create an SSLContext that uses our TrustManager
//			SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
//			sslContext.init(null, tmf.getTrustManagers(), null);
//
//
//			HostnameVerifier hostnameVerifier = new HostnameVerifier() {
//				@Override
//				public boolean verify(String s, SSLSession sslSession) {
//					HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
//					return true;
//				}
//			};
//
////			Below code shud only be activated if, we are extracting build for UAT, Bcoz UAT certificate is expired....
////			#1 ... Bypassing SSL Certificate chking in Android....
////			SSLContext sc = SSLContext.getInstance("TLSv1.2");
////			sc.init(null, new TrustManager[] { new TrustAllX509TrustManager() }, new java.security.SecureRandom());
////			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
////			HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier(){
////				public boolean verify(String string,SSLSession ssls) {
////					return true;
////				}
////			});
//			///////////////////////////////////////////////////////////
//
//			String url_string = params[0].getUrl();
////			Log.d(TAG, "url_string == "+url_string);
//			URL url = null;
//			String urlParameters = params[0].getData();
////			Log.d(TAG, "urlParameters == "+urlParameters);
//			//AccountOpeningExceptionHandler accountOpeningExceptionHandler=new AccountOpeningExceptionHandler(context,PostResponseAsync.this);
//			//accountOpeningExceptionHandler.uncaughtException(null,new Exception());
//			HttpsURLConnection connection = null;
//			try {
//				//Create connection
//				url = new URL(url_string);
//				// Tell the URLConnection to use a SocketFactory from our SSLContext
//
//				connection = (HttpsURLConnection)url.openConnection();
//				connection.setHostnameVerifier(hostnameVerifier);
//				connection.setSSLSocketFactory(sslContext.getSocketFactory());
//				connection.setRequestMethod("POST");
//				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//				connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
//				connection.setRequestProperty("Content-Language", "en-US");
//
//				connection.setFixedLengthStreamingMode(urlParameters.getBytes().length);
//
//				connection.setUseCaches(false);
//				connection.setDoInput(true);
//				connection.setDoOutput(true);
//
//				//Send request
//				wr = new DataOutputStream (connection.getOutputStream ());
//				wr.writeBytes (urlParameters);
//
//				//Get Response
//				is = connection.getInputStream();
//				rd = new BufferedReader(new InputStreamReader(is));
//				String line;
//				StringBuffer responseStringBuffer = new StringBuffer();
//				while((line = rd.readLine()) != null) {
//					responseStringBuffer.append(line);
//					responseStringBuffer.append('\r');
//				}
//
//				response = responseStringBuffer.toString().trim();
//			} catch (Exception e) {
////				Log.d(TAG, e.getMessage());
//				exception = e;
//			} finally {
//				if(connection != null) {
//					connection.disconnect();
//				}
//				else
//					Log.w(TAG, "HttpURLCOnnection's variable connection is null...");
//				if(wr != null){
//					wr.flush ();
//					wr.close ();
//				}
//				else
//					Log.w(TAG, "Data OutputStream variable wr is null...");
//				if(rd != null)
//					rd.close();
//				else
//					Log.w(TAG, "BufferReader variable rd is null...");
//				if(is != null)
//					is.close();
//				else
//					Log.w(TAG, "InputStreamReader variable is is null...");
//			}//finally closes here....
//		}//try closes here....
//		catch(Exception e){
//			Log.e(TAG, "Exception while posting Parameters to get Response from Service : "+e);
//			exception = e;
//		}//catch closes here....
//				return response;



//DEV CODE
        // TODO Auto-generated method stub
//		String response = null;
//		DataOutputStream wr = null;
//		InputStream is = null;
//		BufferedReader rd = null;
//		try{
//			//#1 ... Bypassing SSL Certificate chking in Android....
//			SSLContext sc = SSLContext.getInstance("TLS");
//			sc.init(null, new TrustManager[] { new TrustAllX509TrustManager() }, new java.security.SecureRandom());
//			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//			HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier(){
//				public boolean verify(String string,SSLSession ssls) {
//					return true;
//				}
//			});
////			///////////////////////////////////////////////////////////
//
//			//Step : 1 >> Chk whether device is connected to network or not !!
//	            String url_string = params[0].getUrl();
//				Log.d(TAG, "url_string == "+url_string);
//				URL url = null;
//				String urlParameters = params[0].getData();
//				Log.d(TAG, "urlParameters == "+urlParameters);
//				HttpURLConnection connection = null;
//				try {
//					//Create connection
//					url = new URL(url_string);
//					connection = (HttpURLConnection)url.openConnection();
//					connection.setRequestMethod("POST");
//					connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//					connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
//					connection.setRequestProperty("Content-Language", "en-US");
//					connection.setRequestProperty("connection", "close");
////					connection.setReadTimeout(AppConstants.CONNECTION_TIMEOUT*3);
//					connection.setFixedLengthStreamingMode(urlParameters.getBytes().length);
//					connection.setUseCaches(false);
//					connection.setDoInput(true);
//					connection.setDoOutput(true);
//
//
//
//					//Send request
//					wr = new DataOutputStream (connection.getOutputStream ());
//					wr.writeBytes (urlParameters);
//
//					// Get the response code
//					/*int statusCode = connection.getResponseCode();
//
//					if (statusCode >= 200 && statusCode < 400) {
//						// Create an InputStream in order to extract the response object
//						is = connection.getInputStream();
//					}
//					else {
//						is = connection.getErrorStream();
//					}*/
//
//					//Get Response
//					is = connection.getInputStream();
//					rd = new BufferedReader(new InputStreamReader(is));
//					String line;
//					StringBuffer responseStringBuffer = new StringBuffer();
//					while((line = rd.readLine()) != null) {
//						responseStringBuffer.append(line);
//						responseStringBuffer.append('\r');
//					}
//
//					response = responseStringBuffer.toString().trim();
//				} catch (Exception e) {
//					e.printStackTrace();
//					exception = e;
//				} finally {
//					if(connection != null) {
//						connection.disconnect();
//					}
//					else
//						Log.w(TAG, "HttpURLCOnnection's variable connection is null...");
//					if(wr != null){
//						wr.flush ();
//						wr.close ();
//					}
//					else
//						Log.w(TAG, "Data OutputStream variable wr is null...");
//					if(rd != null)
//						rd.close();
//					else
//						Log.w(TAG, "BufferReader variable rd is null...");
//					if(is != null)
//						is.close();
//					else
//						Log.w(TAG, "InputStreamReader variable is is null...");
//				}//finally closes here....
//		}//try closes here....
//		catch(Exception e){
//			Log.e(TAG, "Exception while posting Parameters to get Response from Service : "+e);
//			e.printStackTrace();
//			exception = e;
//		}//catch closes here....
//			return response;

    }//doInBackground closes here....






    public void setResponseListener(NetworkResponseListener listener) {
        // TODO Auto-generated method stub
        this.listener = listener;
    }//setResponseListener closes here....



    public void setDetails(Context context, Object instnceOfClass){
        this.context = context;
        this.instanceOfClass = instnceOfClass;
    }//setDetails closes here....


    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);


        //Stop the progressDialog here...
        if(progressDialog != null && progressDialog.isShowing())

            try {
                progressDialog.dismiss();//Closing the Dialog here....
            }catch (Exception e){
                //Log.d(TAG, "Cant not dismiss" +e.getMessage());
            }
        Log.d(TAG, "Response = "+result);

        if(result == null)
            result = "";


        if(result.contains("Resource not available.")){
            AppUtils.showAlertDialog(context, PostResponseAsync.this,"Resource not available.");
        }//if RNA exists closes here....
        else{
            Log.e("Response",result.toString());
            //else NOT RNA starts below....
            //Sending the Response back to the corresponding class....
            if(listener != null && result != null && !result.isEmpty()){
                listener.notifyNetworkResponseSuccess(result.toString().trim());
            }//if(listener != null) closes here....
            else{
                //else response is error....
                if (listener != null) {
                    listener.notifyNetworkResponseFailure(exception, result);
                }
            }//else response is error closes here....
        }//else not RNA closes here......
    }//onPostExecute closes here....
}
