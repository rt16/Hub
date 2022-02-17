package com.icicisecurities.hub.ui.fatca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.icicisecurities.hub.R;
import com.icicisecurities.hub.ui.fatca.model.FatcaVO;

import java.util.ArrayList;

/**
 * Created by yagneshparikh on 27/10/16.
 */
public class FatcaCountryAdapter extends ArrayAdapter<FatcaVO> {

    //High priority UI variables goes below.....
    private Context context;
    private int resource;

    //Medium priority NON-UI variables goes below.....
    private ArrayList<FatcaVO> listObject;
    private LayoutInflater inflater;



    //Least priority variables goes below....
    private final String TAG = "KeyValueAdapter";

    public FatcaCountryAdapter(Context context, int resource, ArrayList<FatcaVO> listObject) {
        super(context, resource, listObject);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.resource = resource;
        this.listObject = listObject;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }//PersonalDetailsEducationAdapter constructor closes here.....


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return  getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return  getCustomView(position, convertView, parent);
    }//getDropDownView closes here....

    public View getCustomView(int position, View convertView, ViewGroup parent){

        convertView = inflater.inflate(R.layout.spinner_row_layout, null);

        /////////////............INITIALIZATIONS GOES BELOW.............\\\\\\\\\\\\\\\\\\\\
        TextView txtView = (TextView) convertView.findViewById(R.id.spinnerTextView);

        ////////////.............SETTING DATA..........\\\\\\\\\\\\\\\\\\\\
        FatcaVO object = listObject.get(position);//This will give the POJO object at this position....
        txtView.setText(object.getCountryName());

        return convertView;

    }//getCustomView closes here....
}
