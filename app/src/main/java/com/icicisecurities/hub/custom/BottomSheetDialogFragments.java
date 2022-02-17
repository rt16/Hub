package com.icicisecurities.hub.custom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.icicisecurities.hub.R;
import com.icicisecurities.hub.utils.AppUtils;
import com.icicisecurities.hub.utils.FilterLeadsListener;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.icicisecurities.hub.utils.SELECT_RANGE_FOR_OPEN_CLOSED_CASES.FROM_DATE;
import static com.icicisecurities.hub.utils.SELECT_RANGE_FOR_OPEN_CLOSED_CASES.TO_DATE;

public class BottomSheetDialogFragments extends BottomSheetDialogFragment implements View.OnClickListener {

    private static final String TAG = "BottomSheetDialogFr";
    BottomSheetBehavior sheetBehavior;
    BottomSheetDialog bottomSheetDialog;
    @BindView(R.id.fromDateTET)
    TextInputEditText fromDateTET;
    @BindView(R.id.toDateTET)
    TextInputEditText toDateTET;
    @BindView(R.id.fromDateTIL)
    TextInputLayout fromDateTIL;
    @BindView(R.id.toDateTIL)
    TextInputLayout toDateTIL;
    @BindView(R.id.btnProceed)
    ImageButton imageButtonProceed;
    @BindView(R.id.searchTIL)
    TextInputLayout searchTIL;
    @BindView(R.id.searchTET)
    TextInputEditText searchTET;

