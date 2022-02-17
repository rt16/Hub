package com.icicisecurities.hub.ui.fatca;


import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.icicisecurities.hub.R;
import com.icicisecurities.hub.connection.NetworkResponseListener;
import com.icicisecurities.hub.connection.Params_POJO;
import com.icicisecurities.hub.connection.PostResponseAsync;
import com.icicisecurities.hub.ui.fatca.model.FatcaVO;
import com.icicisecurities.hub.ui.fatca.model.GetCityListener;
import com.icicisecurities.hub.ui.fatca.model.GetCountry;
import com.icicisecurities.hub.ui.fatca.model.GetSetCountryListListner;
import com.icicisecurities.hub.ui.fatca.model.GetStateListenr;
import com.icicisecurities.hub.utils.AppConstants;
import com.icicisecurities.hub.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.ButterKnife;

import static com.icicisecurities.hub.utils.AppUtils.showSnackBar;

public class FatcaFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, GetSetCountryListListner, View.OnClickListener, NetworkResponseListener, GetCountry, GetStateListenr, GetCityListener {

    private FatcaViewModel fatcaViewModel;
        //High priority variables goes below
        private View view;
        private AppCompatSpinner countryOfBirth, countryOfTaxResi, sourceOfWealth, juridictionAddrSpinner,fatcaJuridictionCountrySpinner,
                fatcaJuridictionStateSpinner,fatcaJuridictionCitySpinner;
        private AppCompatCheckBox usIndiciaChkBx, usPersonChkBox, otherCountryChkBox;
        private TextView usIndiciaResponcetext, usPersonResponcetext, otherCountryResponseText;
        private MaterialButton submitButton;
        private TextInputEditText taxableCountryOneET, taxableCountryOneTinET, taxableCountryTwoTinET, placeOfBirthET;
        private TextInputEditText addrLineOne,addrLineTwo,addrLineThree, pinCode;
        private LinearLayout placeOfBirthContainer, juridictionAddressContainer, juridictionOtherAddressContainer;

        String addressType = "Y", addrLine1, addrLine2, addrLine3, country = "", state = "", city = "";

        //High priority NON UI vars
        private String countryOfBirthString, countryOfTaxResidencyString, sourceOfWealthString;
        private String sourceOfWealthCodeString;
        private boolean preLoadedDropDownFlag = false;

        //Views to hide
        private LinearLayout usPersonContainer, anyOtherCountryContainer;


        //Least priority variables goes below....
        private final String TAG = "FatcaFragment";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


            fatcaViewModel =
                    ViewModelProviders.of(this).get(FatcaViewModel.class);
            view = inflater.inflate(R.layout.fragment_fatca, container, false);
            ButterKnife.bind(this,view);

            getActivity().setTitle(getResources().getString(R.string.fatcaPageName));
            /**For v-4.0, for Overflow flow.**/
//        AppUtils.getOverFlowSharedPrefeerence(getActivity()).edit().putString(AppConstants.FATCA_FRAG_OVERFLOW_CHECK, AppConstants.FATCA_FRAG_OVERFLOW_CHECK).commit();

            initializations();


            FatcaTask fatcaTask = new FatcaTask(getActivity(), FatcaFragment.this);
            fatcaTask.setDetailsListener(FatcaFragment.this);
            fatcaTask.getDetails();

            CountryTask fatcaCountryTask = new CountryTask(getActivity(), FatcaFragment.this);
            fatcaCountryTask.setDetailsListener(FatcaFragment.this);
            fatcaCountryTask.getDetails();


