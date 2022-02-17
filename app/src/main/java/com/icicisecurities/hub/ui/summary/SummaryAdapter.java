package com.icicisecurities.hub.ui.summary;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.icicisecurities.hub.R;
import com.icicisecurities.hub.ui.summary.model.SummaryVO;
import com.icicisecurities.hub.utils.AppUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SummaryAdapter extends ArrayAdapter<SummaryVO> {

    private TextView mFormNumberTxtView, mPANNumber, mStatusTxtView, mRejectedReasonTxtView, mSubmittedDateTxtView, mUpdatedDateTxtView;

    //Medium priority NON-UI variables goes below....
    private Context context;
    private int resource, screenWidth;
    private List<SummaryVO> leadsAl;
    private List<SummaryVO> copyList;
    private LayoutInflater inflater;
    private View rowView;


    //Least priority variables goes below....
    private final String TAG = "LeadsAdapter";

    public SummaryAdapter(Context context, int resource, List<SummaryVO> leadsAl) {
        super(context, resource, leadsAl);
        this.context = context;
        this.resource = resource;
        this.leadsAl = leadsAl;
        this.copyList = new ArrayList<>();
        copyList.addAll(leadsAl);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        screenWidth = AppUtils.getScreenWidth(context);
    }//LeadsAdapter constructor closes here....


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
//        		super.getView(position, convertView, parent);

        convertView = inflater.inflate(resource, null , false);
        rowView = convertView;
        //Generating Events now below....
        mFormNumberTxtView = (TextView) convertView.findViewById(R.id.leftTxtView);
        mStatusTxtView = (TextView) convertView.findViewById(R.id.rightTxtView);
        mPANNumber = (TextView) convertView.findViewById(R.id.middleTxtView);
        mRejectedReasonTxtView = (TextView) convertView.findViewById(R.id.rejectedReasonTxtView);
        mSubmittedDateTxtView = convertView.findViewById(R.id.submittedDateTxtView);
        mUpdatedDateTxtView = convertView.findViewById(R.id.updatedDateTxtView);


        //Setting Data now....
        mFormNumberTxtView.setText(leadsAl.get(position).getFormNumber());
		mStatusTxtView.setText(leadsAl.get(position).getStatus());
		String submittedDateText = leadsAl.get(position).getSubmittedDate();
        String updatedDateText = leadsAl.get(position).getUpdatedDate();

		mSubmittedDateTxtView.setText(submittedDateText);
        mUpdatedDateTxtView.setText(updatedDateText);

        String rejectedFlag = leadsAl.get(position).getRejectedFlag().toString().trim();

        switch (rejectedFlag) {
            case "y":
            case "Y":
                //This is rejected Case....
                mStatusTxtView.setText(leadsAl.get(position).getStatus());
                mRejectedReasonTxtView.setVisibility(View.VISIBLE);
                mRejectedReasonTxtView.setText(rejectedSpanned(leadsAl.get(position).getRejectedReason()));
                break;
            default:
//                mRejectedReasonTxtView.setVisibility(View.GONE);
                mStatusTxtView.setText(leadsAl.get(position).getStatus());
                break;
        }//switch (rejectedFlag) closes here....


        //Making the ListView as per Device Screen width...
        mFormNumberTxtView.setWidth(screenWidth / 2);
//        mPANNumber.setWidth(screenWidth / 3);
        mStatusTxtView.setWidth(screenWidth / 2);


        return convertView;
    }//getView closes here....


    private Spanned acceptedSpanned() {
        // TODO Auto-generated method stub
        return Html.fromHtml("<font color='" + context.getResources().getColor(R.color.drkGreenColor) + "'>" + context.getResources().getString(R.string.acceptedSpelling) + "</font>");
    }


    private Spanned rejectedSpanned(String rejectedReason) {
        // TODO Auto-generated method stub

        return Html.fromHtml("<font color='" + context.getResources().getColor(R.color.drkRedColor) + "'>" + rejectedReason + "</font>");
    }//rejectedSpanned closes here....


    @Override
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub

//        Log.d(TAG , "isEnabled --> position ---> "+position);

        if(!leadsAl.isEmpty()) {
            if (leadsAl.get(position).getRejectedFlag().equalsIgnoreCase("Y")) {
                //The case is rejected therefore this will be disabled....
                rowView.setClickable(true);
                return true;
            } else
                rowView.setClickable(false);
            return true;//Bcoz case is not rejected...
        }
        return true;
    }//isEnabled closes here....


    public void filter(String queryText) {
        leadsAl.clear();

        if(queryText.isEmpty())
        {
            leadsAl.addAll(copyList);
        }
        else
        {

            for(SummaryVO name: copyList)
            {
                if(name.getFormNumber().toLowerCase().contains(queryText.toLowerCase()))
                {
                    leadsAl.add(name);
                }
            }

        }

        notifyDataSetChanged();
    }
}