    //Medium priority NON-UI variables goes below....
    private static Context context;//Bcoz this is used to open & close SharedPrefernece by 2 static methods...
    private Object instanceOfClass;
    private FragmentTransaction fragmentTransaction;
    private String DATE_PICKER_FRAGMENT_TAG = "DATE_PICKER_FRAGMENT_TAG";
    private FilterLeadsListener mFilterLeadsListener;
    private static String currentDate;//This is static bcoz we return the Date to the Calling class....
    private final static String LEADS_FILTER_FROM_DATE = "LEADS_FILTER_FROM_DATE";
    private final static String LEADS_FILTER_TO_DATE = "LEADS_FILTER_TO_DATE";
    private static String search = "%";
    public BottomSheetDialogFragments(Context context, Object instanceOfClass) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.instanceOfClass = instanceOfClass;
    }//FilterDialog constructor closes here....

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, R.style.AppModalStyle);

        //Set the custom view
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet, null);
        dialog.setContentView(view);
        ButterKnife.bind(this,view);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        fromDateTET = view.findViewById(R.id.fromDateTET);
        fromDateTIL = view.findViewById(R.id.fromDateTIL);
        toDateTET = view.findViewById(R.id.toDateTET);
        toDateTIL = view.findViewById(R.id.toDateTIL);
        searchTIL = view.findViewById(R.id.searchTIL);
        searchTET = view.findViewById(R.id.searchTET);
        imageButtonProceed = view.findViewById(R.id.btnProceed);
        imageButtonProceed.setOnClickListener(this);
        fromDateTET.setOnClickListener(this);
        toDateTET.setOnClickListener(this);

        fromDateTET.setText(BottomSheetDialogFragments.getLeadsFilter_FromDate());
        toDateTET.setText(BottomSheetDialogFragments.getLeadsFilter_ToDate());
        //Setting data on the UI...
        //Whenever this Fragment is inflated, the Current Date shud be setted on the Buttons....

        if (behavior != null && behavior instanceof BottomSheetBehavior) {

            ((BottomSheetBehavior) behavior).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    String state = "";

                    switch (newState) {
                        case BottomSheetBehavior.STATE_DRAGGING: {
                            state = "DRAGGING";
                            break;
                        }
                        case BottomSheetBehavior.STATE_SETTLING: {
                            state = "SETTLING";
                            break;
                        }
                        case BottomSheetBehavior.STATE_EXPANDED: {
                            state = "EXPANDED";
                            break;
                        }
                        case BottomSheetBehavior.STATE_COLLAPSED: {
                            state = "COLLAPSED";
                            break;
                        }
                        case BottomSheetBehavior.STATE_HIDDEN: {
                            dismiss();
                            state = "HIDDEN";
                            break;
                        }
                    }

//                    Toast.makeText(getActivity(), "Bottom Sheet State Changed to: " + state, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        }
    }

    public void showBottomSheet(){

//        bottomSheetDialog = new BottomSheetDialog(context);
//        View layoutView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
//        bottomSheetDialog.setContentView(layoutView);
//        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View)layoutView.getParent());
//        bottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
//        Button proceedBtn = layoutView.findViewById(R.id.btn_proceed);
//        proceedBtn.setOnClickListener(this);

//        fromDateTET = layoutView.findViewById(R.id.fromDateTET);
//        fromDateTIL = layoutView.findViewById(R.id.fromDateTIL);
//        toDateTET = layoutView.findViewById(R.id.toDateTET);
//        toDateTIL = layoutView.findViewById(R.id.toDateTIL);
//        imageButtonProceed = layoutView.findViewById(R.id.btnProceed);
//        imageButtonProceed.setOnClickListener(this);
//        fromDateTET.setOnClickListener(this);
//        toDateTET.setOnClickListener(this);

//        dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
//        dialog.setContentView(R.layout.dialog_filter_dates);
////		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        dialog.setCancelable(true);



        //Initializing the Dialog's Widgets....
//        mSelectFromDateBtn = (Button) dialog.findViewById(R.id.selectFromDateBtn);
//        mSelectToDateBtn = (Button) dialog.findViewById(R.id.selectToDateBtn);
//        mOKBtn = (Button) dialog.findViewById(R.id.okBtn);

        //Generating Events...
//        mSelectFromDateBtn.setOnClickListener(FilterDialog.this);
//        mSelectToDateBtn.setOnClickListener(FilterDialog.this);
//        mOKBtn.setOnClickListener(FilterDialog.this);

        //Setting data on the UI...
        //Whenever this Fragment is inflated, the Current Date shud be setted on the Buttons....
//        mSelectFromDateBtn.setText(FilterDialog.getLeadsFilter_FromDate());
//        mSelectToDateBtn.setText(FilterDialog.getLeadsFilter_ToDate());

//        fromDateTET.setText(BottomSheetDialogFragments.getLeadsFilter_FromDate());
//        toDateTET.setText(BottomSheetDialogFragments.getLeadsFilter_ToDate());

        bottomSheetDialog.show();
    }//showDialog closes here....

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnProceed:
                String fromDate = fromDateTET.getText().toString().trim();
                String toDate = toDateTET.getText().toString().trim();
                search = searchTET.getText().toString().trim();
                mFilterLeadsListener.filterLeadsListener(true);
                this.dismiss();
                break;
            case R.id.fromDateTET:

                DialogFragment fromDatePickerFragment = new DatePickerDialogFragment(getActivity(), fromDateTET, FROM_DATE);
                fromDatePickerFragment.show(((Activity) getActivity()).getFragmentManager(), DATE_PICKER_FRAGMENT_TAG);
                break;

            case R.id.toDateTET:
                DialogFragment toDatePickerFragment = new DatePickerDialogFragment(getActivity(), toDateTET, TO_DATE);
                toDatePickerFragment.show(((Activity)getActivity()).getFragmentManager(), DATE_PICKER_FRAGMENT_TAG);
                break;
        }

    }

    public static String getLeadsFilter_FromDate(){
//		PreferenceManager.getDefaultSharedPreferences(context).
        if(context == null){
            //User has not edited the From & To date till now....
            Calendar calendar = AppUtils.getCalendarInstance(context);
            currentDate = AppUtils.formatDate(context, null, calendar);
//			Log.d(TAG, "Returning Current Date = "+currentDate);
            return currentDate;
        }//if(context == null) closes here....
        else{
            if(PreferenceManager.getDefaultSharedPreferences(context).contains(LEADS_FILTER_FROM_DATE))
                return PreferenceManager.getDefaultSharedPreferences(context).getString(LEADS_FILTER_FROM_DATE, null);
            else{
                Log.d(TAG, "From Date doesnt exists in SP");
                Calendar calendar = AppUtils.getCalendarInstance(context);
                currentDate = AppUtils.formatDate(context, null, calendar);
                return currentDate;
            }
        }//else context is not null closes here...
    }//getFromDate closes here....


    public static String getLeadsFilter_ToDate(){
//		PreferenceManager.getDefaultSharedPreferences(context).
        if(context == null){
            //User has not edited the From & To date till now....
            Calendar calendar = AppUtils.getCalendarInstance(context);
            currentDate = AppUtils.formatDate(context, null, calendar);
//			Log.d(TAG, "Returning Current Date = "+currentDate);
            return currentDate;
        }//if(context == null) closes here....
        else{
            if(PreferenceManager.getDefaultSharedPreferences(context).contains(LEADS_FILTER_TO_DATE))
                return PreferenceManager.getDefaultSharedPreferences(context).getString(LEADS_FILTER_TO_DATE, null);
            else{
                Log.d(TAG, "From Date doesnt exists in SP");
                Calendar calendar = AppUtils.getCalendarInstance(context);
                currentDate = AppUtils.formatDate(context, null, calendar);
                return currentDate;
            }
        }//else context is not null closes here...

    }//getFromDate closes here....

    public static String getLeadsFilter_SearchForm() {
        if(search.isEmpty()){
            return "%";
        }else{
            return search;
        }
    }

    public static void setLeadsFilter_FromDate(String formattedDate) {
        // TODO Auto-generated method stub
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(LEADS_FILTER_FROM_DATE, formattedDate).apply();
    }//setLeadsFilter_FromDate closes here....



    public static void setLeadsFilter_ToDate(String formattedDate) {
        // TODO Auto-generated method stub
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(LEADS_FILTER_TO_DATE, formattedDate).apply();
    }//setLeadsFilter_ToDate closes here....

    public void setFilterListener(FilterLeadsListener filterListenerInstance) {
        // TODO Auto-generated method stub
        this.mFilterLeadsListener = filterListenerInstance;
    }//setFilterListener closes here....


    // to make the search as empty so even after logout it will not contain previous searched form
    public static void setSearch(String search) {
        BottomSheetDialogFragments.search = search;
    }
}