            return view;
        }

        private void initializations() {
            //spinners
            countryOfBirth =  view.findViewById(R.id.fatcaCountryOfBirthSpenner);
            countryOfTaxResi =  view.findViewById(R.id.fatcaTacResiCountrySpinner);
            sourceOfWealth =  view.findViewById(R.id.fatcaSourceWealthSpinner);

            //checkBox US indicia
            usIndiciaChkBx =  view.findViewById(R.id.fatcaUsIndiciaChkBox);
            usIndiciaResponcetext =  view.findViewById(R.id.fatcaUsIndiCiaChkBoxRespomseTxtView);
            usIndiciaResponcetext.setText(getResources().getString(R.string.OopsNoIDontSpelling));

            //placeOfBirthContainer
            placeOfBirthContainer =  view.findViewById(R.id.placeOfBirthContainer);
            placeOfBirthET =  view.findViewById(R.id.placeOfBirthEdtTxt);

            //are you a US person
            usPersonChkBox =  view.findViewById(R.id.fatcaUSPersonChkBox);
            usPersonContainer =  view.findViewById(R.id.fatcaUSPersonContainer);
            usPersonContainer.setVisibility(View.GONE);
            usPersonResponcetext = (TextView) view.findViewById(R.id.fatcaUspersonChkBoxRespomseTxtView);
            usPersonResponcetext.setText(getResources().getString(R.string.OopsNoIDontSpelling));

            //tax resident in any othe country
            otherCountryChkBox =  view.findViewById(R.id.fatcaOtherCountryChkBox);
            otherCountryResponseText = (TextView) view.findViewById(R.id.fatcaOtherCountryChkBoxResponseTxtView);
            otherCountryResponseText.setText(getResources().getString(R.string.OopsNoIDontSpelling));
            anyOtherCountryContainer = (LinearLayout) view.findViewById(R.id.fatcaTaxableCountryTwoContainer);
            anyOtherCountryContainer.setVisibility(View.GONE);

            //taxable country EditTexts
            taxableCountryOneET =  view.findViewById(R.id.fatcaUSPersonEditTextUS);
            taxableCountryOneTinET =  view.findViewById(R.id.fatcaUSPersonEditTextUSTin);
            taxableCountryTwoTinET =  view.findViewById(R.id.fatcaTacResiCountryTinEditText);

            //juridicytion Address
            juridictionAddressContainer = (LinearLayout) view.findViewById(R.id.fatcaJuridictionAddrContaine);
            juridictionAddrSpinner =  view.findViewById(R.id.fatcaJuridictionAddressSpinner);
            juridictionOtherAddressContainer = (LinearLayout) view.findViewById(R.id.juridictionAdressContainer);

            fatcaJuridictionCountrySpinner =  view.findViewById(R.id.fatcaJuridictionCountrySpinner);
            fatcaJuridictionStateSpinner =  view.findViewById(R.id.fatcaJuridictionStateSpinner);

            fatcaJuridictionCitySpinner =  view.findViewById(R.id.fatcaJuridictionCitySpinner);

            addrLineOne =  view.findViewById(R.id.fatcaJuridictionAdrLine1);
            addrLineTwo =  view.findViewById(R.id.fatcaJuridictionAdrLine2);
            addrLineThree =  view.findViewById(R.id.fatcaJuridictionAdrLine3);

//        //address line 1,2 and three should not have any special characters
//        addrLineOne.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(36)});
//        addrLineTwo.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(36)});
//        addrLineThree.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(8)});

            pinCode =  view.findViewById(R.id.fatcaJuridictionAdrPinCode);
//        pinCode.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(10)});

            ((TextInputLayout) view.findViewById(R.id.fatcaJuridictionAdrLine1Title))
                    .setHint(AppUtils.mandatorySpanned(getActivity(),
                            FatcaFragment.this,
                            getResources().getString(R.string.juridictionAddressLine1),""));

            ((TextInputLayout) view.findViewById(R.id.fatcaJuridictionAdrLine2Title))
                    .setHint(AppUtils.mandatorySpanned(getActivity(),
                            FatcaFragment.this,
                            getResources().getString(R.string.juridictionAddressLine2),""));

            ((TextInputLayout) view.findViewById(R.id.fatcaJuridictionAdrLine3Title))
                    .setHint(AppUtils.mandatorySpanned(getActivity(),
                            FatcaFragment.this,
                            getResources().getString(R.string.juridictionAddressLine3),""));

            ((TextInputLayout) view.findViewById(R.id.fatcaJuridictionAdrPinCodeTitle))
                    .setHint(AppUtils.mandatorySpanned(getActivity(),
                            FatcaFragment.this,
                            getResources().getString(R.string.juridictionAddressPincode),""));


            //setting text for country state and city spinner
            ((TextView) view.findViewById(R.id.fatcaJuridictionCountrySpinnerTitle))
                    .setHint(AppUtils.mandatorySpanned(getActivity(),
                            FatcaFragment.this,
                            getResources().getString(R.string.juridictionAddressCountry),""));

            ((TextView) view.findViewById(R.id.fatcaJuridictionStateSpinnerTitle))
                    .setHint(AppUtils.mandatorySpanned(getActivity(),
                            FatcaFragment.this,
                            getResources().getString(R.string.juridictionAddressState),""));

            ((TextView) view.findViewById(R.id.fatcaJuridictionCitySpinnerTitle))
                    .setHint(AppUtils.mandatorySpanned(getActivity(),
                            FatcaFragment.this,
                            getResources().getString(R.string.juridictionAddressCity),""));

            submitButton =  view.findViewById(R.id.nxtBtnFATCA);

            //source of wealth spinner
            sourceOfWealth = view.findViewById(R.id.fatcaSourceWealthSpinner);

            //Source of icome values

            final List<String> categories = new ArrayList<String>();
            categories.add("Salary");
            categories.add("business Income");
            categories.add("Gift");
            categories.add("Ancestral Property");
            categories.add("Rental Income");
            categories.add("Prize Money");
            categories.add("Royalty");
            categories.add("Others");

            ArrayAdapter<String> wealthAdater = new ArrayAdapter<String>
                    (getActivity(), R.layout.spinner_textview, categories);
//        wealthAdater.setDropDownViewResource
//                (android.R.layout.simple_spinner_dropdown_item);
            sourceOfWealth.setAdapter(wealthAdater);

            sourceOfWealth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

//                ((TextView) adapterView.getChildAt(i)).setTextColor(Color.parseColor("#ffffff"));
//
                    sourceOfWealthString = categories.get(i).trim();
                    int temp = i + 1;
                    sourceOfWealthCodeString = "0" + temp;

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    sourceOfWealthString = categories.get(0).trim();
                    sourceOfWealthCodeString = "1";
                }
            });


