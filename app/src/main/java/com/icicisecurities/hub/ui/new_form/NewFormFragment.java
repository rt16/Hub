package com.icicisecurities.hub.ui.new_form;

import android.app.DatePickerDialog;
import android.app.IntentService;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
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
import com.icicisecurities.hub.ui.new_form.model.GetProductsListener;
import com.icicisecurities.hub.ui.new_form.model.ProductVO;
import com.icicisecurities.hub.ui.new_form.model.RelationVO;
import com.icicisecurities.hub.ui.new_form.model.PersonalOneSpinnersDataListner;
import com.icicisecurities.hub.ui.summary.SummaryFragment;
import com.icicisecurities.hub.utils.AppConstants;
import com.icicisecurities.hub.utils.AppUtils;
import com.icicisecurities.hub.utils.SessionLogoutDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.icicisecurities.hub.utils.AppUtils.convertProperDateFormat;
import static com.icicisecurities.hub.utils.AppUtils.showSnackBar;


public class NewFormFragment extends Fragment implements NetworkResponseListener, AdapterView.OnItemSelectedListener, PersonalOneSpinnersDataListner, CompoundButton.OnCheckedChangeListener, TextWatcher, View.OnFocusChangeListener, GetProductsListener, RadioGroup.OnCheckedChangeListener {

    private NewFormViewModel newFormViewModel;
    @BindView(R.id.submitBtn)
    MaterialButton submitBtn;
    @BindView(R.id.panTIL)
    TextInputLayout panTIL;
    @BindView(R.id.dobDayTIL)
    TextInputLayout dobDayTIL;
    @BindView(R.id.dobMonTIL)
    TextInputLayout dobMonTIL;
    @BindView(R.id.dobYearTIL)
    TextInputLayout dobYearTIL;
    @BindView(R.id.fNameTIL)
    TextInputLayout fNameTIL;
    @BindView(R.id.mNameTIL)
    TextInputLayout mNameTIL;
    @BindView(R.id.lNameTIL)
    TextInputLayout lNameTIL;
    @BindView(R.id.custTypeTIL)
    TextView custTypeTIL;
    @BindView(R.id.productTypeTIL)
    TextView productTypeTIL;
//    @BindView(R.id.arnNoTIL)
//    TextInputLayout arnNoTIL;
    @BindView(R.id.emailTIL)
    TextInputLayout emailTIL;
    @BindView(R.id.emailRelationTIL)
    TextView emailRelationTIL;
    @BindView(R.id.mobileTIL)
    TextInputLayout mobileTIL;
    @BindView(R.id.mobileRelationTIL)
    TextView mobileRelationTIL;

    @BindView(R.id.panET)
    EditText panTET;
    @BindView(R.id.dobDayTET)
    TextInputEditText dobDayTET;
    @BindView(R.id.dobMonTET)
    TextInputEditText dobMonTET;
    @BindView(R.id.dobYearTET)
    TextInputEditText dobYearTET;
    @BindView(R.id.fNameTET)
    TextInputEditText fNameTET;
    @BindView(R.id.mNameTET)
    TextInputEditText mNameTET;
    @BindView(R.id.lNameTET)
    TextInputEditText lNameTET;
    @BindView(R.id.custTypeSpinner)
    AppCompatSpinner custTypeTET;
    @BindView(R.id.productTypeSpinner)
    AppCompatSpinner productTypeTET;
//    @BindView(R.id.arnNoTET)
//    TextInputEditText arnNoTET;
    @BindView(R.id.emailTET)
    TextInputEditText emailTET;
    @BindView(R.id.emailRelationSpinner)
    AppCompatSpinner emailRelationTET;
    @BindView(R.id.mobileTET)
    TextInputEditText mobileTET;
    @BindView(R.id.mobileRelationSpinner)
    AppCompatSpinner mobileRelationTET;
    @BindView(R.id.formTET)
    TextInputEditText formNumberTET;
    @BindView(R.id.formTIL)
    TextInputLayout formNumberTIL;
    @BindView(R.id.formScroll)
    ScrollView scrollView;
    @BindView(R.id.newFormLayout)
    LinearLayout formLayout;
    @BindView(R.id.emailCheckBox)
    AppCompatCheckBox emailChkBox;
    @BindView(R.id.personal_detail_email_relation_container)
    LinearLayout emailRelationContainer;
    @BindView(R.id.mobileContainer)
    LinearLayout mobileContainer;
    String dob;
    @BindView((R.id.agentCodeTIL))
    TextInputLayout agentCodeTIL;
    @BindView((R.id.agentCodeTET))
    TextInputEditText agentCodeTET;
    private FOCUSED_DATE_EDITTEXT_ENUM focusedEdtTxt;

