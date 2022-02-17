/**
 * 
 */
package com.icicisecurities.hub.custom;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;


import com.google.android.material.textfield.TextInputEditText;
import com.icicisecurities.hub.utils.AppUtils;
import com.icicisecurities.hub.utils.SELECT_RANGE_FOR_OPEN_CLOSED_CASES;

import java.util.Calendar;




/**
 * @author isec
 *
 */
@SuppressLint("ValidFragment")

public class DatePickerDialogFragment extends DialogFragment implements OnDateSetListener {

	//High priority UI variables goes below.....
	private Context context;
	private EditText dateEditText;
	private TextInputEditText mBtn;
	
	//Medium priority NON-UI variables goes below....
	private SELECT_RANGE_FOR_OPEN_CLOSED_CASES selectDate;
	//@selectDate : We store that user is changing the "FROM Date" or "TO Date". This helps in decision making. 
	
	//Least priority variables goes below....
	private final String TAG = "DatePickerDialogFragment";
	
	
	
	/**
	 * @param context : Context of the Accessing Activity....
	 * @param mDOBValueEdtTxt : When Date is selected & ok is clicked the Date shue be setted on this EditText.
	 * So pass the address of the EditText...
	 */
	public DatePickerDialogFragment(Context context, EditText mDOBValueEdtTxt) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.dateEditText = mDOBValueEdtTxt;
	}//DatePickerDialog constructor closes here....
	
	
	/**
	 * @param context : Context of the Accessing Activity....
	 * @param selectDate 
	 * @param v 
	 * @param mDOBValueEdtTxt : When Date is selected & ok is clicked the Date shue be setted on this EditText.
	 * So pass the address of the EditText...
	 */
	public DatePickerDialogFragment(Context context, TextInputEditText mBtn, SELECT_RANGE_FOR_OPEN_CLOSED_CASES selectDate) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mBtn = mBtn;
		this.selectDate = selectDate;
	}//DatePickerDialog constructor closes here....
	
	
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 // Use the current date as the default date in the picker
        Calendar c = AppUtils.getCalendarInstance(context);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, this, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        return datePickerDialog;

	}//onCreateDialog closes here....

	/* (non-Javadoc)
	 * @see android.app.DatePickerDialog.OnDateSetListener#onDateSet(android.widget.DatePicker, int, int, int)
	 */
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		// TODO Auto-generated method stub
		
		Calendar cal = AppUtils.getCalendarInstance(getActivity());
		cal.set(year, monthOfYear, dayOfMonth);
//		System.out.println(cal.getTime());
		// Output "Fri Nov 13 10:18:32 GMT+05:30 2015"

		String formattedDate = AppUtils.formatDate(getActivity(), DatePickerDialogFragment.this, cal);
		
		if(dateEditText != null)
			dateEditText.setText(formattedDate);
		
		if(mBtn != null)
			mBtn.setText(formattedDate);
		
		
		
		switch (selectDate) {
		case FROM_DATE:
//			FilterDialog.setLeadsFilter_FromDate(formattedDate);
			BottomSheetDialogFragments.setLeadsFilter_FromDate(formattedDate);

			break;
		case TO_DATE:
//			FilterDialog.setLeadsFilter_ToDate(formattedDate);
			BottomSheetDialogFragments.setLeadsFilter_ToDate(formattedDate);
			break;
		default:
			Log.w(TAG, "Unhandled case: "+selectDate);
			break;
		}//switch (selectDate) closes here...
	}//onDateSet closes here.....
	

}//DatePickerDialog class closes here.....