//        ArrayAdapter<String> juridictionAdapter = new ArrayAdapter<String>
//                (getActivity(), android.R.layout.simple_spinner_item, getActivity().getResources().getStringArray(R.array.juridictionAddress));
//        juridictionAdapter.setDropDownViewResource
//                (android.R.layout.simple_spinner_dropdown_item);
//        juridictionAddrSpinner.setAdapter(juridictionAdapter);
//
//        juridictionAddrSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                if (position == 2) {
//                    juridictionOtherAddressContainer.setVisibility(View.VISIBLE);
//                    addressType = "Y";
//                } else {
//                    juridictionOtherAddressContainer.setVisibility(View.GONE);
//                    addressType = "N";
//                }
//
//
//            }//onItemSelected closes here.....
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }//onNothingSelected closes ehre.....
//        });//juridictionAddrSpinner.setOnItemSelectedListener coses here.....


            generateEvents();

        }//initializations closes here....

        private void generateEvents() {


            usIndiciaChkBx.setOnCheckedChangeListener(FatcaFragment.this);
            usPersonChkBox.setOnCheckedChangeListener(FatcaFragment.this);
            otherCountryChkBox.setOnCheckedChangeListener(FatcaFragment.this);
            submitButton.setOnClickListener(FatcaFragment.this);
        }//generateEvents() closes here....


        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checkedFlag) {

            switch (compoundButton.getId()) {

                case R.id.fatcaUsIndiciaChkBox:

                    if (checkedFlag) {
                        usIndiciaResponcetext.setText(getResources().getString(R.string.yesIDoSpelling));
                    } else {
                        usIndiciaResponcetext.setText(getResources().getString(R.string.OopsNoIDontSpelling));
                    }
                    break;

                case R.id.fatcaUSPersonChkBox:

                    if (checkedFlag) {
                        usPersonResponcetext.setText(getResources().getString(R.string.yesIDoSpelling));
                        usPersonContainer.setVisibility(View.VISIBLE);
                        juridictionOtherAddressContainer.setVisibility(View.VISIBLE);
//                    juridictionAddressContainer.setVisibility(View.VISIBLE);

                    } else {
                        usPersonResponcetext.setText(getResources().getString(R.string.OopsNoIDontSpelling));
                        usPersonContainer.setVisibility(View.GONE);

                        if(otherCountryChkBox.isChecked())
                            juridictionAddressContainer.setVisibility(View.GONE);
                        else {
                            juridictionOtherAddressContainer.setVisibility(View.GONE);
//                        juridictionAddressContainer.setVisibility(View.GONE);
//                        juridictionOtherAddressContainer.setVisibility(View.GONE);
                        }

                    }
                    break;

                case R.id.fatcaOtherCountryChkBox:

                    if (checkedFlag) {
                        otherCountryResponseText.setText(getResources().getString(R.string.yesIDoSpelling));
                        anyOtherCountryContainer.setVisibility(View.VISIBLE);
                        juridictionOtherAddressContainer.setVisibility(View.VISIBLE);
//                    juridictionAddressContainer.setVisibility(View.VISIBLE);
                    } else {
                        otherCountryResponseText.setText(getResources().getString(R.string.OopsNoIDontSpelling));
                        anyOtherCountryContainer.setVisibility(View.GONE);

                        if(!usPersonChkBox.isChecked()){
                            juridictionAddressContainer.setVisibility(View.GONE);
                            juridictionOtherAddressContainer.setVisibility(View.GONE);
                        }

                    }
                    break;

            }

        }


        @Override
        public void getCountryList(final ArrayList<FatcaVO> fatcaAl) {
            String defaultValue = "INDIA";


            FatcaCountryAdapter fatcaCountryAdapter = new FatcaCountryAdapter(getActivity(), R.layout.spinner_row_layout,
                    fatcaAl);
            countryOfBirth.setAdapter(fatcaCountryAdapter);

            // This code will set the default selection to india
//        for(int i=0; i<fatcaAl.size(); i++) {
//         if(fatcaAl.get(i).getCountryName().equals("INDIA") || fatcaAl.get(i).getCountryName().equals("india"))
//            countryOfBirth.setSelection(i);
//        }


            if (preLoadedDropDownFlag) {

                String country = PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .getString(AppConstants.FATCA_BIRTH_COUNTRY, "");

                for (int i = 0; i < fatcaAl.size(); i++) {
                    if (fatcaAl.get(i).getCountryName().equalsIgnoreCase(country))
                        countryOfBirth.setSelection(i);
                }
            }//if (preLoadedDropDownFlag) closes here....
            else {
                // This code will set the default selection to india
                for (int i = 0; i < fatcaAl.size(); i++) {
                    if (fatcaAl.get(i).getCountryName().equals("INDIA") || fatcaAl.get(i).getCountryName().equals("india"))
                        countryOfBirth.setSelection(i);
                }//for (int i = 0; i < fatcaAl.size(); i++) closes here....
            }//else  closes here....


            countryOfBirth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    countryOfBirthString = fatcaAl.get(i).getCountryCode();

                    if (!fatcaAl.get(i).getCountryName().equalsIgnoreCase("india")) {
                        Log.d(TAG, "country changed");
                        placeOfBirthContainer.setVisibility(View.VISIBLE);
                    } else {
                        placeOfBirthContainer.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    countryOfBirthString = fatcaAl.get(0).getCountryCode();
                }
            });


        }//getCountryList xloses here....

        @Override
        public void getCountryTAXList(final ArrayList<FatcaVO> fatcaAl) {

            //setting adapter
            FatcaCountryAdapter fatcaCountryAdapter = new FatcaCountryAdapter(getActivity(), R.layout.spinner_row_layout,
                    fatcaAl);
            countryOfTaxResi.setAdapter(fatcaCountryAdapter);

            if (preLoadedDropDownFlag) {

                String country = PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .getString(AppConstants.FATCA_TAX_COUNTRY_TWO, "");

                for (int i = 0; i < fatcaAl.size(); i++) {
                    if (fatcaAl.get(i).getCountryName().equalsIgnoreCase(country))
                        countryOfTaxResi.setSelection(i);
                }
            }

            //setting onClick listener
            countryOfTaxResi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    countryOfTaxResidencyString = fatcaAl.get(i).getCountryCode();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    countryOfTaxResidencyString = fatcaAl.get(0).getCountryCode();
                }
            });
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.nxtBtnFATCA:

                    Boolean flag = chkValidations();

                    if (flag) {


                        try {
                            String submitFatcaDetailsURL = AppConstants.URL + AppConstants.FATCA_SUB_URL;
                            String callType = AppConstants.FATCA_CALL_TYPE;
                            String formNumber = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.FORM_NUMBER, null);
                            String rmId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.LOGGED_IN_USER_ID, null);
                            String placeOfBirth = placeOfBirthET.getText().toString().trim();
                            String sessionID = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.SESSION_ID, null);
                            String usIndiciaPerson, usPersonCheck, otherCountryCheck;

                            //TaxableCountry Variables
                            String taxableCountryOne, taxableCountryTwo, taxableCountryOneTin, taxableCountryTwoTin;

                            //US person Check BoX
                            if (usIndiciaChkBx.isChecked())
                                usIndiciaPerson = "Y";
                            else
                                usIndiciaPerson = "N";

                            //Are you a US Person
                            if (usPersonChkBox.isChecked()) {
                                usPersonCheck = "Y";
                                taxableCountryOne = taxableCountryOneET.getText().toString().trim();
                                taxableCountryOneTin = taxableCountryOneTinET.getText().toString().trim();

                            } else {
                                usPersonCheck = "N";
                                taxableCountryOne = "";
                                taxableCountryOneTin = "";
                            }

                            //anyOther country
                            if (otherCountryChkBox.isChecked()) {
                                otherCountryCheck = "Y";
                                taxableCountryTwoTin = taxableCountryTwoTinET.getText().toString().trim();

                            } else {
                                otherCountryCheck = "N";
                                taxableCountryTwoTin = "";
                                countryOfTaxResidencyString = "";
                            }


                            String data = callType + AppConstants.RESPONSE_SPLITTER_STRING
                                    + formNumber + AppConstants.RESPONSE_SPLITTER_STRING
                                    + rmId + AppConstants.RESPONSE_SPLITTER_STRING
                                    + usIndiciaPerson + AppConstants.RESPONSE_SPLITTER_STRING
                                    + countryOfBirthString + AppConstants.RESPONSE_SPLITTER_STRING
                                    + usPersonCheck + AppConstants.RESPONSE_SPLITTER_STRING
                                    + otherCountryCheck + AppConstants.RESPONSE_SPLITTER_STRING
                                    + taxableCountryOne + AppConstants.RESPONSE_SPLITTER_STRING
                                    + taxableCountryOneTin + AppConstants.RESPONSE_SPLITTER_STRING
                                    + countryOfTaxResidencyString + AppConstants.RESPONSE_SPLITTER_STRING
                                    + taxableCountryTwoTin + AppConstants.RESPONSE_SPLITTER_STRING
                                    + placeOfBirth + AppConstants.RESPONSE_SPLITTER_STRING
                                    + sourceOfWealthCodeString + AppConstants.RESPONSE_SPLITTER_STRING
                                    + sessionID + AppConstants.RESPONSE_SPLITTER_STRING;



                            String juridictionData =  addressType + AppConstants.RESPONSE_SPLITTER_STRING
                                    + addrLineOne.getText().toString().trim() + AppConstants.RESPONSE_SPLITTER_STRING
                                    + addrLineTwo.getText().toString().trim() + AppConstants.RESPONSE_SPLITTER_STRING
                                    + addrLineThree.getText().toString().trim() + AppConstants.RESPONSE_SPLITTER_STRING
                                    + city + AppConstants.RESPONSE_SPLITTER_STRING
                                    + country + AppConstants.RESPONSE_SPLITTER_STRING
                                    + state + AppConstants.RESPONSE_SPLITTER_STRING
                                    + pinCode.getText().toString().trim() + AppConstants.RESPONSE_SPLITTER_STRING
                                    + AppConstants.END_OF_URL;


                            if(usPersonChkBox.isChecked() || otherCountryChkBox.isChecked())
                                data = data + juridictionData;
                            else {
                                data = data + addressType + AppConstants.RESPONSE_SPLITTER_STRING

                                        + "" + AppConstants.RESPONSE_SPLITTER_STRING
                                        + "" + AppConstants.RESPONSE_SPLITTER_STRING
                                        + "" + AppConstants.RESPONSE_SPLITTER_STRING
                                        + "" + AppConstants.RESPONSE_SPLITTER_STRING
                                        + "" + AppConstants.RESPONSE_SPLITTER_STRING
                                        + "" + AppConstants.RESPONSE_SPLITTER_STRING
                                        + "" + AppConstants.RESPONSE_SPLITTER_STRING
                                        + AppConstants.END_OF_URL;
                            }

                            /////.......ENCODING THE URL.......\\\\\\\\\