    @BindView(R.id.radioGender)
            RadioGroup radioGroup;
            RadioButton radioButton;
    private ArrayList<RelationVO> relationList=new ArrayList<RelationVO>();


    private enum FOCUSED_DATE_EDITTEXT_ENUM {DATE_IS_FOCUSED, MONTH_IS_FOCUSED, YEAR_IS_FOCUSED}


    final Calendar myCalendar = Calendar.getInstance();
    private String emailRelationSelected, mobileRElationSelected, customerTypeSelected, productTypeSelected, dobMonSelected, genderSelected;
    private View root;
    private String TAG = getClass().getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newFormViewModel =
                ViewModelProviders.of(this).get(NewFormViewModel.class);
        root = inflater.inflate(R.layout.fragment_new_form, container, false);

        ButterKnife.bind(this,root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailRelationTET.setOnItemSelectedListener(NewFormFragment.this);
        mobileRelationTET.setOnItemSelectedListener(NewFormFragment.this);
        custTypeTET.setOnItemSelectedListener(NewFormFragment.this);
        productTypeTET.setOnItemSelectedListener(NewFormFragment.this);
        emailChkBox.setOnCheckedChangeListener(NewFormFragment.this);
        dobDayTET.addTextChangedListener(NewFormFragment.this);
        dobMonTET.addTextChangedListener(NewFormFragment.this);
        dobYearTET.addTextChangedListener(NewFormFragment.this);
        dobDayTET.setOnFocusChangeListener(NewFormFragment.this);
        dobMonTET.setOnFocusChangeListener(NewFormFragment.this);
        dobYearTET.setOnFocusChangeListener(NewFormFragment.this);
        RelationTask relationTask = new RelationTask(getActivity(), NewFormFragment.this);
        relationTask.setDetailsListener(NewFormFragment.this);
        relationTask.getDetails();
//        mobileRelationTET.


        int selectedID = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) root.findViewById(selectedID);
        radioGroup.setOnCheckedChangeListener(NewFormFragment.this);
        genderSelected = "";

//        http://203.189.92.210/ria/Customer/RI_AccOpenHubActions.aspx?requeststring=GETPRODUCTTYPE|^IND|^PRD|^C711095|^0

//        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                // TODO Auto-generated method stub
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                updateLabel();
//            }
//
//        };

//        dobDayTET.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                new DatePickerDialog(getContext(), date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//input: HUB_INSERT|^Session ID|^Form No|^PAN No|^DOB|^First Name|^Middle Name|^Last Name|^Customer Type|^ARN No|^Email ID|^Email Relation|^Mobile No|^Mobile Relation|^Employee No|^|$
                String date = dobDayTET.getText().toString().trim();
                String month = dobMonTET.getText().toString().trim();

                if (!dobDayTET.getText().toString().trim().isEmpty()){
                    if (Integer.parseInt(date) < 9){
                        date = "0" + date;
                    }
                }else{
                    dobDayTIL.setError("DOB cannot be empty");
                }

                if (!dobMonTET.getText().toString().trim().isEmpty()){
                    if (Integer.parseInt(month) < 9){
                        month = "0" + month;
                    }
                }else{
                    dobMonTIL.setError("DOB cannot be empty");
                }
                dob = date + "-" + month + "-" + dobYearTET.getText().toString().trim();

                try {
                    dob = convertProperDateFormat(dob);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (valid()) {

                    Params_POJO params_pojo = new Params_POJO();
                    params_pojo.setUrl(AppConstants.URL + AppConstants.SUBMIT_NEW_FORM_URL);
                    String emailRelationS = "";

                    if(emailChkBox.isChecked()){
                        emailRelationS = "";
                    }else{
                        emailRelationS = emailRelationSelected;
                    }
                    String data = AppConstants.SUBMIT_NEW_FORM_CALL_TYPE +
                            AppConstants.RESPONSE_SPLITTER_STRING + PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.SESSION_ID, "") +
                            AppConstants.RESPONSE_SPLITTER_STRING + formNumberTET.getText().toString().trim() +
                            AppConstants.RESPONSE_SPLITTER_STRING + panTET.getText().toString().trim() +
                            AppConstants.RESPONSE_SPLITTER_STRING + dob + //dobDayTET.getText().toString().trim() +
                            AppConstants.RESPONSE_SPLITTER_STRING + fNameTET.getText().toString().trim() +
                            AppConstants.RESPONSE_SPLITTER_STRING + mNameTET.getText().toString().trim() +
                            AppConstants.RESPONSE_SPLITTER_STRING + lNameTET.getText().toString().trim() +
                            AppConstants.RESPONSE_SPLITTER_STRING + customerTypeSelected +
                            AppConstants.RESPONSE_SPLITTER_STRING + "" +
                            AppConstants.RESPONSE_SPLITTER_STRING + emailTET.getText().toString().trim() +
                            AppConstants.RESPONSE_SPLITTER_STRING + emailRelationS +
                            AppConstants.RESPONSE_SPLITTER_STRING + mobileTET.getText().toString().trim() +
                            AppConstants.RESPONSE_SPLITTER_STRING + mobileRElationSelected +
                            AppConstants.RESPONSE_SPLITTER_STRING + PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.LOGGED_IN_USER_ID, "") +
                            AppConstants.RESPONSE_SPLITTER_STRING + productTypeSelected +
                            AppConstants.RESPONSE_SPLITTER_STRING + agentCodeTET.getText().toString().trim() +
                            AppConstants.RESPONSE_SPLITTER_STRING + genderSelected.trim() +
                            AppConstants.RESPONSE_SPLITTER_STRING + AppConstants.END_OF_URL;
                    Log.e("data",data);
                    params_pojo.setData(data);
                    PostResponseAsync postResponseAsync = new PostResponseAsync(getActivity(), NewFormFragment.this);
                    postResponseAsync.setResponseListener(NewFormFragment.this);
                    postResponseAsync.execute(params_pojo);
                } else {
                    Log.d(TAG, "Not valid");
                }
            }
        });
    }

    private Boolean valid() {

        if(formNumberTIL.isErrorEnabled()){
            formNumberTIL.setErrorEnabled(false);
        }else if(panTIL.isErrorEnabled()){
            panTIL.setErrorEnabled(false);
        }else if(dobDayTIL.isErrorEnabled()){
            dobDayTIL.setErrorEnabled(false);
        } else if(dobMonTIL.isErrorEnabled()){
            dobMonTIL.setErrorEnabled(false);
        }else if(dobYearTIL.isErrorEnabled()){
            dobYearTIL.setErrorEnabled(false);
        }else if(fNameTIL.isErrorEnabled()){
            fNameTIL.setErrorEnabled(false);
        }else if(lNameTIL.isErrorEnabled()){
            lNameTIL.setErrorEnabled(false);
        }
//        else if(arnNoTIL.isErrorEnabled()){
//            arnNoTIL.setErrorEnabled(false);
//        }
        else if(agentCodeTIL.isErrorEnabled()){
            agentCodeTIL.setErrorEnabled(false);
        }else if(emailTIL.isErrorEnabled()){
            emailTIL.setErrorEnabled(false);
        }else if(mobileTIL.isErrorEnabled()){
            mobileTIL.setErrorEnabled(false);
        }

        if(formNumberTET.getText().toString().trim().isEmpty()){
            formNumberTIL.setError("Please enter Form number");
            return false;
        } else if(panTET.getText().toString().trim().isEmpty()){
            panTIL.setError("Please enter PAN number");
            return false;
        } else if(!panTET.getText().toString().trim().isEmpty() && !AppUtils.isPANValid(panTET.getText().toString().trim())){
            panTIL.setError("Please enter a valid PAN number");
            return false;
        } else if(dobDayTET.getText().toString().trim().isEmpty()){
            dobDayTIL.setError("Please enter DOB");
            return false;
        } else if (dobMonTET.getText().toString().trim().equals("") || dobMonTET.getText().toString().trim().isEmpty()) {
            dobMonTIL.setError(getResources().getString(R.string.DOBEmptyError));
            return false;
        }//Year is DOB is empty closes here.....
        else if (dobYearTET.getText().toString().trim().equals("") || dobYearTET.getText().toString().trim().isEmpty()) {
            dobYearTIL.setError(getResources().getString(R.string.DOBEmptyError));
            return false;
        }//Year is DOB is empty closes here.....
         else if (Integer.parseInt(dobDayTET.getText().toString()) > 31 || Integer.parseInt(dobDayTET.getText().toString()) <= 0)  {
            //Date in DOB is not MM.....
            dobDayTIL.setError(getResources().getString(R.string.invalidDateFormatError));
            return false;
        } else if (Integer.parseInt(dobMonTET.getText().toString()) > 12 || Integer.parseInt(dobMonTET.getText().toString()) <= 0)  {
            //Date in DOB is not MM.....
            dobMonTIL.setError(getResources().getString(R.string.invalidDateFormatError));
            return false;
        } else if (dobYearTET.getText().toString().trim().length() < 4) {
            //Month in DOB is not YY.....
            dobYearTIL.setError(getResources().getString(R.string.invalidDateFormatError));
            return false;
        } else if (!dobDayTET.getText().toString().trim().isEmpty() && !dobMonTET.getText().toString().trim().isEmpty() && !dobYearTET.getText().toString().trim().isEmpty() && AppUtils.chkWhetherUserIsMinorOrNot(dob)) {
//            dobDayTIL.setError(getResources().getString(R.string.minorAccountOpeningIssueError));
            AppUtils.showSnackBar(getActivity(),scrollView,getString(R.string.minorAccountOpeningIssueError), Snackbar.LENGTH_SHORT);
//            AppUtils.showAlertDialog(getActivity(), NewFormFragment.this, getResources().getString(R.string.minorAccountOpeningIssueError));//
            return false;
        }else if (genderSelected.trim().isEmpty()){
            AppUtils.showSnackBar(getActivity(),scrollView,"Please Select Gender", Snackbar.LENGTH_SHORT);
//            radioButton.setError("Please Select Gender");
            return false;
        } else if(fNameTET.getText().toString().trim().isEmpty()){
            fNameTIL.setError("Please enter First name");
            return false;
        } else if(lNameTET.getText().toString().trim().isEmpty()){
            lNameTIL.setError("Please enter Last name");
            return false;
        } else if(agentCodeTET.getText().toString().trim().isEmpty()){
            agentCodeTIL.setError("Please enter Agent code");
            return false;
        } else if(emailTET.getText().toString().trim().isEmpty()){
            emailTIL.setError("Please enter email id");
            return false;
        } else if(!emailTET.getText().toString().trim().isEmpty() && !AppUtils.isEmailValid(emailTET.getText().toString().trim())) {
                emailTIL.setError("Please enter a valid email id");
                return false;

        } else if(!customerTypeSelected.equalsIgnoreCase("NRI") && mobileTET.getText().toString().trim().isEmpty()){
            mobileTIL.setError("Please enter Mobile number");
            return false;
        } else if(!customerTypeSelected.equalsIgnoreCase("NRI") && !mobileTET.getText().toString().trim().isEmpty() && mobileTET.getText().toString().trim().length() < 10){
            mobileTIL.setError("Please enter a valid Mobile number");
            return false;
        } else if (productTypeSelected.isEmpty()){
             return false;
        } else{
            return true;
        }

    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.radioMale:
                genderSelected = "M";
                break;

            case R.id.radioFemale:
                genderSelected = "F";
                break;
        }
    }

    private void updateLabel() {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dobDayTET.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void notifyNetworkResponseSuccess(String response) {

        if(response.startsWith(AppConstants.SUCCESS_STRING_PREFIX)) {

            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(AppConstants.FORM_NUMBER, formNumberTET.getText().toString().trim()).apply();
            Navigation.findNavController(root).navigate(R.id.action_nav_new_to_nav_fatca);

        }else if(response.startsWith(AppConstants.ERROR_STRING_PREFIX)){

            //Else if response starts with -1 then below logic is executed....

            String errorMsg = "";

            //Splitting the String w.r.t the |$....
            String[] splittedMsg = response.split(Pattern.quote(AppConstants.END_OF_URL));
            if (splittedMsg != null) {
                errorMsg = splittedMsg[1];
                if (errorMsg.contains(AppConstants.SESSION_ERROR_FLAG)) {
//            Response = -1|$SSSN|^Your current session is no longer valid. Please re-login|^|$

                    new SessionLogoutDialog(getActivity(), NewFormFragment.this, errorMsg.split(Pattern.quote(AppConstants.RESPONSE_SPLITTER_STRING))[1].replace("|^", ""));
//                    AppUtils.deleteFolderRecursively(getActivity(), SummaryFragment.this, AppUtils.getRootDirectory(getActivity(), SummaryFragment.this));
                } else {
                    if (errorMsg.contains(AppConstants.RESPONSE_SPLITTER_STRING))
                        errorMsg = errorMsg.replace(AppConstants.RESPONSE_SPLITTER_STRING, "");
                    AppUtils.showAlertDialog(getActivity(), NewFormFragment.this, errorMsg);
                }
            }//if(splittedMsg != null) closes here....
            else
                Log.w(TAG, "splittedMsg is null...");

        }//else if response starts with -1 closes here....
        else
            Log.w(TAG, "Unhandled case : " + response);

    }



    @Override
    public void notifyNetworkResponseFailure(Exception exception, String response) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()){
            case R.id.custTypeSpinner:
                customerTypeSelected = getResources().getStringArray(R.array.customerTypeArrayItems)[i];
                ProductTask productTask = new ProductTask(getActivity(), NewFormFragment.this);
                productTask.setProductDetailsListener(NewFormFragment.this);
                productTask.getProductTypeDetails(customerTypeSelected);
                Log.e("Type :",customerTypeSelected);

                if(i==1){
                    //NRI is selected
                    mobileContainer.setVisibility(View.GONE);
                    mobileRElationSelected = "";
                    mobileTET.setText("");
//                    if customer type is  NRI then email must be disabled and only SELF must be allowed.
                    RelationAdapter relationAdapter = new RelationAdapter(getActivity(), R.layout.spinner_row_layout,
                            relationList);
                    emailRelationTET.setAdapter(relationAdapter);
                        emailRelationSelected = relationList.get(0).getEntityCode();
                    emailRelationTET.setEnabled(false);


                }else{
                    //IND is selected
                    mobileContainer.setVisibility(View.VISIBLE);
                    emailRelationTET.setEnabled(true);

                }
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        switch (adapterView.getId()){
            case R.id.custTypeSpinner:
                customerTypeSelected = getResources().getStringArray(R.array.customerTypeArrayItems)[0];
                break;
        }

    }

    @Override
    public void getIncomeCategory(ArrayList<RelationVO> incomeAL) {

    }

    @Override
    public void getOccupationCategoty(ArrayList<RelationVO> occupationAL) {

    }

    @Override
    public void getRelation(ArrayList<RelationVO> relationAL) {
        RelationAdapter relationAdapter = new RelationAdapter(getActivity(), R.layout.spinner_row_layout,
                relationAL);
        emailRelationTET.setAdapter(relationAdapter);
        mobileRelationTET.setAdapter(relationAdapter);
        mobileRElationSelected = relationAL.get(0).getEntityCode();
        emailRelationSelected = relationAL.get(0).getEntityCode();
        mobileRelationTET.setEnabled(false);
        relationList.addAll(relationAL);




        emailRelationTET.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                emailRelationSelected = relationAL.get(i).getEntityCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                emailRelationSelected = relationAL.get(0).getEntityCode();
            }
        });

        mobileRelationTET.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mobileRElationSelected = relationAL.get(0).getEntityCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mobileRElationSelected = relationAL.get(0).getEntityCode();
            }
        });

    }


    @Override
    public void getProducts(ArrayList<ProductVO> products) {
        Log.d(TAG, "getProducts : " + products);

        ProductAdapter productAdapter = new ProductAdapter(getActivity(), R.layout.spinner_row_layout,
                products);
        productTypeTET.setAdapter(productAdapter);

        productTypeTET.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                productTypeSelected = products.get(i).getProductType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                productTypeSelected = products.get(0).getProductType();
            }
        });
    }

    @Override
    public void getAddressType(ArrayList<RelationVO> addressTypeAL) {

    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

        switch (compoundButton.getId()) {
            case R.id.emailCheckBox:

                if (checked) {
                    emailTET.setText(getString(R.string.email_not_provided));
                    emailTET.setFocusable(false);
                    emailRelationContainer.setVisibility(View.GONE);
//                    emailRelationSelected = "";     // need to be discuss note
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setMessage(getActivity().getResources().getString(R.string.email_alert));
                    builder.setTitle(getResources().getString(R.string.app_name));
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                } else {
                    emailTET.setText("");
                    emailTET.setFocusableInTouchMode(true);
                    emailRelationContainer.setVisibility(View.VISIBLE);
//                    emailRelationSelected = emailRelationSelected;
                }
                break;

            default:

                break;
        }//switch closes here...

    }//onCheckedChanged closes here...

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (focusedEdtTxt != null) {
            switch (focusedEdtTxt) {
                case DATE_IS_FOCUSED:
//			Log.d(TAG, "Date Text , CharSequence == "+s+", length == "+s.length()+", character == "+count);
                    if (charSequence.length() == 2 || charSequence.length() > 2)
                        dobMonTET.requestFocus();
                    break;

                case MONTH_IS_FOCUSED:
//			Log.d(TAG, "Month Text , CharSequence == "+s+", length == "+s.length()+", character == "+count);
                    if (charSequence.length() == 2 || charSequence.length() > 2)
                        dobYearTET.requestFocus();
                    break;

                case YEAR_IS_FOCUSED:
//			Log.d(TAG, "Year Text , CharSequence == "+s+", length == "+s.length()+", character == "+count);
/*			if(s.length() == 4 || s.length() > 4)
				mMobileNumbrEdtTxt.requestFocus();*/
                    //Do nothing when Year is focused.
                    break;

                default:
                    Log.w(TAG, "Unhandled onTextChange:");
                    break;
            }//switch (focusedEdtTxt) closes here.....
        }//if closes here...
        else {
            Log.d(TAG, "FocusedEditText is null");
        }//else closes here....
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {

            //Date EditText is focused....
            case R.id.dobDayTET:
                if (hasFocus)
                    focusedEdtTxt = FOCUSED_DATE_EDITTEXT_ENUM.DATE_IS_FOCUSED;
                break;

            //Month EditText is focused....
            case R.id.dobMonTET:
                if (hasFocus) {
                    focusedEdtTxt = FOCUSED_DATE_EDITTEXT_ENUM.MONTH_IS_FOCUSED;
//                    String date = dobDayTET.getText().toString().trim();
//
//                    if(date.length() < 2 && Integer.parseInt(date) < 9){
//                        date = "0" + date;
//                        dobDayTET.setText(date);
//                    }
                }
                break;

            //Year EditText is focused.....
            case R.id.dobYearTET:
                if (hasFocus)
                    focusedEdtTxt = FOCUSED_DATE_EDITTEXT_ENUM.YEAR_IS_FOCUSED;
//                String month = dobMonTET.getText().toString().trim();
//
//                if(month.length() < 2 && Integer.parseInt(month) < 9){
//                    month = "0" + month;
//                    dobMonTET.setText(month);
//                }
                break;

            default:
                Log.w(TAG, "Unhandled case is focussed: " + view);
                break;
        }//switch (v.getId()) closes here......
    }
}