//                        data = URLEncoder.encode(data, "UTF-8");

                            Params_POJO pojo = new Params_POJO();
                            pojo.setData(data);
                            pojo.setUrl(submitFatcaDetailsURL);

                            PostResponseAsync submitCustomerDetailsAsync = new PostResponseAsync(getActivity(), FatcaFragment.this);
                            submitCustomerDetailsAsync.setResponseListener(FatcaFragment.this);
                            submitCustomerDetailsAsync.execute(pojo);
                        } catch (Exception e) {
                            Log.e(TAG, "Exception while parsing DOB : " + e);
                        }//catch closes here.....

                    } else {
                    }

                    break;

                default:

                    break;


            }

        }//onClick closes here...

        private Boolean chkValidations() {

            if (placeOfBirthContainer.getVisibility() == View.VISIBLE && placeOfBirthET.getText().toString().equals("")) {
                placeOfBirthET.setError("Enter place of Birth");
                placeOfBirthET.requestFocus();
                return false;
            } else if (usPersonChkBox.isChecked() && taxableCountryOneTinET.getText().toString().equals("")) {
                taxableCountryOneTinET.setError("Enter TIN");
                taxableCountryOneTinET.requestFocus();
                return false;
            } else if (otherCountryChkBox.isChecked() && taxableCountryTwoTinET.getText().toString().equals("")) {
                taxableCountryTwoTinET.setError("Enter TIN");
                taxableCountryTwoTinET.requestFocus();
                return false;
            }else if(addressType.equalsIgnoreCase("Y")
                    &&(usPersonChkBox.isChecked() || otherCountryChkBox.isChecked())) {
                if(addrLineOne.getText().toString().equals("")){
                    addrLineOne.setError("Enter Address");
                    return false;
                }
//            else if(addrLineTwo.getText().toString().equals("")){
//                addrLineTwo.setError("Enter Address");
//                return false;
//            }else if(addrLineThree.getText().toString().equals("")){
//                addrLineThree.setError("Enter Address");
//                return false;
//            }
                else if(pinCode.getText().toString().equals("")){
                    pinCode.setError("Enter Pin Code");
                    return false;
                }else if(country.equals("Select Country")){
//                Toast.makeText(getActivity(),"Select Country",Toast.LENGTH_SHORT).show();
                    showSnackBar(getActivity(),getActivity().findViewById(R.id.drawer_layout) , "Select Country" , Snackbar.LENGTH_SHORT);

                    return false;
                }else if(state.equals("Select State")){
//                Toast.makeText(getActivity(),"Select State",Toast.LENGTH_SHORT).show();
                    showSnackBar(getActivity(),getActivity().findViewById(R.id.drawer_layout) , "Select State" , Snackbar.LENGTH_SHORT);

                    return false;
                }else if(city.equals("Select City")){
//                Toast.makeText(getActivity(),"Select City",Toast.LENGTH_SHORT).show();
                    showSnackBar(getActivity(),getActivity().findViewById(R.id.drawer_layout) , "Select City" , Snackbar.LENGTH_SHORT);

                    return false;
                }else
                    return true;
            }
            else {
                return true;
            }
        }

        @Override
        public void notifyNetworkResponseSuccess(String response) {
            if (response.trim().startsWith(AppConstants.SUCCESS_STRING_PREFIX)) {
                Navigation.findNavController(view).navigate(R.id.action_nav_fatca_to_nav_summary);
            }//if(response.trim().startsWith(AppConstants.SUCCESS_STRING_PREFIX)) closes here....
            else if (response.trim().startsWith(AppConstants.ERROR_STRING_PREFIX)) {
                //-1|$M~Invalid PAN format|^|$

                String errorMsgWithGarbage = response.split(Pattern.quote(AppConstants.END_OF_URL))[1];//M~Invalid PAN format|^

                    String errorMsg = errorMsgWithGarbage.substring(errorMsgWithGarbage.indexOf(AppConstants.TILDE_SYMBOL) + 1, errorMsgWithGarbage.length());//Invalid PAN format|^
                    if (errorMsg.contains(AppConstants.RESPONSE_SPLITTER_STRING))
                        errorMsg = errorMsg.replace(AppConstants.RESPONSE_SPLITTER_STRING, "");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(errorMsg);
                    builder.setTitle(getResources().getString(R.string.app_name));

                    builder.setPositiveButton(getResources().getString(R.string.ok_spelling), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                        }//onClick closes here....
                    });//new DialogInterface.OnClickListener() closes here.....
                    builder.create();
                    builder.show();
            }//else if(response.trim().startsWith(AppConstants.ERROR_STRING_PREFIX)) closes here.....
            else
                Log.w(TAG, "Unhandled case : " + response);
        }


        @Override
        public void notifyNetworkResponseFailure(Exception exception, String response) {
//        Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
            showSnackBar(getActivity(),getActivity().findViewById(R.id.drawer_layout) , "Network Error" , Snackbar.LENGTH_SHORT);

        }

        @Override
        public void getOtherCountryList(final ArrayList<FatcaVO> fatcaAl) {

            //setting adapter
            FatcaCountryAdapter fatcaCountryAdapter = new FatcaCountryAdapter(getActivity(), R.layout.spinner_row_layout,
                    fatcaAl);
            fatcaJuridictionCountrySpinner.setAdapter(fatcaCountryAdapter);


            if (preLoadedDropDownFlag) {

                String country = PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .getString(AppConstants.FATCA_COUNTRY, "");

                for (int i = 0; i < fatcaAl.size(); i++) {
                    if (fatcaAl.get(i).getCountryName().equalsIgnoreCase(country))
                        fatcaJuridictionCountrySpinner.setSelection(i);
                }
            }//if (preLoadedDropDownFlag) closes here....



            fatcaJuridictionCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    country = fatcaAl.get(position).getCountryName();

                    StateTask fatcaCountryTask = new StateTask(getActivity(), FatcaFragment.this);
                    fatcaCountryTask.setDetailsListener(FatcaFragment.this);
                    fatcaCountryTask.getDetails(fatcaAl.get(position).getCountryName());
                }//onItemSelected closes here....

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }//getOtherCountryList closes here.........

        @Override
        public void getOtherStateList(final ArrayList<FatcaVO> fatcaAl) {
            //setting adapter
            FatcaCountryAdapter fatcaCountryAdapter = new FatcaCountryAdapter(getActivity(), R.layout.spinner_row_layout,
                    fatcaAl);
            fatcaJuridictionStateSpinner.setAdapter(fatcaCountryAdapter);


            if (preLoadedDropDownFlag) {

                String state = PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .getString(AppConstants.FATCA_STATE, "");

                for (int i = 0; i < fatcaAl.size(); i++) {
                    if (fatcaAl.get(i).getCountryName().equalsIgnoreCase(state))
                        fatcaJuridictionStateSpinner.setSelection(i);
                }
            }//if (preLoadedDropDownFlag) closes here....


            fatcaJuridictionStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    state = fatcaAl.get(position).getCountryName();
                    CityTask fatcaCountryTask = new CityTask(getActivity(), FatcaFragment.this);
                    fatcaCountryTask.setDetailsListener(FatcaFragment.this);
                    fatcaCountryTask.getDetails(fatcaAl.get(position).getCountryName());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });




        }//getOtherStateList closes here....

        @Override
        public void getOtherCityList(final ArrayList<FatcaVO> fatcaAl) {
            FatcaCountryAdapter fatcaCountryAdapter = new FatcaCountryAdapter(getActivity(), R.layout.spinner_row_layout,
                    fatcaAl);
            fatcaJuridictionCitySpinner.setAdapter(fatcaCountryAdapter);

            if (preLoadedDropDownFlag) {

                String city = PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .getString(AppConstants.FATCA_CITY, "");

                for (int i = 0; i < fatcaAl.size(); i++) {
                    if (fatcaAl.get(i).getCountryName().equalsIgnoreCase(city))
                        fatcaJuridictionCitySpinner.setSelection(i);
                }
            }//if (preLoadedDropDownFlag) closes here....



            fatcaJuridictionCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    city = fatcaAl.get(position).getCountryName();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

    }//FatcaFragment closes here....


